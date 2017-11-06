package com.krynju;

import com.krynju.enums.Direction;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class KeyboardInput extends KeyAdapter {
    public LinkedList<Direction> queuedKeys = new LinkedList<Direction>();
    private boolean upInQueue = false;
    private boolean downInQueue = false;
    private boolean leftInQueue = false;
    private boolean rightInQueue = false;
    private boolean placeBomb = false;

    public boolean isShit() {
        return shit;
    }

    public void setShit(boolean shit) {
        this.shit = shit;
    }

    private boolean shit = false;

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
        if (key == KeyEvent.VK_SPACE) {
            placeBomb = true;
        }

        if(key==KeyEvent.VK_G){
            shit = true;
        }

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

    public boolean isPlaceBomb() {
        return placeBomb;
    }

    public void setPlaceBomb(boolean placeBomb) {
        this.placeBomb = placeBomb;
    }
}
