package com.jteam.isometric.core.path;

import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.util.math.CordMath;
import com.jteam.isometric.core.util.math.Direction;
import org.xguzm.pathfinding.PathFinderOptions;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.NavigationGridGraphNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class StaggeredIsometricNavigationGrid<T extends NavigationGridGraphNode> extends NavigationGrid<T> {

    private List<T> neighbors;

    public StaggeredIsometricNavigationGrid(T[][] nodes) {
        super(nodes);
        neighbors = new ArrayList<>();
    }

    public List<T> getNeighbors(T node, PathFinderOptions opt) {
        StaggeredIsometricFinderOptions options = (StaggeredIsometricFinderOptions)opt;
        boolean allowDiagonal = options.isDiagonalAllowed();
        boolean dontCrossNotWalkableCorners = options.isCrossNotWalkableCornersAllowed();

        int x = node.getX();
        int y = node.getY();
        this.neighbors.clear();

         AtomicBoolean neWalkable = new AtomicBoolean(false);
         AtomicBoolean seWalkable = new AtomicBoolean(false);
         AtomicBoolean swWalkable = new AtomicBoolean(false);
         AtomicBoolean nwWalkable = new AtomicBoolean(false);

         /*
                 (N+0xN+2)
          (N+0xN+1)     (N+1xN+1)
        (N-1xN+0)   NxN   (N+1xN+0)
          (N+0xN-1)	    (N+1xN-1)
                 (N+0xN-2)
        */
        this.getNeighbor(x, y, Direction.NE).
            ifPresent(neighbor -> { neWalkable.set(true); this.neighbors.add(neighbor); });
        this.getNeighbor(x, y, Direction.SE)
            .ifPresent(neighbor -> { seWalkable.set(true); this.neighbors.add(neighbor); });
        this.getNeighbor(x, y, Direction.SW)
            .ifPresent(neighbor -> { swWalkable.set(true); this.neighbors.add(neighbor); });
        this.getNeighbor(x, y, Direction.NW)
            .ifPresent(neighbor -> { nwWalkable.set(true); this.neighbors.add(neighbor); });

        if(allowDiagonal) {
            this.getNeighbor(x, y, Direction.N)
                .filter(neighbor -> !dontCrossNotWalkableCorners || (neWalkable.get() && nwWalkable.get()))
                .ifPresent(neighbor -> this.neighbors.add(neighbor));
            this.getNeighbor(x, y, Direction.E)
                .filter(neighbor -> !dontCrossNotWalkableCorners || (neWalkable.get() && seWalkable.get()))
                .ifPresent(neighbor -> this.neighbors.add(neighbor));
            this.getNeighbor(x, y, Direction.S)
                .filter(neighbor -> !dontCrossNotWalkableCorners || (seWalkable.get() && swWalkable.get()))
                .ifPresent(neighbor -> this.neighbors.add(neighbor));
            this.getNeighbor(x, y, Direction.W)
                .filter(neighbor -> !dontCrossNotWalkableCorners || (swWalkable.get() && nwWalkable.get()))
                .ifPresent(neighbor -> this.neighbors.add(neighbor));
        }

        return this.neighbors;
    }

    public float getMovementCost(T node1, T node2, PathFinderOptions opt) {
        StaggeredIsometricFinderOptions options = (StaggeredIsometricFinderOptions)opt;

        if (node1 == node2) {
            return 0.f;
        }

        /*
                 (N+0xN+2)
          (N+0xN+1)     (N+1xN+1)
        (N-1xN+0)   NxN   (N+1xN+0)
          (N+0xN-1)	    (N+1xN-1)
                 (N+0xN-2)
        */
        if((node1.getX()     == node2.getX() && node1.getY() + 2 == node2.getY()) ||
           (node1.getX() + 1 == node2.getX() && node1.getY()     == node2.getY()) ||
           (node1.getX()     == node2.getX() && node1.getY() - 2 == node2.getY()) ||
           (node1.getX() - 1 == node2.getX() && node1.getY()     == node2.getY())) {
            return options.getNormalMovementCost();
        }

        return options.getDiagonalMovementCost();
    }

    private Optional<T> getNeighbor(int x, int y, Direction dir) {
        Vector2 currentCord = new Vector2(x, y);
        Vector2 neighborCord = new Vector2();

        return CordMath.getNeighborCord(currentCord, neighborCord, dir)
            .filter(cord -> this.isWalkable((int) cord.x, (int) cord.y))
            .map(cord -> this.nodes[(int)cord.x][(int)cord.y]);
    }

}
