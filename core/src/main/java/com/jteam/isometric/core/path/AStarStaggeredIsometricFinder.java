package com.jteam.isometric.core.path;

import com.badlogic.gdx.math.Vector2;
import org.xguzm.pathfinding.finders.AStarFinder;
import org.xguzm.pathfinding.grid.NavigationGridGraph;
import org.xguzm.pathfinding.grid.NavigationGridGraphNode;

import java.util.List;

import static com.jteam.isometric.core.util.TileConst.TILE_HEIGHT;
import static com.jteam.isometric.core.util.TileConst.TILE_WIDTH;

public class AStarStaggeredIsometricFinder<T extends NavigationGridGraphNode> extends AStarFinder<T> {

    public AStarStaggeredIsometricFinder(Class<T> clazz) {
        super(clazz, StaggeredIsometricFinderOptions.builder()
            .diagonalAllowed(true)
            .crossNotWalkableCornersAllowed(true)
            .normalMovementCost(TILE_WIDTH)
            .diagonalMovementCost(TILE_HEIGHT)
            .heuristic(new StaggeredIsometricHeuristic())
            .build());
    }

    public AStarStaggeredIsometricFinder(Class<T> clazz, StaggeredIsometricFinderOptions options) {
        super(clazz, options);
    }

    public List<T> findPath(Vector2 startCord, Vector2 endCord, NavigationGridGraph<T> grid) {
        return this.findPath(grid.getCell((int)startCord.x, (int)startCord.y), grid.getCell((int)endCord.x, (int)endCord.y), grid);
    }

}
