package com.krynju;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int TICKRATE = 100;
    private static final int FRAMERATE = 60;
    private static final String title = "Gaem";
    private Thread thread;
    private boolean running = false;
    private Handler handler;


    private Game() {
        handler = new Handler();
        new Window(WIDTH, HEIGHT, title, this);

        this.start();


        handler.addObject(new Player(30, 30,100,100));
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

        double lastFrame = System.nanoTime();
        double lastTick = System.nanoTime();
        long timer = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;

        while (running) {
            double now = System.nanoTime();

            if ((now - lastTick) > 1000000000 / TICKRATE) {
                tick((now - lastTick) / 1000000000);
                lastTick = now;
                ticks++;
            }

            if ((now - lastFrame) > 1000000000 / FRAMERATE) {
                render();
                lastFrame = now;
                frames++;
            }

            if ((System.currentTimeMillis() - timer) > 1000) {
                System.out.println("FPS: " + frames);
                System.out.println("ticks: " + ticks);
                timer = System.currentTimeMillis();
                frames = 0;
                ticks = 0;
            }
        }
        stop();
    }

    private void tick(double timeElapsedSeconds) {
        handler.tick(timeElapsedSeconds);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Game();
    }
}
