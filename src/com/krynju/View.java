package com.krynju;

import com.krynju.modules.Field;
import com.krynju.modules.GameObject;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

/**
 * View class creates the whole UI and runs the rendering loop
 */
public class View implements Runnable {
    private final Controller controller;
    private JPanel gamePanel;
    private JButton playPauseButton;
    private Thread thread;

    /**
     * Flag if the thread is running
     */
    private boolean running = false;

    /**
     * The constructor initialises all UI components and then starts a thread with the rendering loop
     */
    View(Controller controller) {
        this.controller = controller;

        /*GAMEPANEL JPanel*/
        gamePanel = new JPanel() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.white);
                g2d.fillRect(0, 0, Field.fieldsSizeX, Field.fieldsSizeY);
                Field.render(g2d);    //render the field
                for (GameObject obj : controller.getObjectList())//render the gameobjects
                    obj.render(g2d);
                if (controller.isGameEnd())
                    displayEndMessage(g2d);
                g2d.dispose();
            }
        };
        gamePanel.addKeyListener(controller.getKeyboardInput());
        gamePanel.setBounds(145, 60, Field.fieldsSizeX, Field.fieldsSizeY);


        /*FRAME JFrame*/
        JFrame frame = new JFrame(Game.title);
        frame.setLocation(0, 0);
        frame.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMaximumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setMinimumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.add(gamePanel);


        /*BANNER*/
        ImageIcon img = new ImageIcon("src/com/krynju/resources/banner.PNG");
        JLabel banner = new JLabel(img);
        banner.setBounds(176, 0, 600, 60);

        frame.add(banner);


        /*PLAY/PAUSE Button*/
        playPauseButton = new JButton();
        playPauseButton.addActionListener(e -> {
            controller.setPause(!controller.isPaused());
            gamePanel.requestFocus();
        });
        playPauseButton.setBounds(10, 60, 125, Field.tileSize);
        frame.add(playPauseButton);


        /*RESET Button*/
        JButton resetButton = new JButton();
        resetButton.addActionListener(e -> {
            controller.setGameEnd(false);     //it's literally a restart
            gamePanel.requestFocus();
        });
        resetButton.setBounds(10, 130, 125, Field.tileSize);
        resetButton.setText("Reset");
        frame.add(resetButton);


        /*DIFFICULTY Label - over the difficulty combo box*/
        JLabel difficultyLabel = new JLabel();
        difficultyLabel.setText("Difficulty setting:");
        difficultyLabel.setBounds(10, 190, 125, 20);
        frame.add(difficultyLabel);


        /*DIFFICULTY ComboBox*/
        String[] abd = {"normal", "hard", "harder", "the hardest"};
        JComboBox<String> comboBox = new JComboBox<>(abd);
        comboBox.setBounds(10, 210, 125, 30);
        comboBox.addActionListener(e -> {
            int s = ((JComboBox) e.getSource()).getSelectedIndex();
            switch (s) {
                case 0: //normie
                    Game.AI_SPEED = 3 * Field.tileSize;
                    Game.AI_DELAY = 0.5;
                    break;
                case 1: //dank
                    Game.AI_SPEED = 6 * Field.tileSize;
                    Game.AI_DELAY = 0.5;
                    break;
                case 2://dankest
                    Game.AI_SPEED = 3 * Field.tileSize;
                    Game.AI_DELAY = 0;
                    break;
                case 3://most dankest
                    Game.AI_SPEED = 6 * Field.tileSize;
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

        /*INSTRUCTIONS*/
        instructions(frame);


        /*FRAME VISIBILITY ON*/
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);

        /*THREAD START*/
        this.start();
    }

    /**
     * Creates a thread and starts the rendering loop
     */
    private synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
        gamePanel.requestFocus();
    }

    /**
     * Thread stop
     */
    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rendering loop
     */
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
                render();
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

    /**
     * Updates the playPauseButton's text according to the gamestate
     */
    private void uiTextUpdate() {
        /*PLAY/PAUSE Button text update*/
        if (controller.isPaused())
            playPauseButton.setText("Play");
        else
            playPauseButton.setText("Pause");
    }

    /**
     * Displays the appropriate message when the game ends
     */
    private void displayEndMessage(Graphics2D g2d) {
        /*Displaying grey bar in the middle of the screen*/
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 4 * Field.tileSize, Field.fieldsSizeX, 3 * Field.tileSize);
        /*Displaying the appropriate message*/
        g2d.setFont(new Font("Verdana", Font.PLAIN, 125));
        g2d.setColor(new Color(200, 48, 46));
        switch (controller.getEndingType()) {
            case lose:
                g2d.drawString("YOU DIED", (int) (Field.fieldsSizeX * 0.02), (int) (6.3 * Field.tileSize));
                break;
            case win:
                g2d.drawString("YOU WIN", (int) (Field.fieldsSizeX * 0.065), (int) (6.3 * Field.tileSize));
                break;
            case draw:
                g2d.drawString("DRAW", (int) (Field.fieldsSizeX * 0.2), (int) (6.3 * Field.tileSize));
                break;
        }
        /*Displaying restart information under the message*/
        g2d.setFont(new Font("Verdana", Font.PLAIN, 20));
        g2d.drawString("PRESS R TO RESTART", (int) (Field.fieldsSizeX * 0.33), (int) (Field.fieldsSizeY * 0.62));
    }

    /**Synchronized repaint wrapper */
    synchronized private void render(){
        gamePanel.repaint();
    }

    /**
     * Adding instructions text to the frame
     */
    private void instructions(JFrame frame) {
        Font textFont = new Font("Verdana", Font.PLAIN, 12);
        Font blockFont = new Font("TimesRoman", Font.PLAIN, 20);
        int y = 270;
        int height = 20;


        JLabel INSTRUCTIONS = new JLabel();
        INSTRUCTIONS.setFont(new Font("Verdana", Font.BOLD, 12));
        INSTRUCTIONS.setBounds(10, y - height, 125, height);
        INSTRUCTIONS.setText("Instructions:");
        frame.add(INSTRUCTIONS);

        JLabel WASD = new JLabel();
        WASD.setFont(textFont);
        WASD.setBounds(10, y, 125, height);
        WASD.setText("W,A,S,D - move");
        frame.add(WASD);

        JLabel BOMB = new JLabel();
        BOMB.setFont(textFont);
        BOMB.setBounds(10, y + height, 125, height);
        BOMB.setText("SPACE - set bomb");
        frame.add(BOMB);

        JLabel PAUSE = new JLabel();
        PAUSE.setFont(textFont);
        PAUSE.setBounds(10, y + 2 * height, 125, height);
        PAUSE.setText("P - pause");
        frame.add(PAUSE);

        JLabel PLAYER_SQUARE = new JLabel();
        PLAYER_SQUARE.setFont(blockFont);
        PLAYER_SQUARE.setForeground(Game.playerColor);
        PLAYER_SQUARE.setBounds(10, y + 3 * height, 125, height);
        PLAYER_SQUARE.setText("\u25A0");
        frame.add(PLAYER_SQUARE);

        JLabel PLAYER = new JLabel();
        PLAYER.setFont(textFont);
        PLAYER.setBounds(30, y + 3 * height, 125, height);
        PLAYER.setText("- player");
        frame.add(PLAYER);

        JLabel ENEMY_SQUARE = new JLabel();
        ENEMY_SQUARE.setFont(blockFont);
        ENEMY_SQUARE.setForeground(Game.enemyColor);
        ENEMY_SQUARE.setBounds(10, y + 4 * height, 125, height);
        ENEMY_SQUARE.setText("\u25A0");
        frame.add(ENEMY_SQUARE);

        JLabel ENEMY = new JLabel();
        ENEMY.setFont(textFont);
        ENEMY.setBounds(30, y + 4 * height, 125, height);
        ENEMY.setText("- enemy");
        frame.add(ENEMY);

        JLabel NORM_WALL_SQUARE = new JLabel();
        NORM_WALL_SQUARE.setFont(blockFont);
        NORM_WALL_SQUARE.setForeground(Game.normalWallColor);
        NORM_WALL_SQUARE.setBounds(10, y + 5 * height, 125, height);
        NORM_WALL_SQUARE.setText("\u25A0");
        frame.add(NORM_WALL_SQUARE);

        JLabel NORM_WALL = new JLabel();
        NORM_WALL.setFont(textFont);
        NORM_WALL.setBounds(30, y + 5 * height, 125, height);
        NORM_WALL.setText("- normal wall");
        frame.add(NORM_WALL);

        JLabel DESTR_WALL_SQUARE = new JLabel();
        DESTR_WALL_SQUARE.setFont(blockFont);
        DESTR_WALL_SQUARE.setForeground(Game.destroyableWallColor);
        DESTR_WALL_SQUARE.setBounds(10, y + 6 * height, 125, height);
        DESTR_WALL_SQUARE.setText("\u25A0");
        frame.add(DESTR_WALL_SQUARE);

        JLabel DESTR_WALL = new JLabel();
        DESTR_WALL.setFont(textFont);
        DESTR_WALL.setBounds(30, y + 6 * height, 125, height);
        DESTR_WALL.setText("- destroyable wall");
        frame.add(DESTR_WALL);
    }
}
