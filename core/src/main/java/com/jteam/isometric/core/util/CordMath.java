package com.jteam.isometric.core.util;

import com.badlogic.gdx.math.Vector2;

public class CordMath {

    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 32;
    private static final int TILE_WIDTH_HALF = TILE_WIDTH / 2;
    private static final int TILE_HEIGHT_HALF = TILE_HEIGHT / 2;

    /**
    * Calculates left top corner or center tile position (cartesian) from cord (isometric)
    */
    public static void cordToPosition(Vector2 cord, Vector2 position, boolean center) {
        int cordX = (int)cord.x;
        int cordY = (int)cord.y;

        position.x = (cordX * TILE_WIDTH) + (cordY % 2 == 0 ? TILE_WIDTH_HALF : 0);
        position.y = cordY * TILE_HEIGHT_HALF;

        if(center) {
            position.x += TILE_WIDTH_HALF;
            position.y += TILE_HEIGHT_HALF;
        }
    }

    /**
     * Calculates cord (isometric) from position (cartesian)
     */
    public static void positionToCord(Vector2 position, Vector2 cord) {
        int posX = (int)position.x;
        int posY = (int)position.y;

        int normalizedPosX = posX % TILE_WIDTH;
        int normalizedPosY = posY % TILE_HEIGHT;

        int cordX = (int)Math.floor(posX / TILE_WIDTH);
        int cordY = (int)Math.floor(posY / TILE_HEIGHT) * 2;

        // left top corner
        if(isInsideTriangle(normalizedPosX, normalizedPosY,
                0 - 1, TILE_HEIGHT_HALF - 1,
                0 - 1, TILE_HEIGHT + 1,
                TILE_WIDTH_HALF + 1, TILE_HEIGHT + 1)) {
            cordY++;
        }
        // right top corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                TILE_WIDTH_HALF - 1, TILE_HEIGHT + 1,
                TILE_WIDTH + 1, TILE_HEIGHT + 1,
                TILE_WIDTH + 1, TILE_HEIGHT_HALF - 1)) {
            cordX++;
        }
        // left bottom corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                0 - 1, 0 - 1,
                0 - 1, TILE_HEIGHT_HALF + 1,
                TILE_WIDTH_HALF + 1, 0 - 1)) {
            cordX--;
            cordY++;
        }
        // right bottom corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                TILE_WIDTH + 1, TILE_HEIGHT_HALF + 1,
                TILE_WIDTH + 1, 0 - 1,
                TILE_WIDTH_HALF - 1, 0 - 1)) {
            cordY--;
        }

        cord.x = cordX;
        cord.y = cordY;
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
