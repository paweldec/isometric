package com.jteam.isometric.core.path;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import java.util.List;

public class PathFinder {

    private static final String LAYER_NAME = "collision";
    private static final String CELL_PROPERTY = "walkable";

    private final TiledMap map;
    private AStarGridFinder<GridCell> finder;
    private NavigationGrid<GridCell> navigationGrid;

    public PathFinder(TiledMap map) {
        this.map = map;
        init();
    }

    public List<GridCell> find(Vector2 start, Vector2 end) {
        return this.finder.findPath((int)start.x, (int)start.y, (int)end.x, (int)end.y, this.navigationGrid);
    }

    private void init() {
        initGridFinder();
        initNavigationGrid();
    }

    private void initGridFinder() {
        GridFinderOptions opt = new GridFinderOptions();
        opt.allowDiagonal = false;
        this.finder = new AStarGridFinder<>(GridCell.class, opt);
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

        GridCell[][] cells = new GridCell[width][height];

        for(int x = 0; x < cells.length; x++) {
            for(int y = 0; y < cells[x].length; y++) {
                boolean walkable = true;

                TiledMapTileLayer.Cell cell = layer.getCell(x, (cells.length - 1) - y);

                if(cell != null) {
                    Integer property = cell.getTile().getProperties().get(CELL_PROPERTY, Integer.class);

                    if(property != null && property == 0) {
                        walkable = false;
                    }
                }

                cells[x][y] = new GridCell(x, y, walkable);
            }
        }

        this.navigationGrid = new NavigationGrid<>(cells);
    }
}
