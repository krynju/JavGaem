package com.krynju;

import com.krynju.ai.AI;
import com.krynju.enums.Direction;
import com.krynju.enums.Ending;
import com.krynju.modules.GameObject;

import java.util.LinkedList;

/**
 * The Controller class controls the flow of the game,
 * runs a thread that updates the objects positions,
 * analyses the player's keyboard input
 * and runs the AI
 */
public class Controller implements Runnable {
    /**
     * Keyboard input reference, its the object that gets analysed in the controller
     */
    private KeyboardInput keyboardInput;
    private Thread thread;
    private Model model;
    private AI ai;
    /**
     * Thread running flag
     */
    private boolean running = false;
    /**
     * Time counter for the AI, used when the delay is set by the AI
     */
    private double AIDelayTimeCounter = 0;

    /**
     * Game paused flag (position update loop paused
     */
    private boolean paused = true;
    /**
     * Renderer loop paused (screen stops updating)
     */
    private boolean renderPaused = false;
    /**
     * Game ended marker, happens when win/lose/draw
     */
    private boolean gameEnd = false;
    /**
     * The way the game ended win/lose/draw
     */
    private Ending endingType;

    /**
     * The constructor creates a KeyboardInput and starts the thread
     */
    Controller() {
        this.keyboardInput = new KeyboardInput(this);
        this.start();
    }

    /**
     * Creates a thread and starts the position updating loop
     */
    private synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    /**
     * Stop thread
     */
    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Position updating loop
     */
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

    /**
     * AI decision making function and AI delay support
     */
    private void AIDecisions(double timeElapsedSeconds) {
        if (ai.isDelay()) {
            AIDelayTimeCounter += timeElapsedSeconds;
            if (AIDelayTimeCounter > Game.AI_DELAY && model.enemy.isAtDestination()) {
                try {
                    model.enemy.move(ai.mainAIAlgorithm());
                } catch (Exception ignored) {
                }
                AIDelayTimeCounter = 0;
                ai.setDelay(false);
            }
        } else if (model.enemy.isAtDestination()) {
            try {
                model.enemy.move(ai.mainAIAlgorithm());
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Function that updates all the GameObjects positions in the Model's list
     */
    private void tick(double timeElapsedSeconds) {
        for (GameObject obj : model.objectList) {
            obj.tick(timeElapsedSeconds);
        }
    }

    /**Adding the model to the local references*/
    public void addModel(Model model) {
        this.model = model;
        this.ai = new AI(model);
    }

    /**ObjectList getter
     * @return model.objectList reference*/
    public LinkedList<GameObject> getObjectList() {
        return model.objectList;
    }

    /**Analyses the players keyboard input*/
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

    /**Game ending function.
     * Pauses the controller's thread and then sets the gameEnd flag to true
     * Passing false to the function pauses the controller loop and the rendering loop
     * then reloads the model and ends the pause on the renderer*/
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

    /**Setting the pause only works in the normal game mode, if the GameEnd flag is on then it's not possible*/
    public void setPause(boolean running) {
        if (!this.isGameEnd())
            this.paused = running;
    }
}
