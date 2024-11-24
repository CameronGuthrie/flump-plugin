package com.flump;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import net.runelite.client.callback.ClientThread;

/**
 * Manages the player's inventory, including scanning for items and randomizing inventory points for mouse movements.
 */
@Singleton
public class InventoryManager {

    private final MathStuff mathStuff;
    private final Client client;
    private final MouseController mouseController;
    private final ClientThread clientThread;

    public static final int INVENTORY_SIZE = 28;

    @Getter
    @Setter
    private Widget[] widgets;

    @Getter
    @Setter
    private Point[] randomInventoryPoints = new Point[INVENTORY_SIZE];

    @Getter
    @Setter
    private Item[] inventoryItems;

    /**
     * Constructor for InventoryManager.
     *
     * @param mathStuff       Utility class for mathematical operations.
     * @param client          The game client instance.
     * @param mouseController Controller to simulate mouse inputs.
     * @param clientThread    The client thread for scheduling tasks.
     */
    @Inject
    private InventoryManager(MathStuff mathStuff, Client client, MouseController mouseController, ClientThread clientThread) {
        this.mathStuff = mathStuff;
        this.client = client;
        this.mouseController = mouseController;
        this.clientThread = clientThread;
    }

    /**
     * Simulates hovering over a random inventory slot by moving the mouse to a random point within an item's bounds.
     */
    public void mouseOver() {
        if (getInventoryItems() == null || getInventoryItems().length == 0) {
            // Inventory items not initialized, attempt to scan inventory
            scanInventory();
            if (getInventoryItems() == null || getInventoryItems().length == 0) {
                System.out.println("No inventory items found, cannot perform mouseOver.");
                return;
            }
        }

        if (getRandomInventoryPoints() == null || getRandomInventoryPoints().length == 0) {
            // Random points not initialized, attempt to generate them
            randomInventoryLocations();
            if (getRandomInventoryPoints() == null || getRandomInventoryPoints().length == 0) {
                System.out.println("Random inventory points not initialized, cannot perform mouseOver.");
                return;
            }
        }

        Random rand = new Random();
        //int index = rand.nextInt(getInventoryItems().length);
        int index = rand.nextInt(getRandomInventoryPoints().length);
        mouseController.move(getRandomInventoryPoints()[index]);
    }

    /**
     * Simulates hovering over all inventory slots by moving the mouse to each item's location.
     */
    public void mouseOverAll() {
        if (getInventoryItems() == null || getInventoryItems().length == 0) {
            // Inventory items not initialized, attempt to scan inventory
            scanInventory();
            if (getInventoryItems() == null || getInventoryItems().length == 0) {
                System.out.println("No inventory items found, cannot perform mouseOverAll.");
                return;
            }
        }

        if (getRandomInventoryPoints() == null || getRandomInventoryPoints().length == 0) {
            // Random points not initialized, attempt to generate them
            randomInventoryLocations();
            if (getRandomInventoryPoints() == null || getRandomInventoryPoints().length == 0) {
                System.out.println("Random inventory points not initialized, cannot perform mouseOverAll.");
                return;
            }
        }

        // Run the loop in a background thread to avoid blocking the main thread
        new Thread(() -> {
            for (Point point : getRandomInventoryPoints()) {
                // Schedule the mouse movement on the client thread
                clientThread.invokeLater(() -> mouseController.move(point));

                // Optional delay between movements to simulate human behavior
                try {
                    Thread.sleep(100); // Sleep for 100 milliseconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    break; // Exit the loop if interrupted
                }
            }
        }).start();
    }

    /**
     * Scans the current inventory and stores the items.
     */
    public void scanInventory() {
        final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
        if (itemContainer != null) {
            setInventoryItems(itemContainer.getItems());
        }
    }

    /**
     * Randomizes the mouseover point for items in the inventory for mouse movement to simulate human-like behavior.
     */
    public void randomInventoryLocations() {
        Widget inventoryWidget = client.getWidget(ComponentID.INVENTORY_CONTAINER);
        if (inventoryWidget != null) {
            setWidgets(Objects.requireNonNull(inventoryWidget).getChildren());
            List<Point> randomPoints = new ArrayList<>();

            for (int i = 0; i < getInventoryItems().length; i++) {
                Item item = getInventoryItems()[i];
                if (item != null && item.getId() > 0) {
                    Widget widget = widgets[i];
                    if (widget != null) {
                        randomPoints.add(mathStuff.randomRectanglePoint(widget.getBounds()));
                    }
                }
            }

            // Convert the list to an array and set it
            setRandomInventoryPoints(randomPoints.toArray(new Point[0]));
        }
    }

}
