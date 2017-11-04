package com.krynju.modules;

import java.awt.Color;
import java.awt.Graphics;

public class Bomb extends GameObject {
    private static final int tickingTime = 2;

    private boolean bombSet = false;

    private boolean bombTicking = false;
    private double timeElapsed = 0;


    public Bomb(int x, int y) {
        super(x, y,0, 0);
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

    private void boom(){
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

    private boolean checkTile(int x, int y){
        /*fetch the tile at cords*/
        Tile tile;
        try{
            tile = Field.getTileRef(x,y);
        }catch(Exception e){
            return false;
        }

        /*destroy walls if possible*/
        if(tile.isWallOnTile()){
            if(tile.getWall().isDestroyable())
                tile.getWall().destroy();
        }

        return tile.isPlayerOnTile();
    }

    public boolean isBombSet() {
        return bombSet;
    }

}
