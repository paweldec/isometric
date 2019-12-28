package com.jteam.isometric.core.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Data;

import java.util.List;

@Data
public class Animation {
    private Texture texture;
    private String texturePath;
    private int renderSizeWidth;
    private int renderSizeHeight;
    private int renderOffsetX;
    private int renderOffsetY;
    private List<AnimationState> states;
    private TextureRegion frame;
}
