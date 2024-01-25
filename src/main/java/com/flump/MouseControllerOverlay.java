package com.flump;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;

import javax.inject.Inject;

class MouseControllerOverlay extends Overlay {

    private final Client client;

    private final MouseController controller;

    @Inject
    private MouseControllerOverlay(Client client, MouseController controller){
        this.client = client;
        this.controller = controller;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);

//        System.out.println("MouseControllerOverlay using MouseController: " + controller.hashCode());
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        Point coords = client.getMouseCanvasPosition();
        Color drawColor = controller.getDrawColor();

        if (coords !=null) {
            renderMouse(graphics, coords, drawColor);
        }

        return null;

    }

    private void renderMouse(Graphics2D graphics, Point coords, Color color) {

        int canvasW = client.getCanvasWidth();
        int canvasH = client.getCanvasHeight();
        int mouseX = coords.getX();
        int mouseY = coords.getY();

        graphics.setColor(color);
        graphics.drawRect(0, mouseY, canvasW, 1);
        graphics.drawRect(mouseX, 0, 1, canvasH);

    }

}


