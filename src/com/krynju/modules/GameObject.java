package com.krynju.modules;

import java.awt.Graphics;

/**
 * Basic gameobject abstract class, every gameobject derives from it
 */
public abstract class GameObject {
    /**
     * X cord on the panel
     */
    protected double x;

    /**
     * Y cord on the panel
     */
    protected double y;

    /**
     * Velocity in the x direction
     */
    protected double xVel;

    /**
     * Velocity in the y direction
     */
    protected double yVel;

    /**
     * X cord on the tilefield
     *
     * @see Field
     */
    protected int tileCordX;

    /**
     * Y cord on the tilefield
     *
     * @see Field
     */
    protected int tileCordY;

    /**
     * Tile the object is standing on reference
     */
    protected Tile assignedTile;

    public GameObject(int x, int y) {
        assignedTile = Field.getTileRef(x, y);
        this.x = assignedTile.getX();
        this.y = assignedTile.getY();
        tileCordX = x;
        tileCordY = y;
    }

    public int getTileCordX() {
        return tileCordX;
    }

    public int getTileCordY() {
        return tileCordY;
    }

    /**
     * Updates the objects parameters according to the given timeElapsedSeconds
     * Used in the controller thread
     *
     * @param timeElapsedSeconds time elapsed in the game
     */
    public abstract void tick(double timeElapsedSeconds);

    /**
     * Paints the object on the panel
     */
    public abstract void render(Graphics g);


}
