package com.krynju;

import com.krynju.ai.AI;
import com.krynju.enums.Direction;
import com.krynju.enums.Ending;
import com.krynju.modules.GameObject;

import java.util.LinkedList;

public class Controller implements Runnable {
    private KeyboardInput keyboardInput;
    private Thread thread;
    private Model model;
    private AI ai;
    private boolean running = false;
    private double delayTimeCounter = 0;

    /*Game flow flags*/
    private boolean paused = true;
    private boolean renderPaused = false;
    private boolean gameEnd = false;
    private Ending endingType;

    Controller() {
        this.keyboardInput = new KeyboardInput(this);
        this.start();
    }

    private synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    private synchronized void stop() {
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
            /*this pausing functionality*/
            if (this.isPaused()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lastTick = System.nanoTime();
                continue;
            }// end this pausing

            double now = System.nanoTime();
            AIDecisions((now - lastTick) / 1000000000);
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

    private void AIDecisions(double timeElapsedSeconds) {
        if (ai.isDelay()) {
            delayTimeCounter += timeElapsedSeconds;
            if (delayTimeCounter > Game.AI_DELAY && model.enemy.isAtDestination()) {
                try {
                    model.enemy.move(ai.mainAIAlgorithm());
                } catch (Exception ignored) {
                }
                delayTimeCounter = 0;
                ai.setDelay(false);
            }
        } else if (model.enemy.isAtDestination()) {
            try {
                model.enemy.move(ai.mainAIAlgorithm());
            } catch (Exception ignored) {
            }
        }
    }

    private void tick(double timeElapsedSeconds) {
        for (GameObject obj : model.objectList) {
            obj.tick(timeElapsedSeconds);
        }
    }

    public void addModel(Model model) {
        this.model = model;
        this.ai = new AI(model);
        this.model = model;
    }
    public LinkedList<GameObject> getObjectList() {
        return model.objectList;
    }

    private void analyzeKeyboardInput() {
        /*Bomb placement*/
        if (keyboardInput.isPlaceBombQueued()) {
            model.player.placeBomb();
            keyboardInput.setPlaceBombQueued(false);
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
                } catch (Exception ignored2) {
                }
            }
        }
    }
    public KeyboardInput getKeyboardInput() {
        return keyboardInput;
    }

    public Ending getEndingType() {
        return endingType;
    }
    public void setEndingType(Ending endingType) {
        this.endingType = endingType;
    }

    public boolean isRenderPaused() {
        return renderPaused;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }
    public void setGameEnd(boolean gameEnd) {
        if (gameEnd) {
            this.setPause(true);
            this.gameEnd = true;
        } else {
            this.setPause(true);
            renderPaused = true;
            model.reload();
            this.gameEnd = false;
            this.setPause(true);
            renderPaused = false;
        }
    }

    public boolean isPaused() {
        return paused;
    }
    public void setPause(boolean running) {
        if (!this.isGameEnd())
            this.paused = running;
    }
}
