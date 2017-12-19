package com.krynju.modules;

import com.krynju.Game;

import java.awt.*;

/**
 * Non-destroyable wall
 */
public class NormalWall extends Wall {
    public NormalWall(int x, int y) {
        super(x, y);
        destroyable = false;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void tick(double timeElapsedSeconds) {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Game.normalWallColor);
        g.fillRect((int) x, (int) y, Field.tileSize, Field.tileSize);
    }
}
