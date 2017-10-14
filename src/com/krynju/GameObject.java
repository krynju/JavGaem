package com.krynju;

import java.awt.Graphics;

public abstract class GameObject {
    protected ObjectID ID;
    protected int x;
    protected int y;
    protected double xVel;
    protected double yVel;


    public GameObject(int x, int y, double xVel, double yVel) {
        this.x = x;
        this.y = y;
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
