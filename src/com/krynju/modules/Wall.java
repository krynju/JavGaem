package com.krynju.modules;

import java.awt.*;

public abstract class Wall extends GameObject{
    protected boolean destroyable;

    public Wall(int x, int y, double xVel, double yVel) {
        super(x, y, xVel, yVel);
        assignedTile.setWallOnTile(true);
        assignedTile.setWall(this);
    }

    @Override
    public void tick(double timeElapsedSeconds) {}

    @Override
    public void render(Graphics g) {
    }

    public abstract void destroy();

    public boolean isDestroyable() {
        return destroyable;
    }
}
