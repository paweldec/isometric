package com.jteam.isometric.core.debug;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DebugConfig {
    boolean mapCords;
    boolean collisionLayer;

    private static DebugConfig instance;

    public static DebugConfig getInstance() {
        if(instance == null) instance = new DebugConfig();
        return instance;
    }
}
