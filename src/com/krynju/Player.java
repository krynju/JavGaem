package com.krynju;

import java.awt.Color;
import java.awt.Graphics;

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

    private void arrivedAtDestination() { // to mozna podobno lambda
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

        if(KeyboardInput.keyPressedDown != Direction.none)
            move(KeyboardInput.keyPressedDown);

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

    private void move(Direction direction) {
        int destination;
        if (atDestination) {
            try {
                destination = Field.getDestination(destinationX, destinationY, direction);
            } catch (Exception e) {
                return;
            }
        } else
            return;

        atDestination = false;
        movementDirection = direction;
        int speed = 120;
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
