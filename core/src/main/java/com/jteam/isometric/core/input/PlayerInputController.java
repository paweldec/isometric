package com.jteam.isometric.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jteam.isometric.core.movement.MovementController;
import com.jteam.isometric.core.util.MouseMath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PlayerInputController {

    private final MovementController movementController;
    private final Viewport viewport;

    public void update() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            moveToMouseCord();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            printMouseWorldPosition();
        }
    }

    private void moveToMouseCord() {
        log.debug("Move to mouse cord");
        Vector2 mouseWorldCord = new Vector2();
        MouseMath.getWorldCord(viewport, mouseWorldCord);
        movementController.moveToCord(mouseWorldCord);
    }

    private void printMouseWorldPosition() {
        Vector2 mouseWorldPosition = new Vector2();
        MouseMath.getWorldPosition(viewport, mouseWorldPosition);
        log.debug("Mouse world position: {}", mouseWorldPosition);
    }

}
