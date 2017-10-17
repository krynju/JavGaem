package com.krynju;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Player extends GameObject {

    public Player(double x, double y,double xVel,double yVel) {
        super(x, y,xVel,yVel);
        ID = ObjectID.player;
    }

    public void tick(double timeElapsedSeconds){

        x += xVel * timeElapsedSeconds;
        y += yVel * timeElapsedSeconds;

    }

    public void render(Graphics g){
        /* CIRCLE
        g.setColor(Color.pink);
        g.fillOval((int)x,(int)y,50,50);
        */

        /*image input*/
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src/com/krynju/Untitled.png")); // eventually C:\\ImageTest\\pic2.jpg
        }
        catch (IOException e) { e.printStackTrace(); }
        g.drawImage(img,(int)x,(int)y,null);
    }
}
