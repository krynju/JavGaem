package com.krynju;

import java.awt.*;

public class Player extends GameObject {
    public Player(double x, double y,double xVel,double yVel) {
        super(x, y,xVel,yVel);
        ID = ObjectID.player;
    }

    public void tick(double timeElapsedSeconds){

        x += xVel * timeElapsedSeconds;
        y += yVel * timeElapsedSeconds;

    }

    public void render(Graphics g){
        g.setColor(Color.pink);
        g.fillOval((int)x,(int)y,100,100);
    }
}
