package com.jteam.isometric.core.util.shape;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Line {
    private final Vector2 start;
    private final Vector2 end;
}
