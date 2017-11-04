package com.krynju;

public class Game {
    public static final int WIDTH = 696;
    public static final int HEIGHT = 557;
    public static final int GAME_WIDTH = WIDTH - 56;
    public static final int GAME_HEIGHT = HEIGHT - 77;
    private static final int TICKRATE = 100;
    private static final int FRAMERATE = 60;
    private static final String title = "Gaem";


    private static View view;
    private static Controller controller;
    private static Model model;


    public static void main(String[] args) {
        model = new Model();
        controller = new Controller(model);
        view = new View(model,controller.getKeyboardInput());
    }







}
