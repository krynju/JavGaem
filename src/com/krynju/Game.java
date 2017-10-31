package com.krynju;

import com.krynju.modules.DestroyableWall;
import com.krynju.modules.Player;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    public static final int WIDTH = 696;
    public static final int HEIGHT = 557;
    public static final int GAME_WIDTH = WIDTH - 56;
    public static final int GAME_HEIGHT = HEIGHT - 77;
    private static final int TICKRATE = 100;
    private static final int FRAMERATE = 60;
    private static final String title = "Gaem";
    private boolean running = false;

    private Thread thread;
    private Modules modules;

    private Game() {
        modules = new Modules();
        this.addKeyListener(new KeyboardInput());
        new Window(WIDTH, HEIGHT, title, this);
        this.start();


        /*pewnie po prostu dam dodawanie wszystkich obiektów w konstruktorze handlera
        * albo zrobię jakieś .addGameObjects*/
        modules.addPlayer(new Player(0, 0, 0, 0));
        modules.addObject(new DestroyableWall(1, 1));
        modules.addObject(new DestroyableWall(2, 2));
    }

    public static void main(String[] args) {
        new Game();
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

            //if ((now - lastTick) > 1000000000 / TICKRATE) {
            tick((now - lastTick) / 1000000000);
            lastTick = now;
            ticks++;
            //}

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
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    private void tick(double timeElapsedSeconds) {
        modules.tick(timeElapsedSeconds);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        /*background*/
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        modules.render(g);

        g.dispose();
        bs.show();
    }
}
