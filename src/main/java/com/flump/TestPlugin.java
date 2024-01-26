package com.flump;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.input.MouseManager;
import javax.inject.Inject;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private ClientThread clientThread;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private KeyManager keyManger;

    @Inject
    private  KeyboardController keyboardController;

    // this is for waiting until an object is not null
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Inject
    private MouseManager mouseManager;

    @Inject
    private MouseController mouseController;

    @Inject
    private MouseControllerOverlay mouseOverlay;

    @Inject
    private ItemManager itemManager;

    @Inject
    private OverlayManager overlayManager;

    @Getter
    @Setter
    private Point moveMe;

    private Timer timer;

    ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    protected void startUp() {

        overlayManager.add(mouseOverlay);
        mouseManager.registerMouseListener(0, mouseController);
        keyManger.registerKeyListener(keyboardController);

        System.out.println("TestPlugin using MouseController: " + mouseController.hashCode());

        log.info("Custom plugin started!");

    }

    @Override
    protected void shutDown() {

        executorService.shutdown();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        overlayManager.remove(mouseOverlay);
        mouseManager.unregisterMouseListener(mouseController);
        log.info("Custom plugin stopped!");
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getItemContainer() != client.getItemContainer(InventoryID.INVENTORY)) {
            return;
        }

        executorService.submit(() -> {
            inventoryManager.scanInventory();
            inventoryManager.randomInventoryLocations();
            // Update the GUI using SwingUtilities.invokeLater if needed
        });

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event){
        if (event.getGameState().equals(GameState.LOGIN_SCREEN)) {
            System.out.println("\n ON LOGIN SCREEN ! \n");
        }

        if (event.getGameState().equals(GameState.LOGGED_IN)){

            scheduler.scheduleAtFixedRate(() -> {
                Widget clickToPlay = client.getWidget(24772681);
                if (clickToPlay != null) {
                    System.out.println("Click to play is not null!");

                    // Widget is found, perform the action
                    MathStuff mathStuff = new MathStuff();
                    mouseController.move(mathStuff.randomRectanglePoint(clickToPlay.getBounds()));
                    mouseController.leftClick();

                    // Stop further scheduling
                    //scheduler.shutdown();
                }
            }, 0, 651, TimeUnit.MILLISECONDS); // Check every 500 milliseconds



            scheduler.scheduleAtFixedRate(() -> {
                Widget inventoryIcon = client.getWidget(ComponentID.FIXED_VIEWPORT_INVENTORY_ICON);
                if (inventoryIcon != null) {

                    inventoryManager.scanInventory();
                    inventoryManager.randomInventoryLocations();
                    System.out.println("updated inventory position");
                    // Stop further scheduling
                    scheduler.shutdown();
                }
            }, 0, 320, TimeUnit.MILLISECONDS); // Check every 500 milliseconds

        }

    }


}
