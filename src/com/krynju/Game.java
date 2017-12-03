package com.krynju;

import java.awt.*;

public class Game {
    /*Frame properties*/
    public static final int WIDTH = 696;
    public static final int HEIGHT = 557;
    public static final int FRAMERATE = 120;
    public static final String title = "DANK SOILS BANDICOT";
    /*GameObject colors*/
    public static final Color playerColor = new Color(0, 0, 255);
    public static final Color normalWallColor = new Color(0, 0, 0);
    public static final Color destroyableWallColor = new Color(255,159,99);
    public static final Color fieldColor = new Color(255, 255, 255);
    public static final Color enemyColor = new Color(255, 0, 0);
    public static final Color bombColor  = new Color(255,0,0,125);
    /*AI properties*/
    public static double AI_DELAY = 0.5;
    public static int AI_SPEED = 120;

    private static View view;
    private static Controller controller;
    private static Model model;

    public static void main(String[] args) {
        controller = new Controller();
        model = new Model(controller);
        view = new View(controller);
    }
}
