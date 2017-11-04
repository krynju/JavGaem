package com.krynju;

import java.awt.*;
import java.awt.image.BufferStrategy;

import javax.swing.*;

public class View extends Canvas implements Runnable{
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

    public View(Model model) {
        this.model = model;

        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        frame.setMaximumSize(new Dimension(WIDTH,HEIGHT));
        frame.setMinimumSize(new Dimension(WIDTH,HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);

        this.addKeyListener(new KeyboardInput());
        this.start();
    }

    synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }


    @Override
    public void run() {
        this.requestFocus();
        double lastFrame = System.nanoTime();
        double lastTick = System.nanoTime();
        long timer = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;

        while (running) {
            double now = System.nanoTime();

            if ((now - lastFrame) > 1000000000 / FRAMERATE) {
                render();
                lastFrame = now;
                frames++;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();

    }
    synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        model.render(g);

        g.dispose();
        bs.show();
    }


}
