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

    /**
     * Handles animation changes to detect the use of protection prayers.
     * @param event The animation change event.
     */
    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        // Logic to handle animation changes.
        final Actor actor = event.getActor();
        final int anim = actor.getAnimation();

        if (actor.getName() == null || actor instanceof Player){
            return;
        }

        switch(anim) {
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
                sendMessage(actor.getName() + " is playing animation with id " + anim);
                break;
        }

    }

    /**
     * Handles projectile movements to track incoming attacks.
     * @param event The projectile moved event.
     */
    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        // Logic to handle projectile movements.
        final Projectile projectile = event.getProjectile();

        if (projectile.getInteracting().getName() == null) {
            sendMessage("Projectile " + projectile.getId() + " is firing at local point " + projectile.getTarget().toString());
        }

        sendMessage("Projectile " + projectile.getId() + " is firing at " + projectile.getInteracting().getName());

        int id = projectile.getId();

    }

    // CHECK IF ANYTHING IS INTERACTING ON THE CLIENT
    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {

        Actor source = event.getSource();
        Actor target = event.getTarget();

        if (client.getLocalPlayer() == null || target == null || source == null) {
            return;
        }

        // If the local player is the source
        if (source.equals(client.getLocalPlayer())) {
            System.out.println("You interact with " + target.getName() + ".");
            sendMessage("You interact with " + target.getName() + ".");
        } else if (target.equals(client.getLocalPlayer())) { // If the local player is the target
            System.out.println(source.getName() + " is interacting with you.");
            sendMessage(source.getName() + " is interacting  with you.");
        }

    }

    private void sendMessage(String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.BLACK), "");
    }



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