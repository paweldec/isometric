package com.jteam.isometric.core.path;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import lombok.extern.slf4j.Slf4j;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PathFinder {

    private static final String LAYER_NAME = "collision";
    private static final String CELL_PROPERTY = "walkable";

    private final TiledMap map;
    private AStarStaggeredIsometricFinder<GridCell> finder;
    private NavigationGrid<GridCell> navigationGrid;

    public PathFinder(TiledMap map) {
        this.map = map;
        init();
    }

    private void init() {
        initFinder();
        initNavigationGrid();
    }

    public List<GridCell> find(Vector2 startCord, Vector2 endCord) {
        return Optional.ofNullable(this.finder.findPath(startCord, endCord, this.navigationGrid))
                .orElse(new ArrayList<>());
    }

    private void initFinder() {
        this.finder = new AStarStaggeredIsometricFinder<>(GridCell.class);
    }

    private void initNavigationGrid() {
        final TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(LAYER_NAME);

        if(layer == null) {
            throw new IllegalArgumentException(String.format("Layer '%s' not found on map!", LAYER_NAME));
        }

        int width = layer.getWidth();
        int height = layer.getHeight();

        if(width < 1 || height < 1) {
            throw new IllegalArgumentException(String.format("Invalid layer size %sx%s!", width, height));
        }

        GridCell[][] cells = new GridCell[height][width];

        for(int y = cells.length - 1; y >= 0; y--) {
            for(int x = cells[y].length - 1; x >= 0; x--) {
                boolean walkable = true;

                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if(cell != null) {
                    Integer property = cell.getTile().getProperties().get(CELL_PROPERTY, Integer.class);

                    if(property != null && property == 0) {
                        walkable = false;
                    }
                }

                cells[x][y] = new GridCell(x, y, walkable);
            }
        }

        this.navigationGrid = new StaggeredIsometricNavigationGrid<>(cells);
    }

}
