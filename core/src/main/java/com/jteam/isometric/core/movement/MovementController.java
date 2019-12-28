package com.jteam.isometric.core.movement;

import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.creature.Creature;
import com.jteam.isometric.core.path.PathFinder;
import com.jteam.isometric.core.util.CordMath;
import lombok.extern.slf4j.Slf4j;
import org.xguzm.pathfinding.grid.GridCell;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class MovementController {

    private final Creature creature;
    private final PathFinder pathFinder;
    private final Vector2 targetPosition;

    private boolean moving;
    private Queue<GridCell> path;

    private static final float MOVE_SPEED = 0.5f;

    public MovementController(Creature creature, PathFinder pathFinder) {
        this.pathFinder = pathFinder;
        this.creature = creature;
        this.targetPosition = new Vector2(creature.getPosition());
        this.moving = false;
        this.path = new LinkedList<>();
    }

    public void moveToCord(Vector2 targetCord) {
        Vector2 currentCord = new Vector2();
        CordMath.positionToCord(creature.getPosition(), currentCord);
        path.clear();
        path.addAll(pathFinder.find(currentCord, targetCord));

        log.debug("Moving creature from [{},{}] to [{},{}]", currentCord.x, currentCord.y, targetCord.x, targetCord.y);
        log.debug("Generated path: {}", path);

        if(!path.isEmpty()) {
            moving = true;
        }
    }

    public void update() {
        if(!moving) {
            return;
        }

        GridCell pathPoint = path.peek();

        if(pathPoint == null) {
            moving = false;
            return;
        }

        Vector2 targetCord = new Vector2(pathPoint.x, pathPoint.y);

        CordMath.cordToPosition(targetCord, targetPosition, true);

        Vector2 currentPosition = creature.getPosition();
        Vector2 direction = new Vector2(targetPosition).sub(currentPosition).nor();

        currentPosition.x += direction.x * MOVE_SPEED;
        currentPosition.y += direction.y * MOVE_SPEED;

        if(currentPosition.dst(targetPosition) < 0.3f) {
            path.poll();

            if(path.isEmpty()) {
                moving = false;
            }
        }

    }
}
