package com.krynju;

import com.krynju.modules.*;

import java.util.LinkedList;

public class Model {
    public Player player;
    public LinkedList<GameObject> objectList = new LinkedList<GameObject>();

    public Model() {

        new Field();
        Bomb bomb = new Bomb(0,0);
        objectList.add(bomb);
        player = new Player(0, 0, 0, 0,bomb);
        objectList.add(player);

        addWalls();

    }

    private void addWalls(){

        /*non destroyable walls*/
        for(int i = 1; i < Field.tileCountX; i+=2){
            for(int j = 1; j < Field.tileCountY; j+=2){
                objectList.add(new NormalWall(i,j));
            }
        }

        for(int i = 2; i < Field.tileCountX; i+=2){
            for(int j = 0; j < Field.tileCountY; j+=2){
                objectList.add(new DestroyableWall(i-1,j));
            }
        }

        for(int i = 2; i < Field.tileCountX -1; i+=2){
            for(int j = 1; j < Field.tileCountY; j+=2){
                objectList.add(new DestroyableWall(i,j));
            }
        }

    }
}
