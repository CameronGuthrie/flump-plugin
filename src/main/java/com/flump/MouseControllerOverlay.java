package com.flump;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;

import javax.inject.Inject;

/**
 * An overlay that renders visual indicators for the mouse position, such as crosshairs.
 */
class MouseControllerOverlay extends Overlay {

    private final Client client;
    private final MouseController controller;

    /**
     * Constructor for MouseControllerOverlay.
     *
     * @param client     The game client instance.
     * @param controller The MouseController instance.
     */
    @Inject
    private MouseControllerOverlay(Client client, MouseController controller) {
        this.client = client;
        this.controller = controller;
        setPosition(OverlayPosition.DYNAMIC); // Overlay position can change dynamically
        setLayer(OverlayLayer.ALWAYS_ON_TOP); // Ensure overlay is always visible
        setPriority(OverlayPriority.HIGH); // High priority for rendering order
    }

    /**
     * Renders the mouse overlay, drawing crosshairs at the mouse position.
     *
     * @param graphics The Graphics2D object used for rendering.
     * @return The Dimension of the rendered overlay.
     */
    @Override
    public Dimension render(Graphics2D graphics) {

        Point coords = client.getMouseCanvasPosition();
        Color drawColor = controller.getDrawColor();

        if (coords != null && !coords.equals(new Point(0, 0))) {
            renderMouse(graphics, coords, drawColor);
        }

        return null;
    }

    /**
     * Draws crosshairs centered at the given coordinates.
     *
     * @param graphics The Graphics2D object used for drawing.
     * @param coords   The coordinates at which to draw the crosshairs.
     * @param color    The color of the crosshairs.
     */
    private void renderMouse(Graphics2D graphics, Point coords, Color color) {

        int canvasW = client.getCanvasWidth();
        int canvasH = client.getCanvasHeight();
        int mouseX = coords.getX();
        int mouseY = coords.getY();

        graphics.setColor(color);
        // Draw horizontal line
        graphics.drawLine(0, mouseY, canvasW, mouseY);
        // Draw vertical line
        graphics.drawLine(mouseX, 0, mouseX, canvasH);
    }
}
