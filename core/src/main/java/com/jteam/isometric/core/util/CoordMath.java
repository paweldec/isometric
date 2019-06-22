package com.jteam.isometric.core.util;

import com.badlogic.gdx.math.Vector2;

public class CoordMath {

    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 32;
    private static final int TILE_WIDTH_HALF = TILE_WIDTH / 2;
    private static final int TILE_HEIGHT_HALF = TILE_HEIGHT / 2;

    /**
    * Calculates left top corner or center tile position (cartesian) from coord (isometric)
    */
    public static void coordToPosition(Vector2 coord, Vector2 position, boolean center) {
        int coordX = (int)coord.x;
        int coordY = (int)coord.y;

        position.x = (coordX * TILE_WIDTH) + (coordY % 2 == 0 ? 0 : TILE_WIDTH_HALF);
        position.y = coordY * TILE_HEIGHT_HALF;

        if(center) {
            position.x += TILE_WIDTH_HALF;
            position.y += TILE_HEIGHT_HALF;
        }
    }

    /**
     * Calculates coord (isometric) from position (cartesian)
     */
    public static void positionToCoord(Vector2 position, Vector2 coord) {
        int posX = (int)position.x;
        int posY = (int)position.y;

        int normalizedPosX = posX % (TILE_WIDTH);
        int normalizedPosY = posY % (TILE_HEIGHT);

        int coordX = (int)Math.floor(posX / (TILE_WIDTH));
        int coordY = (int)Math.floor(posY / (TILE_HEIGHT)) * 2;

        // left top corner
        if(isInsideTriangle(normalizedPosX, normalizedPosY,
                0 - 1, TILE_HEIGHT_HALF - 1,
                0 - 1, TILE_HEIGHT + 1,
                TILE_WIDTH_HALF + 1, TILE_HEIGHT + 1)) {
            coordY--;
        }
        // right top corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                TILE_WIDTH_HALF - 1, TILE_HEIGHT + 1,
                TILE_WIDTH + 1, TILE_HEIGHT + 1,
                TILE_WIDTH + 1, TILE_HEIGHT_HALF - 1)) {
            coordX++;
            coordY--;
        }
        // left bottom corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                0 - 1, 0 - 1,
                0 - 1, TILE_HEIGHT_HALF + 1,
                TILE_WIDTH_HALF + 1, 0 - 1)) {
            coordX--;
            coordY--;
        }
        // right bottom corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                TILE_WIDTH + 1, TILE_HEIGHT_HALF + 1,
                TILE_WIDTH + 1, 0 - 1,
                TILE_WIDTH_HALF - 1, 0 - 1)) {
            coordY++;
        }

        coord.x = coordX;
        coord.y = coordY;
    }

    private static boolean isInsideTriangle(int testPointX, int testPointY,
                                            int tPointX1, int tPointY1, int tPointX2, int tPointY2, int tPointX3, int tPointY3) {

        int planeAB = (tPointX1 - testPointX) * (tPointY2 - testPointY) - (tPointX2 - testPointX) * (tPointY1 - testPointY);
        int planeBC = (tPointX2 - testPointX) * (tPointY3 - testPointY) - (tPointX3 - testPointX) * (tPointY2 - testPointY);
        int planeCA = (tPointX3 - testPointX) * (tPointY1 - testPointY) - (tPointX1 - testPointX) * (tPointY3 - testPointY);

        return sign(planeAB) == sign(planeBC) && sign(planeBC) == sign(planeCA);
    }

    private static int sign(int value) {
        return value == 0 ? 0 : Math.abs(value) / value;
    }
}
