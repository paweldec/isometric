package com.jteam.isometric.core.animation;

import lombok.Data;

import java.util.List;

@Data
public class AnimationDef {
    private String image;
    private int renderSizeWidth;
    private int renderSizeHeight;
    private int renderOffsetX;
    private int renderOffsetY;
    private List<Animation> animations;
}