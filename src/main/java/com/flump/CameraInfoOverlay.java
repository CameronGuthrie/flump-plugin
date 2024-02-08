package com.flump;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

class CameraInfoOverlay extends Overlay {
    private final Client client;
    private final TestConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private CameraInfoOverlay(Client client, TestConfig config) {
        setPosition(OverlayPosition.BOTTOM_LEFT);//  .ABOVE_CHATBOX_RIGHT);
        this.client = client;
        this.config = config;
    }

    // currently just doing camera position
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

            // Add a line on the overlay camera pitch
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Pitch:")
                    .right(Integer.toString(client.getCameraPitch()))
                    .build());

            // Add a line on the overlay camera yaw
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Yaw:")
                    .right(Integer.toString(client.getCameraYaw()))
                    .build());

            // Add a line on the overlay camera x
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("X:")
                    .right(Integer.toString(client.getCameraX()))
                    .build());

            // Add a line on the overlay camera y
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Y:")
                    .right(Integer.toString(client.getCameraY()))
                    .build());

            // Add a line on the overlay camera z
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Z:")
                    .right(Integer.toString(client.getCameraZ()))
                    .build());

            /*
            // Add a line on the overlay camera x2
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("X2:")
                    .right(Integer.toString(client.getCameraX2()))
                    .build());

            // Add a line on the overlay camera y2
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Y2:")
                    .right(Integer.toString(client.getCameraY2()))
                    .build());

            // Add a line on the overlay camera z2
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Z2:")
                    .right(Integer.toString(client.getCameraZ2()))
                    .build());
            */
        } else {
            panelComponent.getChildren().clear();
        }

        return panelComponent.render(graphics);
    }
}
