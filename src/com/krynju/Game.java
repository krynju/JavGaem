package com.krynju;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final String title = "Gaem";

    private Thread thread;
    private boolean running = false;

    private Handler handler;


    private Game() {
        new Window(WIDTH, HEIGHT, title, this);
        handler = new Handler();
        handler.addObject(new Player(30,30));
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
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;

        while (running) {
            long now = System.nanoTime();

/*ticks as of now synchronised with frames*/
            if ((now - lastTime) > 1000000000 / 60) {
                tick();
                ticks++;

                render();
                frames++;

                lastTime = now;
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

    private void tick() {
        handler.tick();
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
