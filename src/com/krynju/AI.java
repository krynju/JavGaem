package com.krynju;

import com.krynju.enums.Direction;
import com.krynju.modules.Field;
import com.krynju.modules.Tile;
import java.util.LinkedList;

class Node {
    Node(int x, int y, int c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }
    Node(int x, int y, int c, Node prev) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.previousNode = prev;
    }

    int x;  //node's x cord
    int y;  //node's y cord
    int c;  //node's step
    Node previousNode;
}

public class AI {
    private Model model;

    AI(Model model) {
        this.model = model;
    }

    Direction runOneStep() {
        Tile enemyTile = Field.getTileRef(model.enemy.getTileCordX(),model.enemy.getTileCordY());
        if(enemyTile.isBombDanger() || enemyTile.isBombed())
            return runFromBomb();

        /*initialising the lists used for the path finding algorithm*/
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.player.getTileCordX(), model.player.getTileCordY(), 0));

        int i = 0;  //step counter
        pathloop:
        while (true) {
            /*adding adjacent nodes to the temporary list
            *   A       O - the i-th node
            * A O A     A - adjacent nodes that are getting added
            *   A           they also have the step counter greater than the O node */
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).c + 1));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).c + 1));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).c + 1));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).c + 1));

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
                    if (O.x == node.x && O.y == node.y && O.c <= node.c) {
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
                    //System.out.println("found the target");
                    break pathloop;
                }
            }

            /*out of moves*/
            if(list.getLast() == list.get(i) && tempList.isEmpty()){
                System.out.println("out of moves");
                return getClose();      //run the algorithm without bomb checking to get close to the target
            }

            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();

            i++;                //increment the step counter
        }

        /*fetch the node that found the tile with the controlled object
        * and return a direction in which the controlled object should move */

        Node node = list.get(i);
        Tile tile = Field.getTileRef(node.x, node.y);
        if (tile.isBombDanger() || tile.isBombed())
            return(Direction.none);
        return chooseDirection(node.x,node.y);
    }

    private Direction runFromBomb() {
        /*initialising the lists used for the path finding algorithm*/
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.enemy.getTileCordX(), model.enemy.getTileCordY(), 0,null));

        int i = 0;  //step counter
        pathloop:
        while (true) {
            /*adding adjacent nodes to the temporary list
            *   A       O - the i-th node
            * A O A     A - adjacent nodes that are getting added
            *   A           they also have the step counter greater than the O node */
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).c + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).c + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).c + 1, list.get(i)));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).c + 1, list.get(i)));

            for (Node node : tempList) {
                Tile tile;  //temp tile

                /*checking if the node's cords are valid*/
                if (node.x < 0 || node.y < 0 || node.x >= Field.tileCountX || node.y >= Field.tileCountY) {
                    removeList.add(node);  //if not then add the node to the removeList and continue
                    continue;           //items in the removeList are going to be later removed from the tempList
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
                    if (O.x == node.x && O.y == node.y && O.c <= node.c) {
                        removeList.add(node);
                        break;
                    }
                }
            }

            tempList.removeAll(removeList); //remove nodes from the tempList that got flagged as "to be removed"
            list.addAll(tempList);          //adds the remaining nodes to the list

            /*check if there's a tile that's not endangered*/
            for (Node T : list) {
                if (!Field.getTileRef(T.x,T.y).isBombDanger()) {
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
        while(node.previousNode != list.getFirst())
            node = node.previousNode;
        return chooseDirection(node.x, node.y);
    }

    private Direction getClose(){
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.player.getTileCordX(), model.player.getTileCordY(), 0));

        int i = 0;  //step counter

        pathloop:
        while (true) {
            /*adding adjacent nodes to the temporary list
            *   A       O - the i-th node
            * A O A     A - adjacent nodes that are getting added
            *   A           they also have the step counter greater than the O node */
            tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).c + 1));
            tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).c + 1));
            tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).c + 1));
            tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).c + 1));


            for (Node node : tempList) {
                /*checking if the node's cords are valid*/
                if (node.x < 0 || node.y < 0 || node.x >= Field.tileCountX || node.y >= Field.tileCountY) {
                    removeList.add(node);  //if not then add the node to the removeList and continue
                    continue;           //items in the removeList are going to be later removed from the tempList
                }
                /*checking for nodes in the list that have
                * the same cords and an equal or lesser step*/
                for (Node O : list) {
                    if (O.x == node.x && O.y == node.y && O.c <= node.c) {
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
                    System.out.println("found the target in the getClose");
                    break pathloop;
                }
            }


            /*out of moves*/
            if(list.getLast() == list.get(i) && tempList.isEmpty()){
                System.out.println("out of moves in getClose");
                return Direction.none;
                //return Direction.none;
            }

            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();

            i++;                //increment the step counter
        }


        /*movement direction decisions*/
        Node node = list.get(i);
        Tile tile = Field.getTileRef(node.x, node.y);
        if (tile.isBombDanger() || tile.isBombed())
            return(Direction.none);

        return chooseDirection(node.x, node.y);
    }

    private Direction chooseDirection(int x, int y){
        if (x > model.enemy.getTileCordX())
            return(Direction.right);
        if (x < model.enemy.getTileCordX())
            return(Direction.left);
        if (y > model.enemy.getTileCordY())
            return(Direction.down);
        if (y < model.enemy.getTileCordY())
            return(Direction.up);
        else
            return(Direction.none);
    }
}


