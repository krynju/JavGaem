package com.krynju;

import com.krynju.modules.Bomb;
import com.krynju.modules.GameObject;
import com.krynju.modules.Player;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class Modules {
    public static Field field;
    public static Player PlayerObject;
    public static Bomb bomb;
    private static LinkedList<GameObject> objectList = new LinkedList<GameObject>();

    public Modules() {
        field = new Field();
        bomb = new Bomb(0,0,0,0);
        addObject(bomb);
    }

    public void addPlayer(Player obj) {
        objectList.add(obj);
        PlayerObject = obj;
    }

    public static void addObject(GameObject obj) {
        objectList.add(obj);
    }

    public static void removeObject(GameObject obj) {
        objectList.remove(obj);
    }

    public void tick(double timeElapsedSeconds) {
        Iterator iter = objectList.iterator();
        for (GameObject obj : objectList) {
            obj.tick(timeElapsedSeconds);
        }
    }

    public void render(Graphics g) {
        Field.render(g);
        Iterator iter = objectList.iterator();

        for (GameObject obj : objectList) {
            obj.render(g);
        }
    }
}
