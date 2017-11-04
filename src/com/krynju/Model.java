package com.krynju;

import com.krynju.modules.*;

import java.util.LinkedList;

public class Model {
    public Field field;
    public Player player;
    public LinkedList<GameObject> objectList = new LinkedList<GameObject>();

    public Model() {
        field = new Field();

        Bomb bomb = new Bomb(0,0);
        objectList.add(bomb);
        player = new Player(0, 0, 0, 0,bomb);
        objectList.add(player);

        /*walls*/
        objectList.add(new DestroyableWall(1, 1));
        objectList.add(new DestroyableWall(2, 2));
        objectList.add(new NormalWall(4,4));
    }
}
