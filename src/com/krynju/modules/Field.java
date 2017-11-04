package com.krynju.modules;

import com.krynju.enums.Direction;

import java.awt.Color;
import java.awt.Graphics;

public class Field {
    public static final int fieldsX = 100;
    public static final int fieldsY = 100;
    public static final int tileSize = 40;
    public static final int tileCount = 6;
    public static final int fieldsSizeX = tileSize * (tileCount-1);
    public static final int fieldsSizeY = tileSize * (tileCount-1);


    private static Tile[][] tileField;

    public Field() {
        int k = tileCount;
        tileField = new Tile[k][k];
        for(int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                tileField[i][j] = new Tile(fieldsX + i * tileSize, fieldsY + j * tileSize);
            }
        }
    }

    public static Tile getTileRef(int x,int y){
        return tileField[x][y];
    }

    private static boolean possibleToMove(int x,int y){
        if(tileField[x][y].isWallOnTile())
            return false;
        if(tileField[x][y].isBombed())
            return false;
        if(x < 0 || y < 0 || x > tileCount-1 || y > tileCount-1)
            return false;
        return true;
    }
    public static int getDestination(int x, int y, Direction direction) throws UnableToMove {
        switch (direction) {
            case up:
                if(possibleToMove(x,y -1))
                    return tileField[x][y-1].getY();
                else
                    throw new UnableToMove("un");
            case down:
                if(possibleToMove(x,y +1))
                    return tileField[x][y+1].getY();
                else
                    throw new UnableToMove("un");
            case left:
                if(possibleToMove(x-1,y))
                    return tileField[x-1][y].getX();
                else
                    throw new UnableToMove("un");
            case right:
                if(possibleToMove(x+1,y ))
                    return tileField[x+1][y].getX();
                else
                    throw new UnableToMove("un");
        }
        return 0;
    }


    public static void render(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(fieldsX, fieldsY, fieldsSizeX + tileSize, fieldsSizeY + tileSize);
    }
}
