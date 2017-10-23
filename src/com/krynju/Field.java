package com.krynju;

public class Field {
    public static final int fieldsX = 100;
    public static final int fieldsY = 100;
    public static final int fieldsSizeX = 200;
    public static final int fieldsSizeY = 200;
    public static final int tileSize = 40;

    public static int getDestination(int x, int y, Direction direction) throws UnableToMove {
        switch(direction){
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
}
