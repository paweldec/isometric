package com.jteam.isometric.core.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jteam.isometric.core.movement.MovementController;
import com.jteam.isometric.core.util.math.MouseMath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PlayerInput extends InputAdapter {

    private final MovementController movementController;
    private final Viewport viewport;

    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            moveToMouseCord();
            return true;
        }

        return false;
    }

    private void moveToMouseCord() {
        log.debug("Move to mouse cord");
        Vector2 mouseWorldCord = new Vector2();
        MouseMath.getWorldCord(viewport, mouseWorldCord);
        movementController.moveToCord(mouseWorldCord);
    }

}
