package com.krynju;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Player extends GameObject {
    private int destinationX;
    private int destinationY;
    private Direction movementDirection = Direction.none;
    private boolean atDestination = true;
    private boolean closeToDestination = false;

    public Player(double x, double y, double xVel, double yVel) {
        super(x, y, xVel, yVel);
        destinationX = (int) x;
        destinationY = (int) y;
        ID = ObjectID.player;
    }

    void arrivedAtDestination() {
        setY(destinationY);
        setX(destinationX);
        setxVel(0);
        setyVel(0);
        atDestination = true;
        closeToDestination = false;
    }

    public void tick(double timeElapsedSeconds) {

        x += xVel * timeElapsedSeconds;
        y += yVel * timeElapsedSeconds;

        if (!atDestination) {
            switch (movementDirection) {
                case up:
                    //if (y - destinationY < 5) closeToDestination = true;
                    if (y <= destinationY) arrivedAtDestination();
                    break;
                case down:
                    //if (destinationY - y < 5) closeToDestination = true;
                    if (y >= destinationY) arrivedAtDestination();
                    break;
                case left:
                    //if (x - destinationX < 5) closeToDestination = true;
                    if (x <= destinationX) arrivedAtDestination();
                    break;
                case right:
                    //if (destinationX - x < 5) closeToDestination = true;
                    if (x >= destinationX) arrivedAtDestination();
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

    public void move(Direction direction) {
        int destination;

        if (atDestination)
            try {
                destination = Field.getDestination(destinationX, destinationY, direction);
            } catch (Exception e) {
                return;
            }
        else if (closeToDestination && direction == movementDirection) {
            closeToDestination = false;
            try {
                destination = Field.getDestination(destinationX, destinationY, direction);
            } catch (Exception e) {
                return;
            }
        } else
            return;



        atDestination = false;
        movementDirection = direction;
        int speed = 80;
        switch (direction) {
            case up:
                setxVel(0);
                setyVel(-speed);
                destinationY = destination;
                break;
            case down:
                setxVel(0);
                setyVel(speed);
                destinationY = destination;
                break;
            case left:
                setxVel(-speed);
                setyVel(0);
                destinationX = destination;
                break;
            case right:
                setxVel(speed);
                setyVel(0);
                destinationX = destination;
                break;
        }
    }
}
