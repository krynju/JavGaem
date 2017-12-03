package com.krynju.modules;

import com.krynju.Controller;
import com.krynju.Game;
import com.krynju.enums.Ending;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;

public class Bomb extends GameObject {
    private Controller controller;
    private final int tickingTime = 2;
    private boolean bombSet = false;
    private boolean bombTicking = false;
    private double timeElapsed = 0;
    private LinkedList<Tile> dangerTiles = new LinkedList<>();

    public Bomb(Controller c) {
        super(0, 0, 0, 0);
        this.controller=c;
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
        /*if the bomb isn't set return*/
        if (!bombSet)
            return;
        /*else*/

        /*draw a gray bomb rectangle*/
        g.setColor(new Color(0,0,0,125));
        g.fillRect(assignedTile.getX(), assignedTile.getY(), 40, 40);

        /*draw the countdown timer on the bomb*/
        g.setColor(Color.white);
        g.setFont(new Font("Verdana",Font.PLAIN,21));
        String timerString = String.valueOf(2 - timeElapsed);
        g.drawString(timerString.substring(0,3),assignedTile.getX()+3,assignedTile.getY()+28);

        /*draw the red danger zone*/
        g.setColor(Game.bombColor);
        for (Tile tile : dangerTiles) {
            if (!tile.isWallOnTile())
                g.fillRect(tile.getX(), tile.getY(), 40, 40);
        }

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

    public void setAt(int x, int y) {
        assignedTile = Field.getTileRef(x, y);  //get new tileref
        assignedTile.setBombed(true);           //bomb the new tile
        this.x = assignedTile.getX();           //get new x/y positions
        this.y = assignedTile.getY();
        this.tileCordX = x;
        this.tileCordY = y;
        bombSet = true;
        bombTicking = true;
        fetchingTilesAround(this.tileCordX, this.tileCordY);
        setDangerZone(true);
    }

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

        bombTicking = false;
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
