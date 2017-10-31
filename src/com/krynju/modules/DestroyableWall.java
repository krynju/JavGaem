package com.krynju.modules;

import java.awt.Color;
import java.awt.Graphics;

public class DestroyableWall extends GameObject {


    public DestroyableWall(int x, int y) {
        super(x, y, 0, 0);
        assignedTile.setBlocked(true);
    }

    @Override
    public void tick(double timeElapsedSeconds) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect((int) x, (int) y, 40, 40);

    }
}
