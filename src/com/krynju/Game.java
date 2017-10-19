package com.krynju;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    public static final int WIDTH = 696;
    public static final int HEIGHT = 557;
    public static final int GAME_WIDTH = WIDTH-56;
    public static final int GAME_HEIGHT = HEIGHT-77;
    private static final int TICKRATE = 200;
    private static final int FRAMERATE = 60;
    private static final String title = "Gaem";
    private boolean running = false;

    private Thread thread;
    private Handler handler;

    private Game() {
        handler = new Handler();
        this.addKeyListener(new KeyboardInput(handler));
        new Window(WIDTH, HEIGHT, title, this);
        this.start();


        /*pewnie po prostu dam dodawanie wszystkich obiektów w konstruktorze handlera
        * albo zrobię jakieś .addGameObjects*/
        handler.addPlayer(new Player(100, 100,0,0));
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
            createBufferStrategy(3);
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
