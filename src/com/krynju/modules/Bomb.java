package com.krynju.modules;

import java.awt.Color;
import java.awt.Graphics;

public class Bomb extends GameObject {
    private static final int tickingTime = 2;
    private boolean bombSet = false;
    private boolean bombTicking = false;
    private double timeElapsed = 0;

    public Bomb() {
        super(0, 0, 0, 0);
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
        g.drawRect((int) x-40, (int) y, 40, 40);
        g.drawRect((int) x+40, (int) y, 40, 40);
        g.drawRect((int) x, (int) y-40, 40, 40);
        g.drawRect((int) x, (int) y+40, 40, 40);
//        g.drawRect((int) x-80, (int) y, 40, 40);
//        g.drawRect((int) x+80, (int) y, 40, 40);
//        g.drawRect((int) x, (int) y-80, 40, 40);
//        g.drawRect((int) x, (int) y+80, 40, 40);

    }

    public void setAt(int x, int y) {
        assignedTile = Field.getTileRef(x, y);  //get new tileref
        assignedTile.setBombed(true);           //bomb the new tile

        setDangerZone(x,y,true);

        this.x = assignedTile.getX();           //get new x/y positions
        this.y = assignedTile.getY();
        this.tileCordX = x;
        this.tileCordY = y;

        bombSet = true;
        bombTicking = true;
    }

    private void setDangerZone(int x, int y,boolean mss) {
        try{
            Field.getTileRef(x,y-1).setBombDanger(mss);
        }catch(Exception e){}
        try{
            Field.getTileRef(x,y+1).setBombDanger(mss);
        }catch(Exception e){}
        try{
            Field.getTileRef(x-1,y).setBombDanger(mss);
        }catch(Exception e){}
        try{
            Field.getTileRef(x+1,y).setBombDanger(mss);
        }catch(Exception e){}
//        try{
//            Field.getTileRef(x,y-2).setBombDanger(mss);
//        }catch(Exception e){}
//        try{
//            Field.getTileRef(x,y+2).setBombDanger(mss);
//        }catch(Exception e){}
//        try{
//            Field.getTileRef(x-2,y).setBombDanger(mss);
//        }catch(Exception e){}
//        try{
//            Field.getTileRef(x+2,y).setBombDanger(mss);
//        }catch(Exception e){}
    }

    private void boom() {
        bombTicking = false;
        bombSet = false;
        assignedTile.setBombed(false);
        timeElapsed = 0;


        setDangerZone(tileCordX, tileCordY,false);


        if (checkTile(tileCordX, tileCordY)) {
            System.out.println("FKING CUNT");
        }
        if (checkTile(tileCordX, tileCordY + 1)) {
            System.out.println("chuj");
        }
        if (checkTile(tileCordX, tileCordY - 1)) {
            System.out.println("chuj");
        }
        if (checkTile(tileCordX + 1, tileCordY)) {
            System.out.println("chuj");
        }
        if (checkTile(tileCordX - 1, tileCordY)) {
            System.out.println("chuj");
        }

    }

    private boolean checkTile(int x, int y) {
        /*fetch the tile at cords*/
        Tile tile;
        try {
            tile = Field.getTileRef(x, y);
        } catch (Exception e) {
            return false;
        }

        /*destroy walls if possible*/
        if (tile.isWallOnTile()) {
            if (tile.getWall().isDestroyable())
                tile.getWall().destroy();
        }

        return tile.isPlayerOnTile();
    }

    public boolean isBombSet() {
        return bombSet;
    }

}
