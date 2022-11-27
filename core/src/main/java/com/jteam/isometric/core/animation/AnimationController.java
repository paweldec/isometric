package com.jteam.isometric.core.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jteam.isometric.core.creature.Creature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Slf4j
public class AnimationController {
    private final Creature creature;
    private AnimationState lastState;
    private AnimationState currentState;
    private StopWatch elapsedTime;
    private int currentFrame;

    public AnimationController(Creature creature) {
        this.creature = creature;
        this.lastState = getFirstState();
        this.currentState = getFirstState();
        this.elapsedTime = new StopWatch();
        this.elapsedTime.start();
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

        updateFrame();

        if(creature.isMoving()) {
            setState("run");
        } else {
            setState("stance");
        }

        elapsedTime.reset();
        elapsedTime.start();
    }

    private void setState(String stateName) {
        if(currentState.getName().equals(stateName)) return;

        lastState = currentState;
        currentState = creature.getAnimation().getStates().stream()
                .filter(animationState -> animationState.getName().equals(stateName))
                .peek(animationState -> this.resetAnimation())
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("State %s not found in animation %s",
                        stateName, creature.getAnimation().getTexturePath())));
    }

    private void resetAnimation() {
        currentFrame = 0;
    }

    protected void updateFrame() {
        Animation animation = creature.getAnimation();
        animation.setFrames(getTextureRegions(animation));
    }

    private List<TextureRegion> getTextureRegions(Animation animation) {
        if(currentState == null || animation == null || animation.getTextures() == null || animation.getTextures().isEmpty()) return null;

        int totalFramesInOneLine = animation.getTextures().get(0).getWidth() / animation.getRenderSizeWidth();
        int currentFramePosition = currentState.getPosition() + currentFrame;
        int currentFrameLine = creature.getFacingDir().getCode();
        int currentFrameInLine = currentFramePosition % totalFramesInOneLine;

        int x = animation.getRenderSizeWidth() * currentFrameInLine;
        int y = animation.getRenderSizeHeight() * currentFrameLine;

        return animation.getTextures().stream()
            .map(texture -> new TextureRegion(texture, x, y, animation.getRenderSizeWidth(), animation.getRenderSizeHeight()))
            .collect(toList());
    }

    protected AnimationState getFirstState() {
        return creature.getAnimation().getStates().get(0);
    }

}
