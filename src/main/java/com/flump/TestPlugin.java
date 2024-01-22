package com.flump;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.input.MouseManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@PluginDescriptor(
        name = "! Test Plugin",
        description = "A plugin that is testing mouse event listeners",
        tags = {"", ""}
)
@SuppressWarnings("unused")
public class TestPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private MouseManager mouseManager;

    @Inject
    private MouseController mouseController;

    @Inject
    private MouseControllerOverlay mouseOverlay;

    @Inject
    private OverlayManager overlayManager;

    @Getter
    @Setter
    private Point moveMe;

    private Timer timer;


    @Override
    protected void startUp() {

        overlayManager.add(mouseOverlay);
        mouseManager.registerMouseListener(0, mouseController);

        System.out.println("TestPlugin using MouseController: " + mouseController.hashCode());

        log.info("Custom plugin started!");

        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                for (NPC npc : client.getNpcs()) {
                    if (npc != null && npc.getId() == 306) {
                        System.out.println("MoveMe = " + Perspective.localToCanvas(client, new LocalPoint(npc.getLocalLocation().getX() - 15, npc.getLocalLocation().getY()), npc.getWorldLocation().getPlane()));
                        setMoveMe(Perspective.localToCanvas(client, new LocalPoint(npc.getLocalLocation().getX() - 15, npc.getLocalLocation().getY()), npc.getWorldLocation().getPlane()));
                    }
                }
                mouseController.move(getMoveMe());
                mouseController.leftClick();
            }
        };
        timer.schedule(task, 20000);
    }

    @Override
    protected void shutDown() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        overlayManager.remove(mouseOverlay);
        mouseManager.unregisterMouseListener(mouseController);
        log.info("Custom plugin stopped!");
    }

}
