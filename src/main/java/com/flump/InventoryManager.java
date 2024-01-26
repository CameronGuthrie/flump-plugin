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

    public void mouseOver() {
        Random rand = new Random();
        mouseController.move(getRandomInventoryPoints()[rand.nextInt(getInventoryItems().length-1)]);
//        for(int i = 0; i < INVENTORY_SIZE; i++) {
//            Item[] items = getInventoryItems();
//            if (i < items.length) {
//                mouseController.move(getRandomInventoryPoints()[i]);
//            }
//        }
    }

    public void scanInventory() {
        final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
        assert itemContainer != null;
        setInventoryItems(itemContainer.getItems());
    }

    public void randomInventoryLocations() {
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
