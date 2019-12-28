package com.jteam.isometric.core.creature;

import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.animation.Animation;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Creature {
    private final Vector2 position;
    private final Animation animation;
}
