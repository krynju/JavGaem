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

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getxVel() {
        return xVel;
    }

    public void setxVel(double xVel) {
        this.xVel = xVel;
    }

    public double getyVel() {
        return yVel;
    }

    public void setyVel(double yVel) {
        this.yVel = yVel;
    }


}
