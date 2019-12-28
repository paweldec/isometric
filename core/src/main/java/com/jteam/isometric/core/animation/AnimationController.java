package com.jteam.isometric.core.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jteam.isometric.core.creature.Creature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AnimationController {
    private final Creature creature;
    private AnimationState lastState;
    private AnimationState currentState;
    private AnimationDirection direction;
    private StopWatch elapsedTime;
    private int currentFrame;

    public AnimationController(Creature creature) {
        this.creature = creature;
        this.lastState = getFirstState();
        this.currentState = getFirstState();
        this.elapsedTime = new StopWatch();
        this.elapsedTime.start();
        this.direction = AnimationDirection.W;
    }

    public void setState(String stateName) {
        lastState = currentState;
        currentState = creature.getAnimation().getStates().stream()
                .filter(animationState -> animationState.getName().equals(stateName))
                .peek(animationState -> this.resetAnimation())
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("State %s not found in animation %s",
                        stateName, creature.getAnimation().getTexturePath())));
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

        creature.getAnimation().setFrame(getTextureRegion());

        elapsedTime.reset();
        elapsedTime.start();
    }

    private void resetAnimation() {
        currentFrame = 0;
    }

    public TextureRegion getTextureRegion() {
        final Animation animation = creature.getAnimation();

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
        return creature.getAnimation().getStates().get(0);
    }

}
