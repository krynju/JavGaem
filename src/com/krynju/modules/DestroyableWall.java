package com.krynju.modules;

import com.krynju.Modules;

import java.awt.Color;
import java.awt.Graphics;

public class DestroyableWall extends Wall {
    private boolean destroyed = false;

    public DestroyableWall(int x, int y) {
        super(x, y, 0, 0);
        destroyable = true;
    }

    @Override
    public void tick(double timeElapsedSeconds) {}

    @Override
    public void render(Graphics g) {
        if(!destroyed) {
            g.setColor(Color.orange);
            g.fillRect((int) x, (int) y, 40, 40);
        }
    }


    public void destroy() {
        assignedTile.setWallOnTile(false);
        destroyed = true;
    }
}
