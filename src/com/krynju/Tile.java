package com.krynju;

import com.krynju.modules.Wall;

public class Tile {
    private Wall wall;
    private int x, y;
    private boolean wallOnTile = false;
    private boolean bombed = false;
    private boolean playerOnTile = false;
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public boolean isPlayerOnTile() {
        return playerOnTile;
    }

    public void setPlayerOnTile(boolean playerOnTile) {
        this.playerOnTile = playerOnTile;
    }

    public boolean isBombed() {
        return bombed;
    }

    public void setBombed(boolean bombed) {
        this.bombed = bombed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isWallOnTile() {
        return wallOnTile;
    }

    public void setWallOnTile(boolean wallOnTile) {
        this.wallOnTile = wallOnTile;
    }
}
