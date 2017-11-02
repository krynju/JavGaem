package com.krynju.modules;

import com.krynju.Field;

import java.awt.*;

public class Bomb extends GameObject {
    public static final int tickingTime = 2;
    public boolean bombSet = false;
    public boolean bombTicking = false;

    public double timeElapsed = 0;


    public Bomb(int x, int y, double xVel, double yVel) {
        super(x, y, xVel, yVel);
    }

    @Override
    public void tick(double timeElapsedSeconds) {
        if (bombTicking) {
            timeElapsed += timeElapsedSeconds;
            if (timeElapsed >= tickingTime)
                boom();
        }
    }

    @Override
    public void render(Graphics g) {
        if (!bombSet)
            return;
        else
            g.setColor(Color.black);
        g.drawOval((int) x, (int) y, 40, 40);
    }

    public void setAt(int x, int y) {
        assignedTile.setBombed(false);          //unbomb the old tile
        assignedTile = Field.getTileRef(x, y);  //get new tileref
        assignedTile.setBombed(true);           //bomb the new tile

        this.x = assignedTile.getX();           //get new x/y positions
        this.y = assignedTile.getY();

        bombSet = true;
        bombTicking = true;
    }

    public void boom(){
        bombTicking = false;
        bombSet = false;
        assignedTile.setBombed(false);
        timeElapsed = 0;

    }
}
