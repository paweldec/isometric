package com.jteam.isometric.core.path;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import lombok.extern.slf4j.Slf4j;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    private void init() {
        initGridFinder();
        initNavigationGrid();
    }

    public List<GridCell> find(Vector2 start, Vector2 end) {
        List<GridCell> path = this.finder.findPath((int)start.x, (int)start.y, (int)end.x, (int)end.y, this.navigationGrid);
        return optimizePath(path);
    }

    private void initGridFinder() {
        GridFinderOptions opt = new GridFinderOptions();
        opt.allowDiagonal = true;
        opt.dontCrossCorners = false;
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

        this.navigationGrid = new NavigationGrid<>(cells);
    }

    private List<GridCell> optimizePath(List<GridCell> path) {
        List<GridCell> cellsToRemove = new ArrayList<>();

        for(int i = 0; i < path.size() - 2; i++) {
            GridCell currentCell = path.get(i);
            GridCell nextCell = path.get(i + 1);
            GridCell afterNextCell = path.get(i + 2);

            if((afterNextCell.x == currentCell.x + 1 && afterNextCell.y == currentCell.y + 2) ||
               (afterNextCell.x == currentCell.x     && afterNextCell.y == currentCell.y + 2) ||
               (afterNextCell.x == currentCell.x - 1 && afterNextCell.y == currentCell.y - 2) ||
               (afterNextCell.x == currentCell.x     && afterNextCell.y == currentCell.y - 2)) {
                cellsToRemove.add(nextCell);
                i++;
            }
        }

        if(!cellsToRemove.isEmpty()) {
            log.debug("Path before optimization: {}", path);
            path.removeAll(cellsToRemove);
            log.debug("Path after optimization: {}", path);
        }

        return path;
    }


}
