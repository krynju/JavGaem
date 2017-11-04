package com.krynju;

import com.krynju.modules.*;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class Model {
    public static Field field;
    public static Player PlayerObject;
    public static Bomb bomb;
    private static LinkedList<GameObject> objectList = new LinkedList<GameObject>();

    public Model() {

    }

    public void initialiseGameObjects(){
        field = new Field();

        bomb = new Bomb(0,0);
        objectList.add(bomb);
        PlayerObject = new Player(0, 0, 0, 0);
        objectList.add(PlayerObject);

        /*walls*/
        objectList.add(new DestroyableWall(1, 1));
        objectList.add(new DestroyableWall(2, 2));
        objectList.add(new NormalWall(4,4));
    }

    public void tick(double timeElapsedSeconds) {
        Iterator iter = objectList.iterator();
        for (GameObject obj : objectList) {
            obj.tick(timeElapsedSeconds);
        }
    }

    public static void render(Graphics g) {
        Field.render(g);
        Iterator iter = objectList.iterator();

        for (GameObject obj : objectList) {
            obj.render(g);
        }
    }
}
