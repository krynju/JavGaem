package com.krynju.modules;

import com.krynju.Game;
import com.krynju.enums.Direction;
import com.krynju.enums.PlayerID;

import java.awt.*;

import static java.lang.Math.abs;

public class Player extends GameObject {
    /**
     * Reference to a bomb that belongs to the player
     */
    public Bomb bomb;
    /**
     * Players ID, either player or enemy
     *
     * @see PlayerID
     */
    private PlayerID ID;
    /**
     * Basic player speed is 3 tiles/second
     */
    private int SPEED = 3 * Field.tileSize;
    /**
     * Flag that the object is at the right destination
     */
    private boolean atDestination = true;
    /**
     * PLAYER's x destination cord
     */
    private int destinationX;
    /**
     * PLAYER's y destination cord
     */
    private int destinationY;
    /**
     * Variable containing information about in which way the movement is happening
     */
    private Direction movementDirection = Direction.none;
    /**
     * Players color reference, used because the enemy and the player have different colors
     * initialised at construction time
     */
    private Color color;

    /**
     * Main player constructor provides all necessary information for the player
     */
    public Player(PlayerID id, int x, int y, Color color, Bomb bomb) {
        super(x, y);
        destinationX = assignedTile.getX();
        destinationY = assignedTile.getY();
        ID = id;
        this.bomb = bomb;
        this.color = color;
        if (this.ID == PlayerID.player)
            assignedTile.setPlayerOnTile(true);
        if(this.ID == PlayerID.enemy)
            assignedTile.setEnemyOnTile(true);
    }


    public boolean isAtDestination() {
        return atDestination;
    }

    /**Method making the player move
     * invoked AFTER all the necessary conditions have been met
     * @param direction direction in which the movement happens
     * @param destination cords to which the player has to move*/
    private void goTo(Direction direction, int destination) {
        /*for the dankest difficulty, speed increase*/
        if (ID == PlayerID.enemy) {
            SPEED = Game.AI_SPEED;
        }
        atDestination = false;
        movementDirection = direction;

        switch (direction) {
            case up:
                xVel = 0;
                yVel = -SPEED;
                destinationY = destination;
                tileCordY--;
                break;
            case down:
                xVel = 0;
                yVel = SPEED;
                destinationY = destination;
                tileCordY++;
                break;
            case left:
                xVel = -SPEED;
                yVel = 0;
                destinationX = destination;
                tileCordX--;
                break;
            case right:
                xVel = SPEED;
                yVel = 0;
                destinationX = destination;
                tileCordX++;
                break;
            case none:
                return;
        }

        if (this.ID == PlayerID.player) {
            assignedTile.setPlayerOnTile(false);    //old tile
            assignedTile = Field.getTileRef(tileCordX, tileCordY);    //get new tile
            assignedTile.setPlayerOnTile(true);     //new tile
        }
        if (this.ID == PlayerID.enemy) {
            assignedTile.setEnemyOnTile(false);    //old tile
            assignedTile = Field.getTileRef(tileCordX, tileCordY);    //get new tile
            assignedTile.setEnemyOnTile(true);     //new tile
        }

    }

    /**
     * Metohod used for stopping the players movement
     */
    private void arrivedAtDestination() {
        /*Stopping the object at destination*/
        y = destinationY;
        x = destinationX;
        xVel = 0;
        yVel = 0;
        atDestination = true;
    }

    /**Method used for fetching th destination from the Field static class
     * The Field.getDestination checks if movemement in the provided direction is possible
     * it returns the destination cords if possible and throws an exception if not possible
     * Then invokes goTo with appropriate parameters
     * @param direction direction in which you want the player to move
     * @throws UnableToMove when the movement is not possible to do
     * @see Player#goTo(Direction, int) */
    public void move(Direction direction) throws UnableToMove {
        int destination = Field.getDestination(tileCordX, tileCordY, direction);
        goTo(direction, destination);
    }

    public void tick(double timeElapsedSeconds) {
        /*Calculating new position*/
        x += xVel * timeElapsedSeconds;
        y += yVel * timeElapsedSeconds;

        /*Stop check - if at or further than destination do a stop */
        if (!atDestination) {
            switch (movementDirection) {
                case up:
                    if (y <= destinationY - 0.5) arrivedAtDestination();
                    break;
                case down:
                    if (y >= destinationY - 0.5) arrivedAtDestination();
                    break;
                case left:
                    if (x <= destinationX - 0.5) arrivedAtDestination();
                    break;
                case right:
                    if (x >= destinationX - 0.5) arrivedAtDestination();
                    break;
                case none:
                    break;
            }
        }


    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, Field.tileSize, Field.tileSize);
    }

    /**Method that makes the player place the bomb if the given conditions are met*/
    public void placeBomb() {
        if (bomb.isBombSet())
            return;
        else if (atDestination)
            bomb.setAt(tileCordX, tileCordY);
        else if ((abs(x - destinationX) < 30) && (abs(y - destinationY) < 30))
            bomb.setAt(tileCordX, tileCordY);
        else {
            switch (movementDirection) {
                case up:
                    bomb.setAt(tileCordX, tileCordY + 1);
                    break;
                case down:
                    bomb.setAt(tileCordX, tileCordY - 1);
                    break;
                case left:
                    bomb.setAt(tileCordX + 1, tileCordY);
                    break;
                case right:
                    bomb.setAt(tileCordX - 1, tileCordY);
                    break;
            }
        }
    }
}
