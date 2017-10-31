package com.krynju.modules;

import com.krynju.Tile;

import java.awt.*;

public class Bomb extends GameObject{

    public boolean isSet = false;

    public Bomb(int x, int y, double xVel, double yVel) {
        super(x, y, xVel, yVel);
    }

    @Override
    public void tick(double timeElapsedSeconds) {

    }

    @Override
    public void render(Graphics g) {
        if(!isSet)
            return;
        else
            g.setColor(Color.black);
            g.drawOval((int)x,(int)y,40,40);
    }

    public void setAt(int x, int y){
        isSet = true;
        this.x = x;
        this.y = y;

    }
}
