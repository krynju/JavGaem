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

    int x, y, c;
}

public class AI {
    private Model model;
    private boolean flag = false;


    AI(Model model) {
        this.model = model;
    }

    Direction test() {

        /*initialising the lists used for the path finding algorithm*/
        LinkedList<Node> list = new LinkedList<Node>();
        LinkedList<Node> tempList = new LinkedList<Node>();
        LinkedList<Node> removeList = new LinkedList<Node>();

        /*adding the first node which is the players cords with a step 0*/
        list.add(new Node(model.player.getTileCordX(), model.player.getTileCordY(), 0));

        int i = 0;  //step counter
        while (true) {
            /*adding adjacent nodes to the temporary list
            *   A       O - the i-th node
            * A O A     A - adjacent nodes that are getting added
            *   A           they also have the step counter greater than the O node */
            try {
                tempList.add(new Node(list.get(i).x, list.get(i).y + 1, list.get(i).c + 1));
                tempList.add(new Node(list.get(i).x - 1, list.get(i).y, list.get(i).c + 1));
                tempList.add(new Node(list.get(i).x + 1, list.get(i).y, list.get(i).c + 1));
                tempList.add(new Node(list.get(i).x, list.get(i).y - 1, list.get(i).c + 1));
            } catch (Exception eg) {
                /*catching a situation when list.get(i) tries to access an element that is out of range
                * it means there were no elements added to the list in the last iteration
                * i is decremented to pass a valid index to the function later*/
                i--;
                break;
            }

            for (Node A : tempList) {
                Tile tile;  //temp tile
                /*checking if the node's cords are valid*/
                if (A.x < 0 || A.y < 0 || A.x >= Field.tileCountX || A.y >= Field.tileCountY) {
                    removeList.add(A);  //if not then add the node to the removeList and continue
                    continue;           //items in the removeList are going to be later removed from the tempList
                } else
                    tile = Field.getTileRef(A.x, A.y);  //if it's a valid tile then get its reference 

                /*checking if there's a wall on the tile*/
                if (tile.isWallOnTile()) {
                    removeList.add(A);
                    continue;
                }
                
                /*checking if there's a bomb on the tile*/
                if (tile.isBombed()) {
                    removeList.add(A);
                    continue;
                }
                
                /*checking if there is a node in the list that has 
                * the same cords and an equal or lesser step*/
                for (Node O : list) {
                    if (O.x == A.x && O.y == A.y && O.c <= A.c) {
                        removeList.add(A);
                        break;
                    }
                }
            }
            
            tempList.removeAll(removeList); //remove nodes from the tempList that got flagged as "to be removed"
            list.addAll(tempList);          //adds the remaining nodes to the list
    

            /*checking if the tempList contained a node that has the same cords
            * as the controlled object(enemy) */
            boolean flag = false;
            for (Node T : tempList) {
                /*if a node like this is found then a flag is set that is going to break the loop*/
                if (T.x == model.enemy.getTileCordX() && T.y == model.enemy.getTileCordY())
                    flag = true;
            }
            if (flag) break;    //break the loop
            
            removeList.clear(); //clear the lists before the next iteration
            tempList.clear();

            i++;                //increment the step counter
        }

        
        /*fetch the node that found the tile with the controlled object
        * and return a direction in which the controlled object should move */
        Node node = list.get(i);
        if (node.x > model.enemy.getTileCordX())
            return(Direction.right);
        if (node.x < model.enemy.getTileCordX())
            return(Direction.left);
        if (node.y > model.enemy.getTileCordY())
            return(Direction.down);
        if (node.y < model.enemy.getTileCordY())
            return(Direction.up);
        else
            return(Direction.none);
    


//        for (Node e : list) {
//            System.out.print(e.x);
//            System.out.print(e.y);
//            System.out.print(e.c);
//            System.out.println();
//        }
    }
}
