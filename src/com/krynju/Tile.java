package com.krynju;

public class Tile {
    private int x, y;
    private boolean blocked = false;
    private boolean bombed = false;


    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
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

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
