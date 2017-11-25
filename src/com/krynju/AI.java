package com.krynju;

import com.krynju.enums.Direction;
import com.krynju.modules.Field;
import com.krynju.modules.Tile;

import java.util.LinkedList;

class Node {
    Node(int x, int y, int step) {
        this.x = x;
        this.y = y;
        this.step = step;
    }

    Node(int x, int y, int step, Node prev) {
        this.x = x;
        this.y = y;
        this.step = step;
        this.previousNode = prev;
    }

    int x;  //node's x cord
    int y;  //node's y cord
    int step;  //node's step
    int destroyableWallsCount = 0;
    Node previousNode;
}

public class AI {
    private Model model;
    public boolean delayFlag = false;
    public boolean setDelay;

    AI(Model model) {
        this.model = model;
    }

    Direction mainAIAlgorithm() {
        /*bomb range check - if positive then run away from the bomb*/
        Tile enemyTile = Field.getTileRef(model.enemy.getTileCordX(), model.enemy.getTileCordY());
        if (enemyTile.isBombDanger() || enemyTile.isBombed())
            return runAwayFromBombAlgorithm();

        /*initialising the lists used for the path finding algorithm*/
        LinkedList<Node> list = new LinkedList<>();
        LinkedList<Node> tempList = new LinkedList<>();
        LinkedList<Node> removeList = new LinkedList<>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.player.getTileCordX(), model.player.getTileCordY(), 0));

        int i = 0;  //step counter
        pathloop:
        while (true) {
            /*adding adjacent nodes to the temporary list
            *   A       O - the i-th node
            * A O A     A - adjacent nodes that are getting added
            *   A           they also have the step counter greater than the O node */
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).step + 1));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).step + 1));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).step + 1));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).step + 1));

            for (Node node : tempList) {
                Tile tile;  //temp tile

                /*checking if the node's cords are valid*/
                if (node.x < 0 || node.y < 0 || node.x >= Field.tileCountX || node.y >= Field.tileCountY) {
                    removeList.add(node);  //if not then add the node to the removeList and continue
                    continue;           //items in the removeList are going to be later removed from the tempList
                } else
                    tile = Field.getTileRef(node.x, node.y);  //if it's a valid tile then get its reference

                /*checking for walls and bombs*/
                if (tile.isWallOnTile() || tile.isBombed() || tile.isBombDanger()) {
                    removeList.add(node);
                    continue;
                }

                /*checking for nodes in the list that have
                * the same cords and an equal or lesser step*/
                for (Node O : list) {
                    if (O.x == node.x && O.y == node.y && O.step <= node.step) {
                        removeList.add(node);
                        break;
                    }
                }
            }
            /*remove nodes from the tempList that got flagged as "to be removed"*/
            tempList.removeAll(removeList);
            /*adds the remaining nodes to the list*/
            list.addAll(tempList);
    

            /*checking if the tempList contained a node that has the same cords
            * as the controlled object(enemy) */
            for (Node T : tempList) {
                /*if a node like this is found then a flag is set that is going to break the loop*/
                if (T.x == model.enemy.getTileCordX() && T.y == model.enemy.getTileCordY()) {
                    /*SET A BOMB IF POSSIBLE*/
                    if (T.step == 1) {
                        setBombAlgorithm();
                        System.out.println("found the target - setting the bomb");
                    }
                    break pathloop;
                }
            }

            /*if all possible paths have been discovered and the controlled object hasn't been found
            * then run an algorithm that's trying to get as close to the target as possible*/
            if (list.getLast() == list.get(i) && tempList.isEmpty())
                /*getCloseToTargetAlgorithm can set a delayFlag, which happens when the ai player
                * stands next to a bombsite and is waiting for the bomb to explode
                * the delay happens AFTER the bomb explodes*/
                return getCloseToTargetAlgorithm();

            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();
            i++;                //increment the step counter
        }

        /*checking the delayFlag, if the program got to this point then the bomb has already exploded
        * it is the moment when the delay starts, setting the setDelay flag will freeze the ai players
        * movement for the set delay delayTimeCounter*/
        if (delayFlag) {
            delayFlag = false;
            setDelay = true;
            return Direction.none;
        }

        /*fetch the node that found the tile with the controlled object
        * and return a direction in which the controlled object should move */
        Node node = list.get(i);
        Tile tile = Field.getTileRef(node.x, node.y);
        if (tile.isBombDanger() || tile.isBombed())     //also check if the move is dangerous
            return (Direction.none);

        /*process the chosen node*/
        return chooseDirection(node.x, node.y);
    }

    private void setBombAlgorithm() {
        /*initialising the lists used for the path finding algorithm*/
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.enemy.getTileCordX(), model.enemy.getTileCordY(), 0, null));

        int i = 0;  //step counter
        pathloop:
        while (true) {
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).step + 1, list.get(i)));

            for (Node node : tempList) {
                Tile tile;  //temp tile

                /*checking if the node's cords are valid*/
                if (node.x < 0 || node.y < 0 || node.x >= Field.tileCountX || node.y >= Field.tileCountY) {
                    removeList.add(node);   //if not then add the node to the removeList and continue
                    continue;               //items in the removeList are going to be later removed from the tempList
                } else
                    tile = Field.getTileRef(node.x, node.y);  //if it's a valid tile then get its reference

                /*checking for walls and player*/
                if (tile.isWallOnTile() || tile.isBombed() || tile.isPlayerOnTile()) {
                    removeList.add(node);
                    continue;
                }

                /*checking for nodes in the list that have
                * the same cords and an equal or lesser step*/
                for (Node O : list) {
                    if (O.x == node.x && O.y == node.y && O.step <= node.step) {
                        removeList.add(node);
                        break;
                    }
                }
            }

            tempList.removeAll(removeList); //remove nodes from the tempList that got flagged as "to be removed"
            list.addAll(tempList);          //adds the remaining nodes to the list
            if (list.getLast() == list.get(i) && tempList.isEmpty()) {
                return;
            }
            if (i > 0) {
            /*check if there's a tile that's not endangered*/
                for (Node T : tempList) {
                    Tile tempTile = Field.getTileRef(T.x, T.y);
                    if (!tempTile.isBombDanger() && !tempTile.isBombed()) {//FIXED THIS SHIT, MISSING ISBOMBED
                        model.enemy.placeBomb();
                        break pathloop;
                    }
                }
            }
            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();
            i++;                //increment the step counter
        }
    }

    private Direction runAwayFromBombAlgorithm() {
        /*initialising the lists used for the path finding algorithm*/
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.enemy.getTileCordX(), model.enemy.getTileCordY(), 0, null));

        int i = 0;  //step counter
        pathloop:
        while (true) {
            /*adding adjacent nodes to the temporary list
            *   A       O - the i-th node
            * A O A     A - adjacent nodes that are getting added
            *   A           they also have the step counter greater than the O node */
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).step + 1, list.get(i)));

            for (Node node : tempList) {
                Tile tile;  //temp tile

                /*checking if the node's cords are valid*/
                if (node.x < 0 || node.y < 0 || node.x >= Field.tileCountX || node.y >= Field.tileCountY) {
                    removeList.add(node);   //if not then add the node to the removeList and continue
                    continue;               //items in the removeList are going to be later removed from the tempList
                } else
                    tile = Field.getTileRef(node.x, node.y);  //if it's a valid tile then get its reference

                /*checking for walls and player*/
                if (tile.isWallOnTile() || tile.isBombed() || tile.isPlayerOnTile()) {
                    removeList.add(node);
                    continue;
                }

                /*checking for nodes in the list that have
                * the same cords and an equal or lesser step*/
                for (Node O : list) {
                    if (O.x == node.x && O.y == node.y && O.step <= node.step) {
                        removeList.add(node);
                        break;
                    }
                }
            }

            tempList.removeAll(removeList); //remove nodes from the tempList that got flagged as "to be removed"
            list.addAll(tempList);          //adds the remaining nodes to the list

            /*check if there's a tile that's not endangered*/
            for (Node T : list) {
                Tile tempTile = Field.getTileRef(T.x, T.y);
                if (!tempTile.isBombDanger() && !tempTile.isBombed()) {//FIXED THIS SHIT, MISSING ISBOMBED
                    i = list.indexOf(T);
                    break pathloop;
                }
            }
            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();
            i++;                //increment the step counter
        }

        System.out.println("avoiding bomb");

        Node node = list.get(i);
        /*using saved previousNodes to find the node with step = 1*/
        while (node.previousNode != list.getFirst())
            node = node.previousNode;

        return chooseDirection(node.x, node.y);
    }

    private Direction getCloseToTargetAlgorithm() {
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        list.add(new Node(model.player.getTileCordX(), model.player.getTileCordY(), 0));

        int i = 0;  //step counter

        pathloop:
        while (true) {
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).step + 1));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).step + 1));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).step + 1));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).step + 1));

            for (Node node : tempList) {
                Tile tile;
                /*checking if the node's cords are valid*/
                if (node.x < 0 || node.y < 0 || node.x >= Field.tileCountX || node.y >= Field.tileCountY) {
                    removeList.add(node);  //if not then add the node to the removeList and continue
                    continue;           //items in the removeList are going to be later removed from the tempList
                } else {
                    tile = Field.getTileRef(node.x, node.y);
                }

                if (tile.isWallOnTile()) {
                    removeList.add(node);
                    continue;
                }

                /*checking for nodes in the list that have
                * the same cords and an equal or lesser step*/
                for (Node O : list) {
                    if (O.x == node.x && O.y == node.y && O.step <= node.step) {
                        removeList.add(node);
                        break;
                    }
                }
            }

            tempList.removeAll(removeList); //remove nodes from the tempList that got flagged as "to be removed"
            list.addAll(tempList);          //adds the remaining nodes to the list

            /*checking if the tempList contained a node that has the same cords
            * as the controlled object(enemy) */
            for (Node T : tempList) {
                /*if a node like this is found then a flag is set that is going to break the loop*/
                if (T.x == model.enemy.getTileCordX() && T.y == model.enemy.getTileCordY()) {
                    //System.out.println("found the target in the getCloseToTargetAlgorithm");
                    break pathloop;
                }
            }

            /*out of moves - break walls*/
            if (list.getLast() == list.get(i) && tempList.isEmpty()) {
                //System.out.println("out of moves in getCloseToTargetAlgorithm");
                return findBreakableWallsAlgorithm();
            }

            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();
            i++;                //increment the step counter
        }


        /*movement direction decisions*/
        Node node = list.get(i);
        Tile tile = Field.getTileRef(node.x, node.y);
        if (tile.isBombDanger() || tile.isBombed()) {
            System.out.println("setting the delayFlag");
            delayFlag = true;
            return (Direction.none);
        }

        return chooseDirection(node.x, node.y);
    }

    private Direction findBreakableWallsAlgorithm() {
        if (model.enemy.bomb.isBombSet())
            return Direction.none;

        /*initialising the lists used for the path finding algorithm*/
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.enemy.getTileCordX(), model.enemy.getTileCordY(), 0, null));

        int i = 0;  //step counter
        pathloop:
        while (true) {
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).step + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).step + 1, list.get(i)));

            for (Node node : tempList) {
                Tile tile;  //temp tile

                /*checking if the node's cords are valid*/
                if (node.x < 0 || node.y < 0 || node.x >= Field.tileCountX || node.y >= Field.tileCountY) {
                    removeList.add(node);   //if not then add the node to the removeList and continue
                    continue;               //items in the removeList are going to be later removed from the tempList
                } else
                    tile = Field.getTileRef(node.x, node.y);  //if it's a valid tile then get its reference

                if (tile.destroyable) {
                    list.get(i).destroyableWallsCount++;
                }

                /*checking for walls and player*/
                if (tile.isWallOnTile() /*|| tile.isBombed() || tile.isPlayerOnTile()*/) {
                    removeList.add(node);
                    continue;
                }

                /*checking for nodes in the list that have
                * the same cords and an equal or lesser step*/
                for (Node O : list) {
                    if (O.x == node.x && O.y == node.y && O.step <= node.step) {
                        removeList.add(node);
                        break;
                    }
                }
            }

            tempList.removeAll(removeList); //remove nodes from the tempList that got flagged as "to be removed"
            list.addAll(tempList);          //adds the remaining nodes to the list

            if (list.getLast() == list.get(i) && tempList.isEmpty()) {
                break;
            }
            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();
            i++;                //increment the step counter
        }

        Node maxNode = list.getFirst();
        for (Node n : list) {
            if (n.destroyableWallsCount > maxNode.destroyableWallsCount)
                maxNode = n;
        }

        if (maxNode == list.getFirst()) {
            setBombAlgorithm();
            return Direction.none;
        }

        /*using saved previousNodes to find the node with step = 1*/
        while (maxNode.previousNode != list.getFirst())
            maxNode = maxNode.previousNode;
        return chooseDirection(maxNode.x, maxNode.y);

    }

    private Direction chooseDirection(int x, int y) {
        if (x > model.enemy.getTileCordX())
            return (Direction.right);
        if (x < model.enemy.getTileCordX())
            return (Direction.left);
        if (y > model.enemy.getTileCordY())
            return (Direction.down);
        if (y < model.enemy.getTileCordY())
            return (Direction.up);
        else
            return (Direction.none);
    }
}


