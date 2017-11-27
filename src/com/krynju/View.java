package com.krynju;

import com.krynju.modules.Field;
import com.krynju.modules.GameObject;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class View extends JPanel implements Runnable {
    private JButton playPauseButton;
    private JLabel restartLabel;

    private boolean running = false;

    private Thread thread;
    private Model model;

    public View(Model model, KeyboardInput keyboardInput) {
        View gamePanel = this;
        this.model = model;
        this.addKeyListener(keyboardInput);
        this.setBounds(140, 60, 520, 440);

        JFrame frame = new JFrame(Game.title);
        frame.setLocation(300, 100);
        frame.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMaximumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMinimumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.add(this);


        /*PLAY/PAUSE Button*/
        playPauseButton = new JButton();
        playPauseButton.addActionListener(e -> {
            Game.setPause(!Game.isPaused());
            gamePanel.requestFocus();
        });
        playPauseButton.setBounds(10, 60, 120, 40);
        frame.add(playPauseButton);


        /*RESET Button*/
        JButton resetButton = new JButton();
        resetButton.addActionListener(e -> {
            Game.setGameEnd(false);     //it's literally a restart
            gamePanel.requestFocus();
        });
        resetButton.setBounds(10, 110, 120, 40);
        resetButton.setText("Reset");
        frame.add(resetButton);


        /*DIFFICULTY Label - over the difficulty combo box*/
        JLabel difficultyLabel = new JLabel();
        difficultyLabel.setText("Difficulty setting:");
        difficultyLabel.setBounds(10, 160, 120, 20);
        frame.add(difficultyLabel);


        /*DIFFICULTY ComboBox*/
        String[] abd = {"normie", "dank", "dankest", "most dankest"};
        JComboBox<String> comboBox = new JComboBox<>(abd);
        comboBox.setBounds(10, 180, 120, 30);
        comboBox.addActionListener(e -> {
            int s = ((JComboBox) e.getSource()).getSelectedIndex();
            switch (s) {
                case 0: //normie
                    Game.AI_SPEED = 120;
                    Game.AI_DELAY = 0.5;
                    break;
                case 1: //dank
                    Game.AI_SPEED = 240;
                    Game.AI_DELAY = 0.5;
                    break;
                case 2://dankest
                    Game.AI_SPEED = 120;
                    Game.AI_DELAY = 0;
                    break;
                case 3://most dankest
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
        frame.add(comboBox);


        /*PRESS R TO RESTART Label*/
        restartLabel = new JLabel();
        restartLabel.setBounds(10, 230, 120, 20);
        frame.add(restartLabel);


        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);


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

    @Override
    public void run() {
        /*Initial focus request by the gamePanel*/
        this.requestFocus();
        /*Initialising the time variable*/
        double lastFrame = System.nanoTime();
        /*Render loop start*/
        while (running) {
            /*Updating the UI text*/
            uiTextUpdate();
            /*Pausing the rendering functionality*/
            if (Game.isRenderPaused()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            /*Getting the time*/
            double now = System.nanoTime();
            /*Render according to the set frame rate*/
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

    private void uiTextUpdate() {
        /*PRESS R TO RESTART Label text update*/
        if (Game.isGameEnd())
            restartLabel.setText("PRESS R TO RESTART");
        else
            restartLabel.setText("");

        /*PLAY/PAUSE Button text update*/
        if (Game.isPaused())
            playPauseButton.setText("Play");
        else
            playPauseButton.setText("Pause");
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
