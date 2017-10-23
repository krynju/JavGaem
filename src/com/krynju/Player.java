package com.krynju;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject {
    private int tileCordX;
    private int tileCordY;
    private static final int SPEED = 120;   // PLAYER's speed
    private boolean atDestination = true;   // Flag if not moving and standing on the right spot
    private int destinationX;               //PLAYER's x destination cord
    private int destinationY;               //PLAYER's y destination cord
    private int queuedDestination;          //PLAYER's queued destination cord
    private Direction queuedDirection = Direction.none;     //Queued movements direction
    private Direction movementDirection = Direction.none;   //Movements direction

    public Player(double x, double y, double xVel, double yVel) {
        super(x, y, xVel, yVel);
        destinationX = (int) x;
        destinationY = (int) y;
        ID = ObjectID.player;
        tileCordX = 0;
        tileCordY = 0;
    }

    private void goThere(Direction direction, int destination){
        atDestination = false;
        movementDirection = direction;
        switch (direction){
            case up:
                setxVel(0);
                setyVel(-SPEED);
                destinationY = destination;
                break;
            case down:
                setxVel(0);
                setyVel(SPEED);
                destinationY = destination;
                break;
            case left:
                setxVel(-SPEED);
                setyVel(0);
                destinationX = destination;
                break;
            case right:
                setxVel(SPEED);
                setyVel(0);
                destinationX = destination;
                break;
        }
    }
    private void arrivedAtDestination() { // to mozna podobno lambda
        /*Stopping the object at destination*/
        setY(destinationY);
        setX(destinationX);
        setxVel(0);
        setyVel(0);
        atDestination = true;

        /*Check if there's a queued movement and if the queued movement key is still pressed down*/
        if(queuedDirection != Direction.none && queuedDirection == KeyboardInput.keyPressedDown){
            goThere(queuedDirection,queuedDestination);
            queuedDirection = Direction.none;
        }
    }
    private void queueMovement(Direction keyPressedDown) {
        int destination;
        try {
            destination = Field.getDestination(destinationX, destinationY, keyPressedDown);
        } catch (Exception e) {
            return;
        }
        queuedDestination = destination;
        queuedDirection = keyPressedDown;
    }
    private void move(Direction direction) {
        int destination;
        try {
            destination = Field.getDestination(destinationX, destinationY, direction);
        } catch (Exception e) {
            return; // if not possible to move
        }
        goThere(direction,destination);
    }

    public void tick(double timeElapsedSeconds) {
        /*Calculating new position*/
        x += xVel * timeElapsedSeconds;
        y += yVel * timeElapsedSeconds;

        /*Stop check - if at or further than destination do a stop */
        switch (movementDirection) {
            case up:
                if (y <= destinationY - 0.5) arrivedAtDestination(); break;
            case down:
                if (y >= destinationY - 0.5) arrivedAtDestination(); break;
            case left:
                if (x <= destinationX - 0.5) arrivedAtDestination(); break;
            case right:
                if (x >= destinationX - 0.5) arrivedAtDestination(); break;
            case none:
                break;
        }

        /*Basic movement - object not moving and key pressed*/
        if(atDestination && KeyboardInput.keyPressedDown != Direction.none)
            move(KeyboardInput.keyPressedDown);

        /*Queued movement - object moving and close to destination, key pressed*/
        if(!atDestination && KeyboardInput.keyPressedDown != Direction.none) {
            int d = 10;
            switch (movementDirection) {
                case up:
                    if (y <= destinationY - d) queueMovement(KeyboardInput.keyPressedDown);
                    break;
                case down:
                    if (y >= destinationY - d) queueMovement(KeyboardInput.keyPressedDown);
                    break;
                case left:
                    if (x <= destinationX - d) queueMovement(KeyboardInput.keyPressedDown);
                    break;
                case right:
                    if (x >= destinationX - d) queueMovement(KeyboardInput.keyPressedDown);
                    break;
                case none:
                    break;
            }
        }
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
}
