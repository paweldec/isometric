package com.jteam.isometric.core.creature;

import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.animation.Animation;
import com.jteam.isometric.core.util.math.Direction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Creature {
    private final Vector2 position;
    private final Animation animation;
    private boolean isMoving;
    private Direction facingDir;
}
