package com.krynju;

import java.awt.Graphics;

public abstract class GameObject {
    protected ObjectID ID;
    protected double x;
    protected double y;
    protected double xVel;
    protected double yVel;

    public GameObject(double x, double y, double xVel, double yVel) {
        this.x = x;
        this.y = y;
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public abstract void tick(double timeElapsedSeconds);
    public abstract void render(Graphics g);

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
