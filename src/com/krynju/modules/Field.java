package com.krynju.modules;

import com.krynju.Game;
import com.krynju.enums.Direction;

import java.awt.*;

public class Field {
    public static final int tileSize = 50;
    public static final int tileCountX = 13;
    public static final int tileCountY = 11;
    public static final int fieldsSizeX = tileSize * tileCountX;
    public static final int fieldsSizeY = tileSize * tileCountY;


    private static Tile[][] tileField;

    /**
     * The constructor initialises the tiles responsible for simulating the playfield
     * 0 1 2 3 4 5 6
     * 0
     * 1   7x5
     * 2   field[6][4]
     * 3   field[x][y]
     * 4
     */
    public Field() {
        tileField = new Tile[tileCountX][tileCountY];
        for (int i = 0; i < tileCountX; i++) {
            for (int j = 0; j < tileCountY; j++) {
                tileField[i][j] = new Tile(i * tileSize, j * tileSize);
            }
        }
    }

    public static Tile getTileRef(int x, int y) {
        return tileField[x][y];
    }

    /**
     * Checks if it is legal to move to the given cords
     *
     * @param x cord you want to move to
     * @param y cord you want to move to
     * @return true/false if possible to move to the given cords
     */
    private static boolean possibleToMove(int x, int y) {
        if (tileField[x][y].isWallOnTile() || tileField[x][y].isEnemyOnTile() ||
                tileField[x][y].isBombed() || tileField[x][y].isPlayerOnTile())
            return false;

        return x >= 0 && y >= 0 && x <= tileCountX - 1 && y <= tileCountY - 1;
    }

    /**
     * First uses Field#possibleToMove to check if the movement is legal, then returns the real cords to where
     * you have to move
     */
    public static int getDestination(int x, int y, Direction direction) throws UnableToMove {
        switch (direction) {
            case up:
                if (possibleToMove(x, y - 1))
                    return tileField[x][y - 1].getY();
                else
                    throw new UnableToMove("un");
            case down:
                if (possibleToMove(x, y + 1))
                    return tileField[x][y + 1].getY();
                else
                    throw new UnableToMove("un");
            case left:
                if (possibleToMove(x - 1, y))
                    return tileField[x - 1][y].getX();
                else
                    throw new UnableToMove("un");
            case right:
                if (possibleToMove(x + 1, y))
                    return tileField[x + 1][y].getX();
                else
                    throw new UnableToMove("un");
            case none:
                throw new UnableToMove("fk");
        }
        return 0;
    }

    public static void render(Graphics g) {
        g.setColor(Game.fieldColor);
        g.fillRect(0, 0, fieldsSizeX, fieldsSizeY);
    }
}
