package com.flump;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

/**
 * Configuration interface for the Flump plugin, defining user-adjustable settings.
 */
@ConfigGroup("test")
public interface TestConfig extends Config {

    /**
     * Enables or disables automatic login when logged out.
     *
     * @return True if auto login is enabled.
     */
    @ConfigItem(
            position = 1,
            keyName = "autoLogin",
            name = "Auto Login",
            description = "Will attempt to log back in if logged out"
    )
    default boolean autoLogin() {
        return false;
    }

    /**
     * Enables or disables the display of camera information overlay.
     *
     * @return True if camera info overlay is enabled.
     */
    @ConfigItem(
            position = 2,
            keyName = "cameraInfo",
            name = "Camera Info",
            description = "Camera positional information"
    )
    default boolean cameraInfo() {
        return false;
    }

    /**
     * Enables or disables the display of player position overlay.
     *
     * @return True if player position overlay is enabled.
     */
    @ConfigItem(
            position = 3,
            keyName = "playerPosition",
            name = "Player Position",
            description = "Player positional information"
    )
    default boolean playerPosition() {
        return false;
    }

    /**
     * Specifies the NPC ID to interact with (functionality placeholder).
     *
     * @return The NPC ID as a string.
     */
    @ConfigItem(
            position = 4,
            keyName = "killNPC",
            name = "Kill NPC ID",
            description = "ID of the NPC to interact with"
    )
    default String killNPC() {
        return "Hello!";
    }
}
