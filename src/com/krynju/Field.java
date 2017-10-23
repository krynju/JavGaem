package com.krynju;

import java.awt.Color;
import java.awt.Graphics;

public class Field {
    public static final int fieldsX = 100;
    public static final int fieldsY = 100;
    public static final int fieldsSizeX = 200;
    public static final int fieldsSizeY = 200;
    public static final int tileSize = 40;

    private Tile[][] tileField;

    public Field() {
        int k = 6;
        this.tileField = new Tile[k][k];
        for(int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                this.tileField[i][j] = new Tile(fieldsX + j * tileSize, fieldsY + i * tileSize);
            }
        }
    }

    public static int getDestination(int x, int y, Direction direction) throws UnableToMove {
        switch (direction) {
            case up:
                if (y == fieldsY)
                    throw new UnableToMove("un");
                else
                    return y - tileSize;
            case down:
                if (y == fieldsY + fieldsSizeY)
                    throw new UnableToMove("un");
                else
                    return y + tileSize;
            case left:
                if (x == fieldsX)
                    throw new UnableToMove("un");
                else
                    return x - tileSize;
            case right:
                if (x == fieldsX + fieldsSizeX)
                    throw new UnableToMove("un");
                else
                    return x + tileSize;
        }
        return 0;
    }

    public static void render(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(fieldsX, fieldsY, fieldsSizeX + tileSize, fieldsSizeY + tileSize);
    }
}
