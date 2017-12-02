package com.krynju;

import com.krynju.enums.ObjectID;
import com.krynju.modules.*;

import java.util.LinkedList;

public class Model {
    public Player player;
    public Player enemy;
    public LinkedList<GameObject> objectList = new LinkedList<GameObject>();

    public Model() {
        load();
    }
    private void load(){
        /*initiate tiles*/
        new Field();

        /*player*/
        Bomb bomb = new Bomb();
        player = new Player(ObjectID.player, 0, 0, Game.playerColor, bomb);


        /*AI*/
        Bomb bomb2 = new Bomb();
        enemy = new Player(ObjectID.enemy, Field.tileCountX - 1, Field.tileCountY - 1, Game.enemycolor, bomb2);


        objectList.add(player);
        objectList.add(enemy);
        objectList.add(bomb);
        objectList.add(bomb2);

        /*walls*/
        addWalls();
    }

    public void reload(){
        objectList.clear();
        load();
    }

    private void addWalls() {
        /*non destroyable walls*/
        for (int i = 1; i < Field.tileCountX; i += 2) {
            for (int j = 1; j < Field.tileCountY; j += 2) {
                objectList.add(new NormalWall(i, j));
            }
        }

        /*destroyable walls*/
        for (int i = 2; i < Field.tileCountX; i += 2) {
            for (int j = 0; j < Field.tileCountY; j += 2) {
                objectList.add(new DestroyableWall(i - 1, j));
            }
        }

        for (int i = 2; i < Field.tileCountX - 1; i += 2) {
            for (int j = 1; j < Field.tileCountY; j += 2) {
                objectList.add(new DestroyableWall(i, j));
            }
        }
    }
}
