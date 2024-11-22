package com.flump;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.input.MouseManager;
import javax.inject.Inject;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main plugin class that integrates mouse, keyboard, interaction, and overlay managers.
 */
@Slf4j
@PluginDescriptor(
        name = "Flump Plugin",
        description = "A plugin that provides enhanced interaction and automation features",
        tags = {"automation", "interaction", "overlay"},
        loadWhenOutdated = true,
        enabledByDefault = false
)
public class TestPlugin extends Plugin {

    @Inject
    private TestConfig config;

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private InteractionManager interactionManager;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private KeyManager keyManager;

    @Inject
    private KeyboardController keyboardController;

    // Scheduler for periodic tasks
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Inject
    private MouseManager mouseManager;

    @Inject
    private MouseController mouseController;

    @Inject
    private MouseControllerOverlay mouseOverlay;

    @Inject
    private CameraInfoOverlay cameraInfoOverlay;

    @Inject
    private PlayerPositionOverlay playerPositionOverlay;

    @Inject
    private ItemManager itemManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private CameraController cameraController;

    @Getter
    @Setter
    private Point moveMe;

    private Timer timer;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Sets up the plugin by registering overlays and input listeners.
     */
    @Override
    protected void startUp() {
        // Register overlays
        overlayManager.add(mouseOverlay);
        overlayManager.add(cameraInfoOverlay);
        overlayManager.add(playerPositionOverlay);

        // Register input listeners
        mouseManager.registerMouseListener(0, mouseController);
        keyManager.registerKeyListener(keyboardController);

        log.info("Flump Plugin started!");
    }

    /**
     * Tears down the plugin by unregistering overlays and input listeners.
     */
    @Override
    protected void shutDown() {
        // Shutdown executor services
        executorService.shutdown();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        // Unregister overlays
        overlayManager.remove(mouseOverlay);
        overlayManager.remove(cameraInfoOverlay);
        overlayManager.remove(playerPositionOverlay);

        // Unregister input listeners
        mouseManager.unregisterMouseListener(mouseController);
        keyManager.unregisterKeyListener(keyboardController);

        log.info("Flump Plugin stopped!");
    }

    /**
     * Handles changes in the item container, such as inventory changes.
     *
     * @param event The item container change event.
     */
    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        // Check if the changed container is the player's inventory
        if (event.getItemContainer() != client.getItemContainer(InventoryID.INVENTORY)) {
            return;
        }

        // Update inventory asynchronously
        executorService.submit(() -> {
            inventoryManager.scanInventory();
            inventoryManager.randomInventoryLocations();
        });
    }

    /**
     * Handles changes in the game state, such as logging in or out.
     *
     * @param event The game state change event.
     */
    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (config.autoLogin()) {
            if (event.getGameState() == GameState.LOGIN_SCREEN) {
                System.out.println("\n ON LOGIN SCREEN ! \n");
            }

            if (event.getGameState() == GameState.LOGGED_IN) {

                // Schedule task to click "Click to Play" button if it appears
                scheduler.scheduleAtFixedRate(() -> {
                    Widget clickToPlay = client.getWidget(24772681);
                    if (clickToPlay != null) {
                        System.out.println("Click to play is not null!");

                        // Simulate mouse movement and click on the button
                        mouseController.move(MathStuff.randomRectanglePoint(clickToPlay.getBounds()));
                        mouseController.leftClick();
                    }
                }, 0, 651, TimeUnit.MILLISECONDS); // Check every 651 milliseconds

                // Schedule task to update inventory positions
                scheduler.scheduleAtFixedRate(() -> {
                    Widget inventoryIcon = client.getWidget(ComponentID.FIXED_VIEWPORT_INVENTORY_ICON);
                    if (inventoryIcon != null) {
                        inventoryManager.scanInventory();
                        inventoryManager.randomInventoryLocations();
                        System.out.println("Updated inventory positions");
                        scheduler.shutdown();
                    }
                }, 0, 320, TimeUnit.MILLISECONDS); // Check every 320 milliseconds

            }
        }
    }

    /**
     * Handles interaction changes involving the player.
     *
     * @param event The interacting changed event.
     */
    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {
        interactionManager.interact(event);
    }

    /**
     * Handles custom commands entered in the game chat.
     *
     * @param commandExecuted The command executed event.
     */
    @Subscribe
    public void onCommandExecuted(CommandExecuted commandExecuted) {
        String[] args = commandExecuted.getArguments();
        switch (commandExecuted.getCommand()) {
            case "camera": {
                cameraController.adjustCamera(512, 250);
                cameraController.adjustZoom(350);
                break;
            }
            case "inventory": {
                inventoryManager.mouseOver();
                break;
            }
            default:
                break;
        }
    }

    /**
     * Provides the plugin configuration.
     *
     * @param configManager The ConfigManager instance.
     * @return The TestConfig instance.
     */
    @Provides
    TestConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(TestConfig.class);
    }
}
