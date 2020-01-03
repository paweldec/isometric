package com.jteam.isometric.core.util.math;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
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
}
