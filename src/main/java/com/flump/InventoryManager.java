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
        for(int i = 0; i < INVENTORY_SIZE; i++) {
            Item[] items = getInventoryItems();
            if (i < items.length) {
                mouseController.move(getRandomInventoryPoints()[i]);
            }
        }
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
//                System.out.println(rands[i]);
//                System.out.println(widget.getBounds().getX() + ", " + widget.getBounds().getY());
            }
        }

        setRandomInventoryPoints(rands);
    }

/*
    public void testMethod() {
        final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
//        client.getVarcIntValue(VarClientInt.INVENTORY_TAB);

        if (itemContainer == null) {
            System.out.println("Inventory is empty");
            return;
        }


        final Item[] items = itemContainer.getItems();


        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (i < items.length) {
                final Item item = items[i];
                final ItemComposition comp = itemManager.getItemComposition(item.getId());
//                System.out.println(item + " : " + comp.getMembersName());
//                assert widgets != null;
//                final Widget widget = widgets[i];
//                controller.move(mathStuff.randomRectanglePoint(widget.getBounds()));
            }
        }


//        final Widget[] widgets = Objects.requireNonNull(client.getWidget(ComponentID.INVENTORY_CONTAINER)).getChildren();
//        for (int i = 0; i < INVENTORY_SIZE; i++) {
//            assert widgets != null;
//            if (i < widgets.length) {
//                final Widget widget = widgets[i];
//                System.out.println(widget.getBounds());
//                controller.move(mathStuff.randomRectanglePoint(widget.getBounds()));
//            }
//        }

    }

//    private void pointAtItem(Rectangle rect){
//        Point point = rect);
//        ;
//    }

*/

}