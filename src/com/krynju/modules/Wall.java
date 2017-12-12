package com.krynju.modules;

import java.awt.*;
/**Abstract wall class contains basic wall parameters*/
public abstract class Wall extends GameObject{
    /**Flag that says if the wall is destroyable*/
    protected boolean destroyable;

    public Wall(int x, int y) {
        super(x, y);
        assignedTile.setWallOnTile(true);
        assignedTile.setWall(this);
    }

    @Override
    public void tick(double timeElapsedSeconds) {}

    @Override
    public void render(Graphics g) {}

    public abstract void destroy();
}
