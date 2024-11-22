package com.flump;

import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.awt.*;
import net.runelite.client.util.ColorUtil;

/**
 * Detects combat-related events, such as animations and projectiles, and manages protection prayers.
 */
@Slf4j
@PluginDescriptor(
        name = "!FLUMP - Detect Test",
        description = "Plugin to detect combat animations and projectiles",
        tags = {"combat", "detection"}
)
public class DetectCombatPlugin extends Plugin {

    @Inject
    private Client client;

    @Override
    protected void startUp() throws Exception {
        log.info("DetectCombatPlugin started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("DetectCombatPlugin stopped!");
    }

    /**
     * Handles animation changes to detect the use of protection prayers or other combat animations.
     *
     * @param event The animation change event.
     */
    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        final Actor actor = event.getActor();
        final int anim = actor.getAnimation();

        // Ignore if actor has no name or is a player (since we're focusing on NPCs)
        if (actor.getName() == null || actor instanceof Player) {
            return;
        }

        // Check for specific animation IDs corresponding to protection prayers
        switch (anim) {
            case (2656): // Protect from Magic animation ID
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                        ColorUtil.wrapWithColorTag("Protect MAGE", Color.BLUE), "");
                break;
            case (2652): // Protect from Missiles animation ID
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                        ColorUtil.wrapWithColorTag("Protect RANGED", Color.GREEN), "");
                break;
            case (2655): // Protect from Melee animation ID
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                        ColorUtil.wrapWithColorTag("Protect MELEE", Color.RED), "");
                break;
            case (-1): // Animation reset or no animation
                break;
            default:
                sendMessage(actor.getName() + " is playing animation with id " + anim);
                break;
        }
    }

    /**
     * Handles projectile movements to track incoming attacks and provide warnings.
     *
     * @param event The projectile moved event.
     */
    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        final Projectile projectile = event.getProjectile();

        // Check if the projectile is targeting an entity
        if (projectile.getInteracting() != null && projectile.getInteracting().getName() != null) {
            sendMessage("Projectile " + projectile.getId() + " is firing at " + projectile.getInteracting().getName());
        } else {
            sendMessage("Projectile " + projectile.getId() + " is firing at location " + projectile.getTarget().toString());
        }

        // Additional logic can be added here based on projectile ID or type
    }

    /**
     * Handles changes in interactions to detect when entities interact with each other.
     *
     * @param event The interacting changed event.
     */
    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {

        Actor source = event.getSource();
        Actor target = event.getTarget();

        if (client.getLocalPlayer() == null || target == null || source == null) {
            return;
        }

        // If the local player is the source (we are interacting with something)
        if (source.equals(client.getLocalPlayer())) {
            System.out.println("You interact with " + target.getName() + ".");
            sendMessage("You interact with " + target.getName() + ".");
        }
        // If the local player is the target (something is interacting with us)
        else if (target.equals(client.getLocalPlayer())) {
            System.out.println(source.getName() + " is interacting with you.");
            sendMessage(source.getName() + " is interacting with you.");
        }
    }

    /**
     * Sends a game message with black text color.
     *
     * @param message The message to send.
     */
    private void sendMessage(String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                ColorUtil.wrapWithColorTag(message, Color.BLACK), "");
    }
}
