package com.krynju;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;


public class KeyboardInput extends KeyAdapter {
    public static boolean upInQueue = false;
    public static boolean downInQueue = false;
    public static boolean leftInQueue = false;
    public static boolean rightInQueue = false;

    public static LinkedList<Direction> queuedKeys = new LinkedList<Direction>();

    public static Direction keyPressedDown = Direction.none;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            if (!upInQueue) {
                queuedKeys.add(Direction.up);
                upInQueue = true;
            }
        }
        if (key == KeyEvent.VK_S) {
            if (!downInQueue) {
                queuedKeys.add(Direction.down);
                downInQueue = true;
            }
        }
        if (key == KeyEvent.VK_A) {
            if (!leftInQueue) {
                queuedKeys.add(Direction.left);
                leftInQueue = true;
            }
        }
        if (key == KeyEvent.VK_D) {
            if (!rightInQueue) {
                queuedKeys.add(Direction.right);
                rightInQueue = true;
            }
        }
        if (key == KeyEvent.VK_SPACE)
            Modules.PlayerObject.placeBomb();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            queuedKeys.remove(Direction.up);
            upInQueue = false;
        }
        if (key == KeyEvent.VK_S) {
            queuedKeys.remove(Direction.down);
            downInQueue = false;
        }
        if (key == KeyEvent.VK_A) {
            queuedKeys.remove(Direction.left);
            leftInQueue = false;
        }
        if (key == KeyEvent.VK_D) {
            queuedKeys.remove(Direction.right);
            rightInQueue = false;
        }

    }


}
