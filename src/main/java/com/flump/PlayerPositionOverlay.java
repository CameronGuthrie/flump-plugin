package com.flump;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

/**
 * An overlay that displays the player's current world position.
 */
class PlayerPositionOverlay extends Overlay {
    private final Client client;
    private final TestConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    /**
     * Constructor for PlayerPositionOverlay.
     *
     * @param client The game client instance.
     * @param config The plugin configuration.
     */
    @Inject
    private PlayerPositionOverlay(Client client, TestConfig config) {
        setPosition(OverlayPosition.BOTTOM_LEFT); // Position the overlay at the bottom left
        this.client = client;
        this.config = config;
    }

    /**
     * Renders the overlay if player position display is enabled in the config.
     *
     * @param graphics The Graphics2D object used for rendering.
     * @return The Dimension of the rendered overlay.
     */
    @Override
    public Dimension render(Graphics2D graphics) {

        if (config.playerPosition()) {
            panelComponent.getChildren().clear();
            String overlayTitle = "Player Position:";

            // Build overlay title
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(overlayTitle)
                    .color(Color.GREEN)
                    .build());

            // Set the size of the overlay (width)
            panelComponent.setPreferredSize(new Dimension(
                    graphics.getFontMetrics().stringWidth(overlayTitle) + 30,
                    0));

            // Add a line on the overlay for player's world X coordinate
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("X:")
                    .right(Integer.toString(client.getLocalPlayer().getWorldLocation().getX()))
                    .build());

            // Add a line on the overlay for player's world Y coordinate
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Y:")
                    .right(Integer.toString(client.getLocalPlayer().getWorldLocation().getY()))
                    .build());

            // Add a line on the overlay for player's plane (floor level)
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Plane:")
                    .right(Integer.toString(client.getLocalPlayer().getWorldLocation().getPlane()))
                    .build());

        } else {
            panelComponent.getChildren().clear();
        }

        return panelComponent.render(graphics);
    }
}
