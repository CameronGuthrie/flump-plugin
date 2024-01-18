package com.flump;

import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.awt.Graphics2D;

import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import net.runelite.client.util.ColorUtil;


@Slf4j
@PluginDescriptor(
        name = "!FLUMP - Detect test",
        description = "",
        tags = {"", ""}
)
@SuppressWarnings("unused")
public class DetectCombatPlugin extends Plugin {


    @Inject
    private Client client;

    @Override
    protected void startUp() throws Exception {
        log.info("Custom plugin started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Custom plugin stopped!");
    }

    // Swap prayers on game tick
//    @Subscribe
//    public void onGameTick(GameTick event) {
//        if (swapMage) {
//            clickPrayer(Prayer.PROTECT_FROM_MAGIC);
//            swapMage = false;
//        } else if (swapRange) {
//            clickPrayer(Prayer.PROTECT_FROM_MISSILES);
//            swapRange = false;
//        } else if (swapMelee) {
//            clickPrayer(Prayer.PROTECT_FROM_MELEE);
//            swapMelee = false;
//        }
//    }

//    private void clickPrayer(Prayer prayer) {
//        if (client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) < 1) {
//            return;
//        }
//    }

    // check for animation
    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {

        if (event.getActor().getName() == null || event.getActor() instanceof Player){
            return;
        }

        int animID = event.getActor().getAnimation();
        switch(animID) {
            case (2656):
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Protect MAGE", Color.BLUE), "");
                break;
            case (2652):
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Protect RANGED", Color.GREEN), "");
                break;
            case (2655):
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Protect MELEE", Color.RED), "");
                break;
            case (-1):
                break;
            default:
                sendMessage(event.getActor().getName() + " is playing animation with id " + event.getActor().getAnimation());
                break;
        }
    }

    // Check for projectiles (fallback if animation check fails)
    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {

        if (event.getProjectile().getInteracting().getName() == null) {
            sendMessage("Projectile " + event.getProjectile().getId() + " is firing at local point " + event.getProjectile().getTarget().toString());
        }

        sendMessage("Projectile " + event.getProjectile().getId() + " is firing at " + event.getProjectile().getInteracting().getName());

        int id = event.getProjectile().getId();

//        switch (id) {
//            case (1339):
//                if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC)) {
//                    swapMage = true;
//                }
//                break;
//            case (1340):
//                if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES)) {
//                    swapRange = true;
//                }
//                break;
//            default:
//                break;
//        }
    }

    // CHECK IF ANYTHING IS INTERACTING ON THE CLIENT
    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {

        // If the local player is the source
        if (event.getSource().equals(client.getLocalPlayer())) {

            System.out.println(event.getSource().getName() + " is interacting with " + event.getTarget().getName());
            sendMessage(event.getSource().getName() + " is interacting with " + event.getTarget().getName());

        } else if (event.getTarget().equals(client.getLocalPlayer())) { // If the local player is the target

            System.out.println(event.getSource().getName() + " is interacting with " + event.getTarget().getName());
            sendMessage(event.getSource().getName() + " is interacting with " + event.getTarget().getName());

        }

    }

//    public int getMillis() {
//           return (int) (Math.random() * config.randLow() + config.randHigh());
//    }

//    // DRAW A CROSS OVER THE MOUSE
//    public void paintMouse(Graphics2D graphics) {
//        int canvasW = client.getCanvasWidth();
//        int canvasH = client.getCanvasHeight();
//        int baseX = client.getBaseX();
//        int baseY = client.getBaseY();
//        int mouseX = client.getMouseCanvasPosition().getX();
//        int mouseY = client.getMouseCanvasPosition().getY();
//        graphics.draw(new Rectangle(baseX, mouseY, canvasW, 1));
//        graphics.draw(new Rectangle(mouseX, baseY, 1, canvasH));
//    }

    private void sendMessage(String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.BLACK), "");
    }



}
