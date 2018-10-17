package com.jteam.isometric.core.animation;

import lombok.Data;

@Data
class Animation {
    private String name;
    private int position;
    private int frames;
    private long duration;
    private AnimationType type;
}