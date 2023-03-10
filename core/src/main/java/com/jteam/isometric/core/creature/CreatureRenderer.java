package com.jteam.isometric.core.creature;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.animation.Animation;
import com.jteam.isometric.core.renderer.Renderer;

public class CreatureRenderer {

    private final Creature creature;
    private final Renderer renderer;
    private final OrthographicCamera camera;

    public CreatureRenderer(Creature creature, Renderer renderer, OrthographicCamera camera) {
        this.creature = creature;
        this.renderer = renderer;
        this.camera = camera;
    }

    public void update() {
        final Vector2 position = creature.getPosition();
        final Animation animation = creature.getAnimation();

        if (animation.getFrames() == null) return;

        float posX = position.x - camera.position.x - (animation.getRenderSizeWidth() - animation.getRenderOffsetX());
        float posY = position.y - camera.position.y - (animation.getRenderSizeHeight() - animation.getRenderOffsetY());

        animation.getFrames().forEach(frame -> renderer.draw(frame, posX, posY));
    }

}
