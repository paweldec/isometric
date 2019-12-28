package com.jteam.isometric.core.renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class DrawCall {
    private final TextureRegion textureRegion;
    private final String text;
    private final DrawCallType type;
    private final float positionX;
    private final float positionY;
}
