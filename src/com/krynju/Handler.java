package com.krynju;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Handler {
    public Player PlayerObject;
    private LinkedList<GameObject> objectList = new LinkedList<GameObject>();
    public void addPlayer(Player obj){
        objectList.add(obj);
        PlayerObject = obj;
    }
    public void addObject(GameObject obj){
        objectList.add(obj);
    }
    public void removeObject(GameObject obj){
        objectList.remove(obj);
    }

    public void tick(double timeElapsedSeconds){
        Iterator iter = objectList.iterator();
        for (GameObject obj : objectList) {
            obj.tick(timeElapsedSeconds);
        }
    }
    public void render(Graphics g){
        Iterator iter = objectList.iterator();
        for (GameObject obj : objectList) {
            obj.render(g);
        }
    }
}
