package com.krynju;

import com.krynju.modules.Field;
import com.krynju.modules.GameObject;

import java.awt.*;
import java.awt.image.BufferStrategy;

import javax.swing.*;

public class View extends JPanel implements Runnable {
    private static final int FRAMERATE = 60;
    private boolean running = false;

    private Thread thread;
    private Model model;

    public View(Model model, KeyboardInput keyboardInput) {
        this.model = model;

        JFrame frame = new JFrame(Game.title);
        frame.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMaximumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMinimumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);

        this.addKeyListener(keyboardInput);
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

        while (running) {
            double now = System.nanoTime();

            if ((now - lastFrame) > 1000000000 / Game.FRAMERATE) {
                repaint();
                lastFrame = now;
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

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        /*background*/
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        Field.render(g);    //render the field
        for (GameObject obj : model.objectList) {//render the gameobjects
            obj.render(g2d);
        }

        g2d.dispose();

    }


}
