package com.krynju;

import java.awt.*;

public class Player extends GameObject {
    public Player(double x, double y) {
        super(x, y,100,100);
        ID = ObjectID.player;
    }

    public void tick(double timeElapsedSeconds){

        x += xVel * timeElapsedSeconds;
        y += yVel * timeElapsedSeconds;

    }

    public void render(Graphics g){
        g.setColor(Color.pink);
        g.fillRect((int)x,(int)y,70,10);
    }
}
