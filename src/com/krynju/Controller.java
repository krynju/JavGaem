package com.krynju;

import com.krynju.enums.Direction;
import com.krynju.modules.GameObject;

import java.awt.Canvas;

public class Controller extends Canvas implements Runnable {
    private Thread thread;
    private Model model;
    private KeyboardInput keyboardInput;
    private AI ai;
    private boolean running = false;

    public Controller(Model model) {
        this.ai = new AI(model);
        this.model = model;
        this.keyboardInput = new KeyboardInput();
        this.start();
    }

    synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        double lastTick = System.nanoTime();
        while (running) {
            double now = System.nanoTime();

            analyzeKeyboardInput();
            tick((now - lastTick) / 1000000000);

            lastTick = now;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    private void tick(double timeElapsedSeconds) {
        for (GameObject obj : model.objectList) {
            obj.tick(timeElapsedSeconds);
        }
    }

    private void analyzeKeyboardInput() {
        Direction dir;

        if(/*keyboardInput.isShit() && */model.enemy.isAtDestination()) {
            ai.test();
            keyboardInput.setShit(false);
        }

        /*Bomb placement*/
        if (keyboardInput.isPlaceBomb()) {
            model.player.placeBomb();
            keyboardInput.setPlaceBomb(false);
        }

        /*Fetching keyboard input from the queue*/
        Direction directionLast;
        try {
            directionLast = keyboardInput.queuedKeys.getLast();
        } catch (Exception e) {
            return;
        }
        /*Basic movement - object not moving and key pressed*/
        if (model.player.isAtDestination()) {    //if at destination
            try {
                model.player.move(directionLast);//then try to move in the fetched direction
            } catch (Exception ignored) {       //if not possible to move that way
                Direction directionLastMinus;
                try {                           //try to fetch last-1 direction from the queue
                    directionLastMinus = keyboardInput.queuedKeys.get(keyboardInput.queuedKeys.indexOf(directionLast) - 1);
                } catch (Exception e) {         //if it doesn't exist then return
                    return;
                }

                /*if it exists check if the direction1 and direction2 aren't opposite
                * to avoid going back and forth near walls when opposite keys are pressed down*/
                if (directionLast == Direction.down && directionLastMinus == Direction.up ||
                        directionLast == Direction.up && directionLastMinus == Direction.down ||
                        directionLast == Direction.left && directionLastMinus == Direction.right ||
                        directionLast == Direction.right && directionLastMinus == Direction.left)
                    return;

                try {
                    model.player.move(directionLastMinus);  //if everything is ok then try to move in the direction2
                } catch (Exception ignored2) {}
            }
        }
    }


    public KeyboardInput getKeyboardInput() {
        return keyboardInput;
    }
}
