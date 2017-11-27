package com.krynju;

import java.awt.*;

public class Game {
    public static final int WIDTH = 696;
    public static final int HEIGHT = 557;
    public static final int GAME_WIDTH = WIDTH - 56;
    public static final int GAME_HEIGHT = HEIGHT - 77;
    public static final int TICKRATE = 100;
    public static final int FRAMERATE = 120;
    public static final String title = "Gaem";
    public static final Color playerColor = new Color(69, 255, 184);
    public static final Color normalWallColor = new Color(0, 0, 0);
    public static final Color destroyableWallColor = Color.pink;
    public static final Color fieldColor = new Color(255, 127, 160);
    public static final Color enemycolor = new Color(255, 0, 0);
    public static double AI_DELAY = 0.5;
    public static int AI_SPEED = 120;
    private static View view;
    private static Controller controller;
    private static Model model;
    private static boolean paused = true;
    private static boolean renderPaused = false;
    private static boolean gameEnd = false;

    public static boolean isRenderPaused() {
        return renderPaused;
    }

    public static void setRenderPaused(boolean renderPaused) {
        Game.renderPaused = renderPaused;
    }

    public static boolean isGameEnd() {
        return gameEnd;
    }

    public static void setGameEnd(boolean gameEnd) {
        if (gameEnd) {
            Game.setPause(true);
            Game.gameEnd = true;
        } else {
            Game.setPause(true);
            Game.setRenderPaused(true);
            model.reload();
            Game.gameEnd = false;
            Game.setPause(true);
            Game.setRenderPaused(false);
        }
    }

    public static boolean isPaused() {
        return paused;
    }

    public static void setPause(boolean running) {
        if (!Game.isGameEnd())
            Game.paused = running;
    }

    public static void main(String[] args) {
        model = new Model();
        controller = new Controller(model);
        view = new View(model, controller.getKeyboardInput());
    }

    public void restart() {

    }


}
