package com.krynju.modules;

/**
 * Basic tile class
 * The field is made of tiles
 *
 * @see Field
 */
public class Tile {
    /**
     * Cords
     */
    private int x, y;
    /**
     * Wall reference that sits on the tile
     */
    private Wall wall;
    /**A flag that says if a wall is on the tile*/
    private boolean wallOnTile = false;
    /**
     * A flag that says if the wall on the tile is destroyable
     */
    private boolean destroyable = false;

    /**A flag that says if the player is standing on the tile
     * @see com.krynju.enums.PlayerID#player
     * @see Player*/
    private boolean playerOnTile = false;
    /**A flag that says if the enemy is standing on the tile
     * @see com.krynju.enums.PlayerID#enemy
     * @see Player*/
    private boolean enemyOnTile = false;
    /**
     * A flag that says if there is a bomb on the tile
     *
     * @see Bomb
     */
    private boolean bombed = false;
    /**A flag that says if the tile is in range of a bomb that is going to explode*/
    private boolean bombDanger = false;
    /**A Flag that says if the tile is in range of two bombs*/
    private boolean twoBombDanger = false;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDestroyable() {
        return destroyable;
    }

    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

    public boolean isTwoBombDanger() {
        return twoBombDanger;
    }

    public void setTwoBombDanger(boolean twoBombDanger) {
        this.twoBombDanger = twoBombDanger;
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

    public boolean isBombDanger() {
        return bombDanger;
    }

    public void setBombDanger(boolean bombDanger) {
        this.bombDanger = bombDanger;
    }

    public boolean isEnemyOnTile() {
        return enemyOnTile;
    }

    public void setEnemyOnTile(boolean enemyOnTile) {
        this.enemyOnTile = enemyOnTile;
    }
}
