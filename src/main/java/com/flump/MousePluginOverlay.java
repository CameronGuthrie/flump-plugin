package com.flump;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;

import javax.inject.Inject;

class MousePluginOverlay extends Overlay {

    private final Client client;
    private final MousePlugin plugin;

    @Inject
    private MousePluginOverlay(Client client, MousePlugin plugin){
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        Point coords = plugin.getMouseCoords();

        if (coords !=null) {
            renderMouse(graphics, coords, plugin.getDrawColor());
        }

        if (coords == null) {
            return null;
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


