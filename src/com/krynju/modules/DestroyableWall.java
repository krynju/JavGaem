package com.krynju.modules;

import com.krynju.Game;

import java.awt.*;
/**Destroyable wall*/
public class DestroyableWall extends Wall {
    private boolean destroyed = false;

    public DestroyableWall(int x, int y) {
        super(x, y);
        destroyable = true;
        assignedTile.destroyable=true;
    }

    @Override
    public void tick(double timeElapsedSeconds) {}

    @Override
    public void render(Graphics g) {
        if(!destroyed) {
            g.setColor(Game.destroyableWallColor);
            g.fillRect((int) x, (int) y, Field.tileSize, Field.tileSize);
        }
    }


    public void destroy() {
        assignedTile.setWallOnTile(false);
        destroyed = true;
        assignedTile.destroyable=false;
    }
}
