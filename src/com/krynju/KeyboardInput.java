package com.krynju;

import com.krynju.enums.Direction;
import com.krynju.enums.Ending;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class KeyboardInput extends KeyAdapter {
    private Controller controller;
    public LinkedList<Direction> queuedKeys = new LinkedList<>();
    private boolean upInQueue = false;
    private boolean downInQueue = false;
    private boolean leftInQueue = false;
    private boolean rightInQueue = false;
    private boolean placeBomb = false;

    KeyboardInput(Controller controller){
        this.controller=controller;
    }

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

            if(!controller.isPaused())
                placeBomb = true;
            if(controller.isPaused()){
                controller.setPause(false);
            }
        }
        if(key== KeyEvent.VK_P){
            controller.setPause(!controller.isPaused());
        }

        if(key==KeyEvent.VK_R){
            if(controller.isGameEnd()) {
                controller.setGameEnd(false);
            }
        }

//        if(key==KeyEvent.VK_Y){
//            controller.setEndingType(Ending.draw);
//            controller.setGameEnd(true);
//        }
//        if(key==KeyEvent.VK_U){
//            controller.setEndingType(Ending.win);
//            controller.setGameEnd(true);
//        }
//        if(key==KeyEvent.VK_I){
//            controller.setEndingType(Ending.lose);
//            controller.setGameEnd(true);
//        }


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
