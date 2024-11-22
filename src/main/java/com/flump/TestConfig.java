package com.flump;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("test")
public interface TestConfig extends Config {

    /*
    https://www.osrsbox.com/blog/2018/08/18/writing-runelite-plugins-part-3-config/


        @ConfigItem (
            position = integer_value,
            keyName = "string_variable_name",
            name = "string_configuration_display_name",
            description = "string_configuration_mouse_hover_name"
    )
    default data_type string_variable_name() { return default_value; }
     */

    @ConfigItem(
            position = 1,
            keyName = "autoLogin",
            name = "Auto Login",
            description = "Will attempt to log back in if logged out"
    )

    default boolean autoLogin() { return false; }

    @ConfigItem(
            position = 2,
            keyName = "cameraInfo",
            name = "Camera Info",
            description = "Camera positional information"
    )

    default boolean cameraInfo() { return false; }

    @ConfigItem(
            position = 3,
            keyName = "playerPosition",
            name = "Player Position",
            description = "Player positional information"
    )

    default boolean playerPosition() { return false; }

    @ConfigItem(
            position = 4,
            keyName = "killNPC",
            name = "Kill NPC ID",
            description = "kill this npc"
    )

    default String killNPC() { return "Hello!"; }

}
