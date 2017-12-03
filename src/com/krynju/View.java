package com.krynju;

import com.krynju.modules.Field;
import com.krynju.modules.GameObject;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class View implements Runnable {
    private final Controller controller;
    private JPanel gamePanel;
    private JButton playPauseButton;
    private JTextArea instructions;

    private boolean running = false;

    private Thread thread;

    View(Controller controller) {
        this.controller = controller;
        //controller.addView(this);

        /*GAMEPANEL JPanel*/
        gamePanel = new JPanel(){
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.white);
                g2d.fillRect(0, 0, Field.fieldsSizeX, Field.fieldsSizeY);
                Field.render(g2d);    //render the field
                for (GameObject obj : controller.getObjectList())//render the gameobjects
                    obj.render(g2d);
                if(controller.isGameEnd())
                    displayEndMessage(g2d);
                g2d.dispose();
        }};
        gamePanel.addKeyListener(controller.getKeyboardInput());
        gamePanel.setBounds(140, 60, Field.fieldsSizeX, Field.fieldsSizeY);


        /*FRAME JFrame*/
        JFrame frame = new JFrame(Game.title);
        frame.setLocation(300, 100);
        frame.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMaximumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMinimumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.add(gamePanel);


        /*PLAY/PAUSE Button*/
        playPauseButton = new JButton();
        playPauseButton.addActionListener(e -> {
            controller.setPause(!controller.isPaused());
            gamePanel.requestFocus();
        });
        playPauseButton.setBounds(10, 60, 120, 40);
        frame.add(playPauseButton);


        /*RESET Button*/
        JButton resetButton = new JButton();
        resetButton.addActionListener(e -> {
            controller.setGameEnd(false);     //it's literally a restart
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


        /*INSTRUCTIONS TextArea*/
        instructions = new JTextArea();
        instructions.setBounds(10, 230, 120, 65);
        instructions.setEditable(false);
        instructions.setFocusable(false);
        instructions.setText("Instructions:\nW,A,S,D - move\nSPACE - set bomb\nP - pause");
        frame.add(instructions);


        /*FRAME VISIBILITY ON*/
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);

        this.start();
    }

    private synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    private synchronized void stop() {
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
        gamePanel.requestFocus();
        /*Initialising the time variable*/
        double lastFrame = System.nanoTime();
        /*Render loop start*/
        while (running) {
            /*Updating the UI text*/
            uiTextUpdate();
            /*Pausing the rendering functionality*/
            if (controller.isRenderPaused()) {
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
                gamePanel.repaint();
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
        /*PLAY/PAUSE Button text update*/
        if (controller.isPaused())
            playPauseButton.setText("Play");
        else
            playPauseButton.setText("Pause");
    }

    private void displayEndMessage(Graphics2D g2d) {
        g2d.setColor(new Color(0,0,0,200));
        g2d.fillRect(0,170,Field.fieldsSizeX,120);
        g2d.setFont(new Font("Verdana",Font.PLAIN,100));
        g2d.setColor(new Color(200,48,46));
        switch(controller.getEndingType()){
            case lose:
                g2d.drawString("YOU DIED",(int)(Field.fieldsSizeX * 0.013),(int)(Field.fieldsSizeY * 0.58));
                break;
            case win:
                g2d.drawString("YOU WIN", (int)(Field.fieldsSizeX * 0.065),(int)(Field.fieldsSizeY * 0.58));
                break;
            case draw:
                g2d.drawString("DRAW", (int)(Field.fieldsSizeX * 0.2), (int)(Field.fieldsSizeY * 0.58));
                break;
        }
        g2d.setFont(new Font("Verdana",Font.PLAIN,20));
        g2d.drawString("PRESS R TO RESTART",(int)(Field.fieldsSizeX * 0.30),(int)(Field.fieldsSizeY * 0.64));
    }
}
