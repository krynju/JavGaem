package com.krynju.modules;

import com.krynju.Field;
import com.krynju.Tile;

import java.awt.Color;
import java.awt.Graphics;

public class Bomb extends GameObject {
    public static final int tickingTime = 2;
    public boolean bombSet = false;
    public boolean bombTicking = false;

    public double timeElapsed = 0;


    public Bomb(int x, int y, double xVel, double yVel) {
        super(x, y, xVel, yVel);
    }

    @Override
    public void tick(double timeElapsedSeconds) {
        if (bombTicking) {
            timeElapsed += timeElapsedSeconds;
            if (timeElapsed >= tickingTime)
                boom();
        }
    }

    @Override
    public void render(Graphics g) {
        if (!bombSet)
            return;
        else
            g.setColor(Color.black);
        g.drawOval((int) x, (int) y, 40, 40);
    }

    public void setAt(int x, int y) {
        assignedTile.setBombed(false);          //unbomb the old tile
        assignedTile = Field.getTileRef(x, y);  //get new tileref
        assignedTile.setBombed(true);           //bomb the new tile

        this.x = assignedTile.getX();           //get new x/y positions
        this.y = assignedTile.getY();
        this.tileCordX = x;
        this.tileCordY = y;

        bombSet = true;
        bombTicking = true;
    }

    public void boom(){
        bombTicking = false;
        bombSet = false;
        assignedTile.setBombed(false);
        timeElapsed = 0;

        if(checkTile(tileCordX,tileCordY)){
            System.out.println("FKING CUNT");
        }
        if(checkTile(tileCordX,tileCordY+1)){
            System.out.println("chuj");
        }
        if(checkTile(tileCordX,tileCordY-1)){
            System.out.println("chuj");
        }
        if(checkTile(tileCordX+1,tileCordY)){
            System.out.println("chuj");
        }
        if(checkTile(tileCordX-1,tileCordY)){
            System.out.println("chuj");
        }

    }

    public boolean checkTile(int x, int y){
        Tile tile;
        try{
            tile = Field.getTileRef(x,y);
        }catch(Exception e){
            return false;
        }

        if(tile.isWallOnTile()){
            if(tile.getWall().isDestroyable())
                tile.getWall().destroy();
        }
        return tile.isPlayerOnTile();
    }

}
