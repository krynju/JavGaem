package com.krynju.modules;

import com.krynju.Game;
import com.krynju.enums.Direction;
import com.krynju.enums.ObjectID;

import java.awt.Color;
import java.awt.Graphics;

import static java.lang.Math.abs;

public class Player extends GameObject {
    protected ObjectID ID;
    private int SPEED = 120;   //PLAYER's speed
    private boolean atDestination = true;   //Flag if not moving and standing on the rightInQueue spot
    public int destinationX;               //PLAYER's x destination cord
    public int destinationY;               //PLAYER's y destination cord
    public Bomb bomb;

    private Direction movementDirection = Direction.none;   //Movements direction
    private Color color;

    public Player(ObjectID id, int x, int y, Color color, Bomb bomb) {
        super(x, y, 0, 0);
        destinationX = assignedTile.getX();
        destinationY = assignedTile.getY();
        ID = id;
        this.bomb = bomb;
        this.color = color;
        if(this.ID == ObjectID.player)
            assignedTile.setPlayerOnTile(true);
        if(this.ID == ObjectID.enemy)
            assignedTile.setEnemyOnTile(true);
    }

    public boolean isAtDestination() {
        return atDestination;
    }

    private void goTo(Direction direction, int destination) {
        /*for the dankest difficulty, speed increase*/
        if (ID == ObjectID.enemy) {
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

        if(this.ID == ObjectID.player) {
            assignedTile.setPlayerOnTile(false);    //old tile
            assignedTile = Field.getTileRef(tileCordX, tileCordY);    //get new tile
            assignedTile.setPlayerOnTile(true);     //new tile
        }
        if(this.ID == ObjectID.enemy) {
            assignedTile.setEnemyOnTile(false);    //old tile
            assignedTile = Field.getTileRef(tileCordX, tileCordY);    //get new tile
            assignedTile.setEnemyOnTile(true);     //new tile
        }

    }

    private void arrivedAtDestination() { // to mozna podobno lambda
        /*Stopping the object at destination*/
        y = destinationY;
        x = destinationX;
        xVel = 0;
        yVel = 0;
        atDestination = true;
    }

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
        g.fillRect((int) x, (int) y, 40, 40);
        /*image input*/
//        BufferedImage img = null;
//        try {
//            img = ImageIO.read(new File("src/com/krynju/Untitled.png")); // eventually C:\\ImageTest\\pic2.jpg
//        }
//        catch (IOException e) { e.printStackTrace(); }
//        g.drawImage(img,(int)x,(int)y,null);
    }

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
