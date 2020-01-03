package com.jteam.isometric.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jteam.isometric.core.renderer.Renderer;
import com.jteam.isometric.core.shape.Line;
import com.jteam.isometric.core.util.MouseMath;
import lombok.RequiredArgsConstructor;

import static com.jteam.isometric.core.util.TileConst.TILE_HEIGHT;
import static com.jteam.isometric.core.util.TileConst.TILE_HEIGHT_HALF;
import static com.jteam.isometric.core.util.TileConst.TILE_WIDTH;
import static com.jteam.isometric.core.util.TileConst.TILE_WIDTH_HALF;

@RequiredArgsConstructor
public class DebugRenderer {

    private final Renderer renderer;
    private final Viewport viewport;

    private final Vector2 mouseWorldCord = new Vector2();

    public void render() {
        MouseMath.getWorldCord(viewport, mouseWorldCord);

        float debugCursorTilePosX = (mouseWorldCord.x * TILE_WIDTH) + (mouseWorldCord.y % 2 == 0 ? 0 : TILE_WIDTH_HALF) - viewport.getCamera().position.x;
        float debugCursorTilePosY = (mouseWorldCord.y * TILE_HEIGHT_HALF) - viewport.getCamera().position.y;

        Line leftTop = Line.builder()
                .start(new Vector2(debugCursorTilePosX, debugCursorTilePosY + TILE_HEIGHT_HALF))
                .end(new Vector2(debugCursorTilePosX + TILE_WIDTH_HALF, debugCursorTilePosY + TILE_HEIGHT))
                .build();

        Line topRight = Line.builder()
                .start(new Vector2(debugCursorTilePosX + TILE_WIDTH_HALF, debugCursorTilePosY + TILE_HEIGHT))
                .end(new Vector2(debugCursorTilePosX + TILE_WIDTH, debugCursorTilePosY + TILE_HEIGHT_HALF))
                .build();

        Line rightBottom = Line.builder()
                .start(new Vector2(debugCursorTilePosX + TILE_WIDTH, debugCursorTilePosY + TILE_HEIGHT_HALF))
                .end(new Vector2(debugCursorTilePosX + TILE_WIDTH_HALF, debugCursorTilePosY))
                .build();

        Line bottomLeft = Line.builder()
                .start(new Vector2(debugCursorTilePosX + TILE_WIDTH_HALF, debugCursorTilePosY))
                .end(new Vector2(debugCursorTilePosX, debugCursorTilePosY + TILE_HEIGHT_HALF))
                .build();

        renderer.drawLine(leftTop);
        renderer.drawLine(topRight);
        renderer.drawLine(rightBottom);
        renderer.drawLine(bottomLeft);
    }

}
