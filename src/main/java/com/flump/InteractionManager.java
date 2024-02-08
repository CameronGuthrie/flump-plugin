package com.flump;

import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.events.InteractingChanged;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InteractionManager {
    private final Client client;
    private final MessageManager messageManager;

    @Inject
    public InteractionManager(Client client, MessageManager messageManager) {
        this.client = client;
        this.messageManager = messageManager;
    }

    private Point getNpcCenter(Actor target) {
        return new Point((int)target.getCanvasTilePoly().getBounds2D().getCenterX(), (int)target.getCanvasTilePoly().getBounds2D().getCenterY());
    }

    public void interact (InteractingChanged event) {

        Actor source = event.getSource();
        Actor target = event.getTarget();

        /*
        if (target == null || source == null) {
            return;
        }

        messageManager.sendMessage(client, source.getName() + " interacts with " + target.getName());
        */

        if (client.getLocalPlayer() == null || target == null || source == null) {
            return;
        }

        // If the local player is the source
        if (source.equals(client.getLocalPlayer())) {
            System.out.println("You interact with " + target.getName() + ".");
            messageManager.sendMessage(client,"You interact with " + target.getName() + ".");
            System.out.println(target.getName() + " center: " + getNpcCenter(target));
            messageManager.sendMessage(client,target.getName() + " center: " + getNpcCenter(target));
        } else if (target.equals(client.getLocalPlayer())) { // If the local player is the target
            System.out.println(source.getName() + " is interacting with you.");
            messageManager.sendMessage(client,source.getName() + " is interacting  with you.");
        }
    }
}
