package com.jteam.isometric.core.util;

import com.badlogic.gdx.math.Vector2;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.jteam.isometric.core.util.TileConst.TILE_HEIGHT;
import static com.jteam.isometric.core.util.TileConst.TILE_HEIGHT_HALF;
import static com.jteam.isometric.core.util.TileConst.TILE_WIDTH;
import static com.jteam.isometric.core.util.TileConst.TILE_WIDTH_HALF;

@Slf4j
public class CordMath {

    /**
    * Calculates left bottom corner or center tile position (cartesian) from cord (isometric)
    */
    public static void cordToPosition(Vector2 cord, Vector2 position) {
        int cordX = (int)cord.x;
        int cordY = (int)cord.y;

        position.x = (cordX * TILE_WIDTH) + (cordY % 2 == 0 ? 0 : TILE_WIDTH_HALF);
        position.y = cordY * TILE_HEIGHT_HALF;

        position.x += TILE_WIDTH_HALF;
        position.y += TILE_HEIGHT_HALF;
    }

    /**
    * Return neighbor cord for specified cord and direction
    */
    public static Optional<Vector2> getNeighborCord(Vector2 cord, Vector2 cordNeighbor, Direction dir) {
        Vector2 position = new Vector2();
        cordToPosition(cord, position);

        switch (dir) {
            case N: position.y += TILE_HEIGHT; break;
            case NE: position.x += TILE_WIDTH_HALF; position.y += TILE_HEIGHT_HALF; break;
            case E: position.x += TILE_WIDTH; break;
            case SE: position.x += TILE_WIDTH_HALF; position.y -= TILE_HEIGHT_HALF; break;
            case S: position.y -= TILE_HEIGHT; break;
            case SW: position.x -= TILE_WIDTH_HALF; position.y -= TILE_HEIGHT_HALF; break;
            case W: position.x -= TILE_WIDTH; break;
            case NW: position.x -= TILE_WIDTH_HALF; position.y += TILE_HEIGHT_HALF; break;
        }

        if(position.x < 0 || position.y < 0) {
            return Optional.empty();
        }

        positionToCord(position, cordNeighbor);

        return Optional.of(cordNeighbor);
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
            cordX--;
            cordY++;
        }
        // right top corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                TILE_WIDTH_HALF - 1, TILE_HEIGHT + 1,
                TILE_WIDTH + 1, TILE_HEIGHT + 1,
                TILE_WIDTH + 1, TILE_HEIGHT_HALF - 1)) {
            cordY++;
        }
        // left bottom corner
        else if(isInsideTriangle(normalizedPosX, normalizedPosY,
                0 - 1, 0 - 1,
                0 - 1, TILE_HEIGHT_HALF + 1,
                TILE_WIDTH_HALF + 1, 0 - 1)) {
            cordX--;
            cordY--;
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
