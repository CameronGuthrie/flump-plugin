package com.flump;

import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.events.InteractingChanged;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages interactions between the local player and other entities, providing feedback in the game chat.
 */
@Singleton
public class InteractionManager {
    private final Client client;
    private final MessageManager messageManager;

    /**
     * Constructor for InteractionManager.
     *
     * @param client         The game client instance.
     * @param messageManager The manager for sending messages to the game chat.
     */
    @Inject
    public InteractionManager(Client client, MessageManager messageManager) {
        this.client = client;
        this.messageManager = messageManager;
    }

    /**
     * Calculates the center point of an NPC on the canvas, useful for targeting.
     *
     * @param target The actor (NPC) to calculate the center for.
     * @return The center point of the NPC on the canvas.
     */
    private Point getNpcCenter(Actor target) {
        return new Point(
                (int) target.getCanvasTilePoly().getBounds2D().getCenterX(),
                (int) target.getCanvasTilePoly().getBounds2D().getCenterY()
        );
    }

    /**
     * Processes interaction events and provides feedback.
     *
     * @param event The InteractingChanged event.
     */
    public void interact(InteractingChanged event) {

        Actor source = event.getSource();
        Actor target = event.getTarget();

        if (client.getLocalPlayer() == null || target == null || source == null) {
            return;
        }

        // If the local player is the source (we are interacting with something)
        if (source.equals(client.getLocalPlayer())) {
            messageManager.sendMessage(client, "You interact with " + target.getName() + ".");
            messageManager.sendMessage(client, target.getName() + " center: " + getNpcCenter(target));
        }
        // If the local player is the target (something is interacting with us)
        else if (target.equals(client.getLocalPlayer())) {
            messageManager.sendMessage(client, source.getName() + " is interacting with you.");
        }
    }
}
