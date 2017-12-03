package com.krynju;

import com.krynju.enums.Direction;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * The main gamePanel's keyboard input class. It records the key's pressed by the user.
 * The data is then analysed by the controller
 */
public class KeyboardInput extends KeyAdapter {
    /**
     * List containing the keys that got pressed in the right order.
     * The last element is the key that was pressed most recently.
     * When a key is released it is removed from this list
     */
    public LinkedList<Direction> queuedKeys = new LinkedList<>();
    /**
     * Controller reference
     */
    private Controller controller;
    /**
     * A marker that W was pressed
     */
    private boolean upInQueue = false;
    /**
     * A marker that S was pressed
     */
    private boolean downInQueue = false;
    /**
     * A marker that L was pressed
     */
    private boolean leftInQueue = false;
    /**
     * A marker that D was pressed
     */
    private boolean rightInQueue = false;
    /**
     * A marker that the bomb is queued to be set
     */
    private boolean placeBombQueued = false;

    /**
     * Constructor is only adding a reference to the controller,
     * necessary for pausing the game
     *
     * @see Controller
     */
    KeyboardInput(Controller controller) {
        this.controller = controller;
    }

    /**
     * Analyzing key presses.
     * WASD - adding to the queue and setting the right flag to true
     * SPACE - setting the bomb IF the game isn't paused
     * R - restarting the game when the game is finished
     */
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
            if (!controller.isPaused())
                placeBombQueued = true;
            if (controller.isPaused()) {
                controller.setPause(false);
            }
        }
        if (key == KeyEvent.VK_P) {
            controller.setPause(!controller.isPaused());
        }
        if (key == KeyEvent.VK_R) {
            if (controller.isGameEnd()) {
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

    /**
     * Removing the right key from the queue and setting off the right flag
     */
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

    /**
     * placeBombQueued getter
     *
     * @return placeBombQueued boolean value
     */
    public boolean isPlaceBombQueued() {
        return placeBombQueued;
    }

    /**
     * placeBombQueued setter
     */
    public void setPlaceBombQueued(boolean placeBombQueued) {
        this.placeBombQueued = placeBombQueued;
    }
}
