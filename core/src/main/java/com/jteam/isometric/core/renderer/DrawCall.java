package com.jteam.isometric.core.renderer;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class DrawCall<T> {
    private final T draw;
    private final float positionX;
    private final float positionY;
    private final float order;
}
