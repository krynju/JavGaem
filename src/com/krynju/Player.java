package com.krynju;

import java.awt.*;

public class Player extends GameObject {
    public Player(int x, int y) {
        super(x, y,1,1);
        ID = ObjectID.player;
    }

    public void tick(){
        x += xVel;
        y += yVel;

    }
    public void render(Graphics g){
        g.setColor(Color.pink);
        g.fillRect(x,y,10,10);
    }
}
