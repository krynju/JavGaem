package com.krynju;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyboardInput extends KeyAdapter {
    public static Direction keyPressedDown = Direction.none;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W)
            keyPressedDown = Direction.up;
        if(key == KeyEvent.VK_S)
            keyPressedDown = Direction.down;
        if(key == KeyEvent.VK_A)
            keyPressedDown = Direction.left;
        if(key == KeyEvent.VK_D)
            keyPressedDown = Direction.right;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressedDown = Direction.none;
    }
}
