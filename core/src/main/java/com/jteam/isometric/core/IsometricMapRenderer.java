package com.jteam.isometric.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.jteam.isometric.core.renderer.Renderer;
import com.jteam.isometric.core.util.CordMath;
import lombok.extern.slf4j.Slf4j;

import static com.jteam.isometric.core.util.TileConst.TILE_HEIGHT_HALF;
import static com.jteam.isometric.core.util.TileConst.TILE_WIDTH_HALF;

@Slf4j
public class IsometricMapRenderer implements TiledMapRenderer, Disposable {

    private Renderer renderer;
    private TiledMap map;
    private OrthographicCamera camera;
    private boolean showCords;

    public IsometricMapRenderer(Renderer renderer, TiledMap map) {
        this.renderer = renderer;
        this.map = map;
        this.showCords = true;
    }

    @Override
    public void renderObjects(MapLayer layer) {

    }

    @Override
    public void renderObject(MapObject object) {

    }

    @Override
    public void renderTileLayer(TiledMapTileLayer layer) {
        float tileWidth = layer.getTileWidth();
        float tileWidthHalf = tileWidth / 2;
        float tileHeight = layer.getTileHeight();
        float tileHeightHalf = tileHeight / 2;

        int viewportWidthInTiles = (int) ((camera.viewportWidth) / tileWidth);
        int viewportHeightInTiles = (int) ((camera.viewportHeight) / tileHeightHalf);

        int offsetInTiles = 6;

        int cameraPosXInTiles = (int) (camera.position.x / tileWidth);
        int cameraPosYInTiles = (int) (camera.position.y / tileHeightHalf);

        for(int y = viewportHeightInTiles + offsetInTiles; y >= 0 - offsetInTiles; y--) {
            for(int x = viewportWidthInTiles + offsetInTiles; x >= 0 - offsetInTiles; x--) {
                int tileIndexX = x + cameraPosXInTiles;
                int tileIndexY = y + cameraPosYInTiles;

                final TiledMapTileLayer.Cell cell = layer.getCell(tileIndexX, tileIndexY);
                if(cell == null) continue;

                final TiledMapTile tile = cell.getTile();
                if(tile == null) continue;

                float tilePosX = (tileIndexX * tileWidth) + (tileIndexY % 2 == 0 ? 0 : tileWidthHalf) - camera.position.x;
                float tilePosY = (tileIndexY * tileHeightHalf) - camera.position.y;

                renderer.draw(tile.getTextureRegion(), tilePosX, tilePosY);

                if(showCords) {
                    renderCord(tilePosX, tilePosY);
                }
            }
        }
    }

    private void renderCord(float tilePosX, float tilePosY) {
        final Vector2 position = new Vector2(tilePosX + camera.position.x, tilePosY + camera.position.y);
        position.x += TILE_WIDTH_HALF;
        position.y += TILE_HEIGHT_HALF;
        final Vector2 cord = new Vector2();
        CordMath.positionToCord(position, cord);
        renderer.drawText(String.format("%sx%s", (int)cord.x, (int)cord.y), tilePosX + 16 + 5, tilePosY + 16 + 5);
    }

    @Override
    public void renderImageLayer(TiledMapImageLayer layer) {

    }

    @Override
    public void setView(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void setView(Matrix4 projectionMatrix, float viewBoundsX, float viewBoundsY, float viewBoundsWidth, float viewBoundsHeight) {

    }

    @Override
    public void render() {
        map.getLayers().forEach(this::renderMapLayer);
    }

    @Override
    public void render(int[] layers) {

    }

    @Override
    public void dispose() {

    }

    protected void renderMapLayer (MapLayer layer) {
        if (!layer.isVisible()) return;

        if (layer instanceof MapGroupLayer) {
            ((MapGroupLayer) layer).getLayers().forEach(this::renderMapLayer);
        } else if (layer instanceof TiledMapTileLayer) {
            renderTileLayer((TiledMapTileLayer)layer);
        } else if (layer instanceof TiledMapImageLayer) {
            renderImageLayer((TiledMapImageLayer)layer);
        } else {
            renderObjects(layer);
        }
    }

}
