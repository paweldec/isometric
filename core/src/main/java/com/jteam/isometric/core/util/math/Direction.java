package com.jteam.isometric.core.util.math;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
@Slf4j
public enum Direction {
    W(0),
    NW(1),
    N(2),
    NE(3),
    E(4),
    SE(5),
    S(6),
    SW(7);

    private final int code;

    public static Direction fromVector(Vector2 direction) {
        double angle = Math.atan2(-direction.y, direction.x);
        int dirCount = values().length;
        if(angle < 0) angle += 2 * Math.PI;
        int octant  = (int)Math.round(dirCount * angle / (2 * Math.PI));
        if(octant == dirCount) octant = 0;
        octant += dirCount / 2;
        octant &= dirCount - 1;

        int finalOctant = octant;

        return Arrays.stream(values())
            .filter(value -> value.code == finalOctant)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
