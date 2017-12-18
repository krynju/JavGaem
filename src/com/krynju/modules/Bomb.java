package com.krynju.modules;

import com.krynju.Controller;
import com.krynju.Game;
import com.krynju.enums.Ending;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;

/**Bomb object class
 * @see Player*/
public class Bomb extends GameObject {
    private Controller controller;
    /**Time reference used when the bomb is set and ticking*/
    private final int tickingTime = 2;
    /**Flag that says if the bomb is set*/
    private boolean bombSet = false;

    /**Counter of the time elapsed from the bomb creation*/
    private double timeElapsed = 0;
    /**List containing tiles that are endangered by the bomb explosion*/
    private LinkedList<Tile> dangerTiles = new LinkedList<>();

    public Bomb(Controller c) {
        super(0, 0);
        this.controller=c;
    }


    @Override
    public void tick(double timeElapsedSeconds) {
        if (bombSet) {
            timeElapsed += timeElapsedSeconds;
            if (timeElapsed >= tickingTime)
                boom();
        }
    }


    @Override
    public void render(Graphics g) {
        /*if the bomb isn't set return*/
        if (!bombSet)
            return;
        /*else*/

        /*draw the red danger zone*/
        g.setColor(Game.bombColor);
        g.fillRect(assignedTile.getX(), assignedTile.getY(), 40, 40);
        for (Tile tile : dangerTiles) {
            if (!tile.isWallOnTile())
                g.fillRect(tile.getX(), tile.getY(), 40, 40);
        }

        /*draw a gray bomb rectangle*/
        g.setColor(new Color(50,50,50));
        g.fillOval(assignedTile.getX(), assignedTile.getY(), 40, 40);


        /*draw the countdown timer on the bomb*/
        g.setColor(Color.white);
        g.setFont(new Font("Verdana",Font.PLAIN,21));
        if(timeElapsed < 2)
            g.drawString(String.valueOf(2.0 - timeElapsed).substring(0,3),assignedTile.getX()+3,assignedTile.getY()+28);
        else
            g.drawString(String.valueOf(0.0).substring(0,3),assignedTile.getX()+3,assignedTile.getY()+28);

        /*draw the "BOOM"'s over the danger zone*/
        g.setColor(Color.white);
        g.setFont(new Font("Verdana",Font.PLAIN,12));
        if(timeElapsed>=1.8){
            for(Tile tile:dangerTiles){
                if(!tile.isWallOnTile())
                    g.drawString("BOOM",tile.getX()+1,tile.getY()+24);
            }
        }
    }

    /**Sets the bomb on a tile
     * @param x tile cord
     * @param y tile cord*/
    public void setAt(int x, int y) {
        assignedTile = Field.getTileRef(x, y);  //get new tileref
        assignedTile.setBombed(true);           //bomb the new tile
        this.x = assignedTile.getX();           //get new x/y positions
        this.y = assignedTile.getY();
        this.tileCordX = x;
        this.tileCordY = y;
        bombSet = true;
        fetchingTilesAround(this.tileCordX, this.tileCordY);
        setDangerZone(true);
    }

    /**Finding adjacent tiles to the main tile
     * and adding them to the dangerTiles list
     * @param x cord of the main tile
     * @param y cord of the main tile
     * @see Bomb#dangerTiles*/
    private void fetchingTilesAround(int x, int y) {
        int a[][] = new int[][]{
                {x + 1, y, x + 2, y},
                {x - 1, y, x - 2, y},
                {x, y + 1, x, y + 2},
                {x, y - 1, x, y - 2}
        };
        for (int i[], c = 0; c < 4; c++) {
            i = a[c];
            Tile temp;
            try {
                temp = Field.getTileRef(i[0], i[1]);
            } catch (Exception e) {
                continue;
            }
            dangerTiles.add(temp);
            if (!temp.isWallOnTile()) {
                try {
                    temp = Field.getTileRef(i[2], i[3]);
                } catch (Exception e) {
                    continue;
                }
                dangerTiles.add(temp);
            }
        }
    }

    /**Sets bombDanger flags to the tiles in the dangerTIles list
     * @param state the flag state true/false
     * @see Bomb#dangerTiles
     * @see Tile#bombDanger
     * @see Tile#twoBombDanger*/
    private void setDangerZone(boolean state) {
        for (Tile tile : dangerTiles) {
            if (state) {
                if (tile.isBombDanger())
                    tile.setTwoBombDanger(true);
                else
                    tile.setBombDanger(true);
            } else {
                if (tile.isTwoBombDanger())
                    tile.setTwoBombDanger(false);
                else
                    tile.setBombDanger(false);
            }
        }
    }

    /**Checks all dangerTiles if there is a player object standing on them
     * and eventually ending the game if there is
     * also deciding if win/lose/draw
     * */
    private void boom() {
        boolean playerCaught = false;
        boolean enemyCaught = false;
        dangerTiles.addFirst(assignedTile);
        for (Tile tile : dangerTiles) {
            if (tile.isWallOnTile())
                tile.getWall().destroy();

            if (tile.isPlayerOnTile()) {
                playerCaught = true;
            }
            if (tile.isEnemyOnTile()) {
                enemyCaught = true;
            }
        }
        dangerTiles.remove(assignedTile);

        if (playerCaught && enemyCaught) {
            controller.setEndingType(Ending.draw);
            controller.setGameEnd(true);
            return;

        } else if (playerCaught) {
            controller.setEndingType(Ending.lose);
            controller.setGameEnd(true);
            return;

        } else if (enemyCaught) {
            controller.setEndingType(Ending.win);
            controller.setGameEnd(true);
            return;
        }

        bombSet = false;
        assignedTile.setBombed(false);
        timeElapsed = 0;
        setDangerZone(false);
        dangerTiles.clear();

    }

    public boolean isBombSet() {
        return bombSet;
    }
}
