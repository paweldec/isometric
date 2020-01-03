package com.jteam.isometric.core.debug;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugInput extends InputAdapter {

    private final DebugConfig debugConfig;

    public DebugInput() {
        debugConfig = DebugConfig.getInstance();
    }

    public boolean keyDown(int keyCode) {
        if(keyCode == Input.Keys.NUM_1) {
            switchMapCords();
            return true;
        } else if(keyCode == Input.Keys.NUM_2) {
            switchCollisionLayer();
        }

        return false;
    }

    private void switchMapCords() {
        log.debug("Switch map cords");
        debugConfig.setMapCords(!debugConfig.isMapCords());
    }

    private void switchCollisionLayer() {
        log.debug("Switch collision layer");
        debugConfig.setCollisionLayer(!debugConfig.isCollisionLayer());
    }
}
