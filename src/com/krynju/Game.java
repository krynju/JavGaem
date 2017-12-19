package com.krynju;

import com.krynju.ai.AI;
import com.krynju.modules.Field;

import java.awt.*;

/**
 * Main Game static class
 * contains the main() function and properties of the Frame, key Gameobjects and AI
 */
public class Game {
    /**
     * JFrame's width
     */
    public static final int WIDTH = 835;
    /**
     * JFrame's height
     */
    public static final int HEIGHT = 675;
    /**
     * JFrame's title text
     */
    public static final String title = "Bomberman";
    /**
     * Framerate cap for the View's rendering loop
     */
    public static final int FRAMERATE = 120;

    public static final Color playerColor = new Color(0, 0, 255);
    public static final Color normalWallColor = new Color(0, 0, 0);
    public static final Color destroyableWallColor = new Color(255, 159, 99);
    public static final Color fieldColor = new Color(255, 255, 255);
    public static final Color enemyColor = new Color(255, 0, 0);
    public static final Color bombColor = new Color(255, 0, 0, 125);

    /**
     * Delay in seconds for the AI. The delay happens after the AI's path is blocked with a bomb,
     * it allows the player to beat AI's reaction time, which without the delay is impossible to beat
     *
     * @see AI
     */
    public static double AI_DELAY = 0.5;
    /**
     * AI's movement speed, it is doubled in the harder difficulty modes
     *
     * @see AI
     */
    public static int AI_SPEED = 3 * Field.tileSize;

    /**
     * The main() function creates the three main objects that make the game work
     * while creating them it passes the controller reference to the other objects
     *
     * @see Controller
     * @see View
     * @see Model
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        new Model(controller);
        new View(controller);
    }
}
