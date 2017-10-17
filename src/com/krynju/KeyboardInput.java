package com.krynju;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyboardInput extends KeyAdapter {
    private int movementSpeed = 100;
    private Handler handler;

    public KeyboardInput(Handler handler){
        this.handler = handler;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W)
            handler.PlayerObject.setyVel(-movementSpeed);
        if(key == KeyEvent.VK_S)
            handler.PlayerObject.setyVel(movementSpeed);
        if(key == KeyEvent.VK_A)
            handler.PlayerObject.setxVel(-movementSpeed);
        if(key == KeyEvent.VK_D)
            handler.PlayerObject.setxVel(movementSpeed);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W)
            handler.PlayerObject.setyVel(0);
        if(key == KeyEvent.VK_S)
            handler.PlayerObject.setyVel(0);
        if(key == KeyEvent.VK_A)
            handler.PlayerObject.setxVel(0);
        if(key == KeyEvent.VK_D)
            handler.PlayerObject.setxVel(0);
    }
}
