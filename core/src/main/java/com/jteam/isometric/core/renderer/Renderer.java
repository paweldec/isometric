package com.jteam.isometric.core.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jteam.isometric.core.shape.Line;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;

public class Renderer {

    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;

    private List<DrawCall<TextureRegion>> drawCallsTexture;
    private List<DrawCall<String>> drawCallsText;
    private List<DrawCall<Line>> drawCallsLine;

    public Renderer() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        shape.setColor(Color.RED);
        font = new BitmapFont();

        drawCallsTexture = new ArrayList<>();
        drawCallsText = new ArrayList<>();
        drawCallsLine = new ArrayList<>();
    }

    public void draw(TextureRegion textureRegion, float positionX, float positionY) {
        if(textureRegion == null) return;
        drawCallsTexture.add(buildDrawCall(textureRegion, positionX, positionY, -positionY));
    }

    public void drawText(String text, float positionX, float positionY) {
        if(text == null) return;
        drawCallsText.add(buildDrawCall(text, positionX, positionY, drawCallsText.size()));
    }

    public void drawLine(Line line) {
        if(line == null) return;
        drawCallsLine.add(buildDrawCall(line, 0, 0, drawCallsLine.size()));
    }

    private <T> DrawCall<T> buildDrawCall(T draw, float positionX, float positionY, float order) {
        return DrawCall.<T>builder()
            .draw(draw)
            .positionX(positionX)
            .positionY(positionY)
            .order(order)
            .build();
    }

    public void render() {
        batch.begin();

        drawCallsTexture.stream()
            .sorted(comparing(DrawCall::getOrder))
            .forEach(this::drawTexture);
        drawCallsText.forEach(this::drawText);;
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        drawCallsLine.forEach(this::drawLine);
        shape.end();

        drawCallsTexture.clear();
        drawCallsText.clear();
        drawCallsLine.clear();
    }

    private void drawTexture(DrawCall<TextureRegion> drawCall) {
        batch.draw(drawCall.getDraw(), drawCall.getPositionX(), drawCall.getPositionY());
    }

    private void drawText(DrawCall<String> drawCall) {
        font.draw(batch, drawCall.getDraw(), drawCall.getPositionX(), drawCall.getPositionY());
    }

    private void drawLine(DrawCall<Line> drawCall) {
        shape.line(drawCall.getDraw().getStart(), drawCall.getDraw().getEnd());
    }

}
