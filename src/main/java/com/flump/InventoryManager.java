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
 * Manages the player's inventory, including scanning for items and randomizing inventory points.
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
    public Point[] randomInventoryPoints = new Point[28];

    @Getter
    @Setter
    private Item[] inventoryItems;

    @Inject
    private InventoryManager(MathStuff mathStuff, Client client, MouseController mouseController) {
        this.mathStuff = mathStuff;
        this.client = client;
        this.mouseController = mouseController;
    }

    /**
     * Simulates hovering over a random inventory slot.
     */
    public void mouseOver() {
        // Logic to hover over a random inventory slot.
        Random rand = new Random();
        mouseController.move(getRandomInventoryPoints()[rand.nextInt(getInventoryItems().length-1)]);
    }

    /**
     * Scans the current inventory and stores the items.
     */
    public void scanInventory() {
        // Logic to scan inventory.
        final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
        assert itemContainer != null;
        setInventoryItems(itemContainer.getItems());
    }

    /**
     * Randomizes the locations of items in the inventory for mouse movement.
     */
    public void randomInventoryLocations() {
        // Logic to randomize inventory locations.
        setWidgets(Objects.requireNonNull(client.getWidget(ComponentID.INVENTORY_CONTAINER)).getChildren());
        Point[] rands = new Point[28];

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            assert widgets != null;
            if (i < widgets.length) {
                final Widget widget = widgets[i];
                rands[i] = mathStuff.randomRectanglePoint(widget.getBounds());
            }
        }

        setRandomInventoryPoints(rands);
    }

}
