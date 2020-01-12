package com.jteam.isometric.core.movement;

import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.creature.Creature;
import com.jteam.isometric.core.path.PathFinder;
import com.jteam.isometric.core.util.math.CordMath;
import com.jteam.isometric.core.util.math.Direction;
import lombok.extern.slf4j.Slf4j;
import org.xguzm.pathfinding.grid.GridCell;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class MovementController {

    private final Creature creature;
    private final PathFinder pathFinder;

    private final Vector2 currentCord;
    private final Vector2 targetCord;
    private final Vector2 targetPosition;
    private final Vector2 direction;

    private Queue<GridCell> path;

    private static final float MOVE_SPEED = 1.0f;

    public MovementController(Creature creature, PathFinder pathFinder) {
        this.creature = creature;
        this.pathFinder = pathFinder;
        this.path = new LinkedList<>();

        this.currentCord = new Vector2();
        this.targetCord = new Vector2();
        this.targetPosition = new Vector2(creature.getPosition());
        this.direction = new Vector2();
    }

    public void moveToCord(Vector2 targetCord) {
        Vector2 currentCord = new Vector2();
        CordMath.positionToCord(creature.getPosition(), currentCord);
        path.clear();
        path.addAll(pathFinder.find(currentCord, targetCord));

        log.debug("Moving creature from {} to {}", currentCord, targetCord);
        log.debug("Generated path: {}", path);

        if(!path.isEmpty()) {
            creature.setMoving(true);
        }
    }

    public void update() {
        if(!creature.isMoving()) {
            return;
        }

        GridCell pathPoint = path.peek();

        if(pathPoint == null) {
            creature.setMoving(false);
            return;
        }

        targetCord.set(pathPoint.getX(), pathPoint.getY());

        CordMath.cordToPosition(targetCord, targetPosition);
        CordMath.positionToCord(creature.getPosition(), currentCord);

        Vector2 currentPosition = creature.getPosition();

        direction.set(targetPosition);
        direction.set(direction.sub(currentPosition).nor());

        currentPosition.x += direction.x * MOVE_SPEED;
        currentPosition.y += direction.y * MOVE_SPEED;

        creature.setFacingDir(Direction.fromVector(direction));

        if(currentPosition.dst(targetPosition) < MOVE_SPEED + 0.1f) {
            path.poll();

            if(path.isEmpty()) {
                creature.setMoving(false);
            }
        }

    }
}
