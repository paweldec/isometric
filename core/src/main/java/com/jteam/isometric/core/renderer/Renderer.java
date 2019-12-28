package com.jteam.isometric.core.renderer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Renderer {

    private SpriteBatch batch;
    private BitmapFont font;
    private List<DrawCall> drawCalls;

    public Renderer() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        drawCalls = new ArrayList<>();
    }

    public void draw(TextureRegion textureRegion, float positionX, float positionY) {
        if(textureRegion == null) return;

        drawCalls.add(DrawCall.builder()
                .textureRegion(textureRegion)
                .type(DrawCallType.TEXTURE)
                .positionX(positionX)
                .positionY(positionY)
                .build());
    }

    public void drawText(String text, float positionX, float positionY) {
        if(text == null) return;

        drawCalls.add(DrawCall.builder()
                .text(text)
                .type(DrawCallType.TEXT)
                .positionX(positionX)
                .positionY(positionY)
                .build());
    }

    public void render() {
        batch.begin();
        drawCalls.stream()
            .sorted(Comparator.comparing(DrawCall::getType))
            .forEach(this::drawCall);
        drawCalls.clear();
        batch.end();
    }

    private void drawCall(DrawCall drawCall) {
        switch (drawCall.getType()) {
            case TEXTURE: batch.draw(drawCall.getTextureRegion(), drawCall.getPositionX(), drawCall.getPositionY()); break;
            case TEXT: font.draw(batch, drawCall.getText(), drawCall.getPositionX(), drawCall.getPositionY()); break;
        }
    }

}
