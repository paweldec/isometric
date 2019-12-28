package com.jteam.isometric.core.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.jteam.isometric.core.animation.Animation;
import com.jteam.isometric.core.creature.Creature;
import com.jteam.isometric.core.renderer.Renderer;

public class RenderController {

    private final Creature creature;
    private final Renderer renderer;
    private final OrthographicCamera camera;

    public RenderController(Creature creature, Renderer renderer, OrthographicCamera camera) {
        this.creature = creature;
        this.renderer = renderer;
        this.camera = camera;
    }

    public void update() {
        final Vector2 position = creature.getPosition();
        final Animation animation = creature.getAnimation();

        renderer.draw(animation.getFrame(),
                position.x - camera.position.x - 64,
                position.y - camera.position.y - 32);
    }

}
