package com.jteam.isometric.core.path;

import lombok.Builder;
import lombok.Getter;
import org.xguzm.pathfinding.PathFinderOptions;

@Getter
public class StaggeredIsometricFinderOptions extends PathFinderOptions {
    private final boolean diagonalAllowed;
    private final boolean crossNotWalkableCornersAllowed;
    private final float normalMovementCost;
    private final float diagonalMovementCost;
    private final StaggeredIsometricHeuristic heuristic;

    @Builder
    public StaggeredIsometricFinderOptions(boolean diagonalAllowed,
                                           boolean crossNotWalkableCornersAllowed,
                                           float normalMovementCost,
                                           float diagonalMovementCost,
                                           StaggeredIsometricHeuristic heuristic) {
        this.diagonalAllowed = diagonalAllowed;
        this.crossNotWalkableCornersAllowed = crossNotWalkableCornersAllowed;
        this.normalMovementCost = normalMovementCost;
        this.diagonalMovementCost = diagonalMovementCost;
        this.heuristic = heuristic;
        super.heuristic = heuristic;
    }
}
