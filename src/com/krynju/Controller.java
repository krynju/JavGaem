package com.krynju;

import java.awt.*;

public class Controller extends Canvas implements Runnable {
    public static final int WIDTH = 696;
    public static final int HEIGHT = 557;
    public static final int GAME_WIDTH = WIDTH - 56;
    public static final int GAME_HEIGHT = HEIGHT - 77;
    private static final int TICKRATE = 100;
    private static final int FRAMERATE = 60;
    private static final String title = "Gaem";
    private boolean running = false;

    private Thread thread;
    private Model model;

    public Controller(Model model) {
        this.model = model;
        this.model.initialiseGameObjects();
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
        model.tick(timeElapsedSeconds);
    }


}
