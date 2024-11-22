package com.flump;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

/**
 * An overlay that displays camera information such as pitch, yaw, and zoom.
 */
class CameraInfoOverlay extends Overlay {
    private final Client client;
    private final TestConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    /**
     * Constructor for CameraInfoOverlay.
     *
     * @param client The game client instance.
     * @param config The plugin configuration.
     */
    @Inject
    private CameraInfoOverlay(Client client, TestConfig config) {
        setPosition(OverlayPosition.BOTTOM_LEFT); // Position the overlay at the bottom left
        this.client = client;
        this.config = config;
    }

    /**
     * Renders the overlay if camera information display is enabled in the config.
     *
     * @param graphics The Graphics2D object used for rendering.
     * @return The Dimension of the rendered overlay.
     */
    @Override
    public Dimension render(Graphics2D graphics) {

        if (config.cameraInfo()) {
            panelComponent.getChildren().clear();
            String overlayTitle = "Camera Position:";

            // Build overlay title
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(overlayTitle)
                    .color(Color.GREEN)
                    .build());

            // Set the size of the overlay (width)
            panelComponent.setPreferredSize(new Dimension(
                    graphics.getFontMetrics().stringWidth(overlayTitle) + 30,
                    0));

            // Add a line on the overlay for camera pitch
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Pitch:")
                    .right(Integer.toString(client.getCameraPitch()))
                    .build());

            // Add a line on the overlay for camera yaw
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Yaw:")
                    .right(Integer.toString(client.getCameraYaw()))
                    .build());

            // Add a line on the overlay for camera zoom
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Zoom:")
                    .right(Integer.toString(client.getScale()))
                    .build());

            /*
            // Uncomment the following lines to display camera X, Y, Z coordinates
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("X:")
                    .right(Integer.toString(client.getCameraX()))
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Y:")
                    .right(Integer.toString(client.getCameraY()))
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Z:")
                    .right(Integer.toString(client.getCameraZ()))
                    .build());
            */
        } else {
            panelComponent.getChildren().clear();
        }

        return panelComponent.render(graphics);
    }
}
