package com.jteam.isometric.core.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private SpriteBatch batch;
    private List<DrawCall> drawCalls;

    public Renderer() {
        batch = new SpriteBatch();
        drawCalls = new ArrayList<>();
    }

    public void draw(TextureRegion textureRegion, float positionX, float positionY) {
        if(textureRegion == null) return;
        
        drawCalls.add(DrawCall.builder()
                .textureRegion(textureRegion)
                .positionX(positionX)
                .positionY(positionY)
                .build());
    }

    public void render() {
        batch.begin();
        drawCalls.forEach(this::drawCall);
        drawCalls.clear();
        batch.end();
    }

    private void drawCall(DrawCall drawCall) {
        batch.draw(drawCall.getTextureRegion(), drawCall.getPositionX(), drawCall.getPositionY());
    }

}
