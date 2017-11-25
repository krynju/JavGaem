package com.krynju.modules;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

public class Bomb extends GameObject {
    private static final int tickingTime = 2;
    private boolean bombSet = false;
    private boolean bombTicking = false;
    private double timeElapsed = 0;
    private LinkedList<Tile> dangerTiles = new LinkedList<>();

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
        for (Tile tile : dangerTiles) {
            if(!tile.isWallOnTile())
                g.drawRect(tile.getX(), tile.getY(), 40, 40);
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
            if (temp.isWallOnTile()) {
                continue;
            } else {
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
            tile.setBombDanger(state);
        }
    }

    private void boom() {
        for (Tile tile : dangerTiles) {
            if (tile.isWallOnTile())
                tile.getWall().destroy();
            if (tile.isPlayerOnTile())
                System.out.println("die fggt");
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
