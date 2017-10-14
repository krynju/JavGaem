package com.krynju;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Handler {
    private LinkedList<GameObject> objectList = new LinkedList<GameObject>();

    public void addObject(GameObject obj){
        objectList.add(obj);
    }

    public void tick(){
        Iterator iter = objectList.iterator();
        for (GameObject obj : objectList) {
            obj.tick();
        }
    }
    public void render(Graphics g){
        Iterator iter = objectList.iterator();
        for (GameObject obj : objectList) {
            obj.render(g);
        }
    }
}
