package com.jteam.isometric.core.path;

import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.util.CordMath;
import org.xguzm.pathfinding.Heuristic;
import org.xguzm.pathfinding.NavigationNode;
import org.xguzm.pathfinding.grid.GridCell;

public class StaggeredIsometricHeuristic implements Heuristic {

    private final Vector2 currentCord = new Vector2();
    private final Vector2 targetCord = new Vector2();
    private final Vector2 currentPosition = new Vector2();
    private final Vector2 targetPosition = new Vector2();

    @Override
    public float calculate(NavigationNode currentNavigationNode, NavigationNode targetNavigationNode) {
        GridCell currentCell = (GridCell)currentNavigationNode;
        GridCell targetCell = (GridCell)targetNavigationNode;

        currentCord.x = currentCell.getX();
        currentCord.y = currentCell.getY();

        targetCord.x = targetCell.getX();
        targetCord.y = targetCell.getY();

        CordMath.cordToPosition(currentCord, currentPosition, false);
        CordMath.cordToPosition(targetCord, targetPosition, false);

        return currentPosition.dst(targetPosition);
    }


}
