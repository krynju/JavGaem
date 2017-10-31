package com.krynju;



import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyboardInput extends KeyAdapter {
    public Modules moduless;
    public KeyboardInput(Modules modules) {
        moduless = modules;
    }


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
        if(key == KeyEvent.VK_SPACE)
            moduless.PlayerObject.placeBomb();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!(e.getKeyCode() == KeyEvent.VK_SPACE))
            keyPressedDown = Direction.none;

    }
}
