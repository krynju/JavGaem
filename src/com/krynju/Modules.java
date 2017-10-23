package com.krynju;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class Modules {
    public Field field;
    public Player PlayerObject;
    private LinkedList<GameObject> objectList = new LinkedList<GameObject>();

    public Modules() {
        field = new Field();
    }


    public void addPlayer(Player obj) {
        objectList.add(obj);
        PlayerObject = obj;
    }

    public void addObject(GameObject obj) {
        objectList.add(obj);
    }

    public void removeObject(GameObject obj) {
        objectList.remove(obj);
    }

    public void tick(double timeElapsedSeconds) {
        Iterator iter = objectList.iterator();
        for (GameObject obj : objectList) {
            obj.tick(timeElapsedSeconds);
        }
    }

    public void render(Graphics g) {
        field.render(g);
        Iterator iter = objectList.iterator();

        for (GameObject obj : objectList) {
            obj.render(g);
        }
    }
}
