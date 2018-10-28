package com.jteam.isometric.core.animation;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jteam.isometric.core.renderer.Renderer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AnimationController {
    private final Renderer renderer;
    private final Animation animation;
    private OrthographicCamera camera;
    private AnimationState lastState;
    private AnimationState currentState;
    private AnimationDirection direction;
    private StopWatch elapsedTime;
    private int currentFrame;

    public AnimationController(Renderer renderer, Animation animation) {
        this.renderer = renderer;
        this.animation = animation;
        this.lastState = getFirstState();
        this.currentState = getFirstState();
        this.elapsedTime = new StopWatch();
        this.elapsedTime.start();
        this.direction = AnimationDirection.W;
    }

    public void setState(String stateName) {
        lastState = currentState;
        currentState = animation.getStates().stream()
                .filter(animationState -> animationState.getName().equals(stateName))
                .peek(animationState -> this.resetAnimation())
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("State %s not found in animation %s",
                        stateName, animation.getTexturePath())));
    }

    public void setDirection(AnimationDirection direction) {
        this.direction = direction;
    }

    public void update() {
        if(currentState == null) return;
        if(elapsedTime.getTime(TimeUnit.MILLISECONDS) < currentState.getDuration()) return;

        if(currentFrame < currentState.getFrames() - 1) {
            currentFrame++;
        } else {
            switch(currentState.getType()) {
                case PLAY_ONCE: break;
                case LOOPED: currentFrame = 0; break;
                case BACK_FORTH: {
                    currentFrame = 0;
                    currentState = lastState;
                    break;
                }
            }
        }

        elapsedTime.reset();
        elapsedTime.start();
    }

    public void setView(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void render() {
        renderer.draw(getTextureRegion(), 0 - camera.position.x, 0 - camera.position.y);
    }

    private void resetAnimation() {
        currentFrame = 0;
    }

    private TextureRegion getTextureRegion() {
        if(currentState == null || animation == null || animation.getTexture() == null) return null;

        int totalFramesInOneLine = animation.getTexture().getWidth() / animation.getRenderSizeWidth();
        int currentFramePosition = currentState.getPosition() + currentFrame;
        int currentFrameLine = direction.getCode();
        int currentFrameInLine = currentFramePosition % totalFramesInOneLine;

        int x = animation.getRenderSizeWidth() * currentFrameInLine;
        int y = animation.getRenderSizeHeight() * currentFrameLine;

        return new TextureRegion(animation.getTexture(), x, y, animation.getRenderSizeWidth(), animation.getRenderSizeHeight());
    }

    private AnimationState getFirstState() {
        return animation.getStates().get(0);
    }

}
