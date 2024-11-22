package com.flump;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;
import java.util.Random;

/**
 * Manages the player's inventory, including scanning for items and randomizing inventory points for mouse movements.
 */
@Singleton
public class InventoryManager {

    private final MathStuff mathStuff;
    private final Client client;
    private final MouseController mouseController;

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
     */
    @Inject
    private InventoryManager(MathStuff mathStuff, Client client, MouseController mouseController) {
        this.mathStuff = mathStuff;
        this.client = client;
        this.mouseController = mouseController;
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
        int index = rand.nextInt(getInventoryItems().length);
        mouseController.move(getRandomInventoryPoints()[index]);
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
     * Randomizes the locations of items in the inventory for mouse movement to simulate human-like behavior.
     */
    public void randomInventoryLocations() {
        Widget inventoryWidget = client.getWidget(ComponentID.INVENTORY_CONTAINER);
        if (inventoryWidget != null) {
            setWidgets(Objects.requireNonNull(inventoryWidget).getChildren());
            Point[] randomPoints = new Point[INVENTORY_SIZE];

            for (int i = 0; i < INVENTORY_SIZE; i++) {
                if (widgets != null && i < widgets.length) {
                    final Widget widget = widgets[i];
                    randomPoints[i] = mathStuff.randomRectanglePoint(widget.getBounds());
                }
            }

            setRandomInventoryPoints(randomPoints);
        }
    }
}
