package com.krynju.modules;

import com.krynju.Direction;
import com.krynju.Field;
import com.krynju.KeyboardInput;
import com.krynju.Modules;

import java.awt.Color;
import java.awt.Graphics;

import static java.lang.Math.abs;

public class Player extends GameObject {

    private static final int SPEED = 120;   //PLAYER's speed
    private boolean atDestination = true;   //Flag if not moving and standing on the rightInQueue spot
    private int destinationX;               //PLAYER's x destination cord
    private int destinationY;               //PLAYER's y destination cord

    private Direction movementDirection = Direction.none;   //Movements direction

    public Player(int x, int y, double xVel, double yVel) {
        super(x, y, xVel, yVel);
        destinationX = assignedTile.getX();
        destinationY = assignedTile.getY();
        ID = ObjectID.player;
    }

    private void goTo(Direction direction, int destination) {
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
        }

    }

    private void arrivedAtDestination() { // to mozna podobno lambda
        /*Stopping the object at destination*/
        y = destinationY;
        x = destinationX;
        xVel = 0;
        yVel = 0;

        atDestination = true;

        assignedTile.setPlayerOnTile(false);    //old tile
        assignedTile= Field.getTileRef(tileCordX,tileCordY);    //get new tile
        assignedTile.setPlayerOnTile(true);     //new tile
    }


    private void move(Direction direction) {
        int destination;
        try {
            destination = Field.getDestination(tileCordX, tileCordY, direction);
        } catch (Exception e) {
            return; // if not possible to move
        }
        goTo(direction, destination);
    }

    public void tick(double timeElapsedSeconds) {
        /*Calculating new position*/
        x += xVel * timeElapsedSeconds;
        y += yVel * timeElapsedSeconds;

        /*Stop check - if at or further than destination do a stop */
        if(!atDestination) {
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

        Direction direction;
        try {
            direction = KeyboardInput.queuedKeys.getLast();
        } catch (Exception e) {
            return;
        }

        /*Basic movement - object not moving and key pressed*/
        if (atDestination && direction != Direction.none)
            move(direction);
    }

    public void render(Graphics g) {
        g.setColor(Color.pink);
        g.fillRect((int) x, (int) y, 40, 40);
//        /*image input*/
//        BufferedImage img = null;
//        try {
//            img = ImageIO.read(new File("src/com/krynju/Untitled.png")); // eventually C:\\ImageTest\\pic2.jpg
//        }
//        catch (IOException e) { e.printStackTrace(); }
//        g.drawImage(img,(int)x,(int)y,null);
    }

    public void placeBomb(){
        if(atDestination || (abs(x - destinationX) < 30))
            Modules.bomb.setAt(tileCordX,tileCordY);
        else{
            switch(movementDirection){
                case up:
                    Modules.bomb.setAt(tileCordX,tileCordY+1);
                    break;
                case down:
                    Modules.bomb.setAt(tileCordX,tileCordY-1);
                    break;
                case left:
                    Modules.bomb.setAt(tileCordX+1,tileCordY);
                    break;
                case right:
                    Modules.bomb.setAt(tileCordX-1,tileCordY);
                    break;
            }
        }
    }
}
