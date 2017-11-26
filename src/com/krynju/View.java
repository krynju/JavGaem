package com.krynju;

import com.krynju.enums.Direction;
import com.krynju.modules.Field;
import com.krynju.modules.GameObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class View extends JPanel implements Runnable {
    private static final int FRAMERATE = 60;
    private boolean running = false;

    private Thread thread;
    private Model model;

    public View(Model model, KeyboardInput keyboardInput) {
        this.model = model;

        JFrame frame = new JFrame(Game.title);
        frame.setLocation(300, 100);
        frame.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMaximumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMinimumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.add(this);
        this.setBounds(140, 60, 520, 440);
        View gamePanel = this;

        /*pause button*/
        JButton button = new JButton();
        button.addActionListener(e -> {
            //System.out.println("gay");
            Game.setPause(!Game.isPaused());
            if(!Game.isPaused())
                button.setText("Pause");
            else
                button.setText("Play");
            gamePanel.requestFocus();
        });
        frame.add(button);
        button.setBounds(10, 60, 120, 40);
        button.setText("Play");


        /*reset button button*/
        JButton button2 = new JButton();
        button2.addActionListener(e -> {
            //System.out.println("gayers");
            Game.setPause(true);
            model.reload();
            Game.setPause(false);
            gamePanel.requestFocus();
        });
        frame.add(button2);
        button2.setBounds(10, 110, 120, 40);
        button2.setText("Reset");

        /*jlabel for difficulty*/
        JLabel label = new JLabel();
        label.setText("Difficulty setting:");
        label.setBounds(10,160,120,20);
        frame.add(label);

        /*combobox difficulty*/
        String[] abd = {"normie", "dank","dankest","most dankest"};
        JComboBox comboBox = new JComboBox(abd);
        frame.add(comboBox);
        comboBox.setBounds(10, 180, 120, 30);
        comboBox.addActionListener(e -> {
            int s = ((JComboBox)e.getSource()).getSelectedIndex();
            switch (s) {
                case 0: //normal
                    Game.AI_SPEED = 120;
                    Game.AI_DELAY = 0.5;
                    break;
                case 1: //dank420
                    Game.AI_SPEED = 240;
                    Game.AI_DELAY = 0.5;
                    break;
                case 2:
                    Game.AI_SPEED = 120;
                    Game.AI_DELAY = 0;
                    break;
                case 3:
                    Game.AI_SPEED = 240;
                    Game.AI_DELAY = 0;
                    break;
            }
            gamePanel.requestFocus();
        });

        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                gamePanel.requestFocus();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

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
        Graphics2D g2d = (Graphics2D) g;
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
