package com.krynju.modules;

import com.krynju.enums.ObjectID;

import java.awt.Graphics;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected double xVel;
    protected double yVel;
    protected int tileCordX;
    protected int tileCordY;
    protected Tile assignedTile;

    public GameObject(int x, int y, double xVel, double yVel) {
        assignedTile = Field.getTileRef(x,y);
        this.x = assignedTile.getX();
        this.y = assignedTile.getY();
        this.xVel = xVel;
        this.yVel = yVel;
        tileCordX = x;
        tileCordY = y;
    }

    public int getTileCordX() {
        return tileCordX;
    }

    public int getTileCordY() {
        return tileCordY;
    }

    public abstract void tick(double timeElapsedSeconds);

    public abstract void render(Graphics g);


}
