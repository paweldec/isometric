package com.jteam.isometric.core.util.math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MouseMath {

    private static Vector3 mouseScreenPosition = new Vector3();
    private static Vector2 mouseWorldPosition = new Vector2();

    public static void getWorldPosition(Viewport viewport, Vector2 mouseWorldPosition) {
        mouseScreenPosition.x = Gdx.input.getX();
        mouseScreenPosition.y = Gdx.input.getY();
        mouseScreenPosition.z = 0;
        mouseScreenPosition = viewport.unproject(mouseScreenPosition);
        mouseWorldPosition.x = mouseScreenPosition.x + (viewport.getWorldWidth() / 2);
        mouseWorldPosition.y = mouseScreenPosition.y + (viewport.getWorldHeight() / 2);
    }

    public static void getWorldCord(Vector2 mouseWorldPosition, Vector2 mouseWorldCord) {
        CordMath.positionToCord(mouseWorldPosition, mouseWorldCord);
    }

    public static void getWorldCord(Viewport viewport, Vector2 mouseWorldCord) {
        getWorldPosition(viewport, mouseWorldPosition);
        CordMath.positionToCord(mouseWorldPosition, mouseWorldCord);
    }

}
