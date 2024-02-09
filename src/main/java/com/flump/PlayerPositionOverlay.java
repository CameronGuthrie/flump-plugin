package com.flump;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

class PlayerPositionOverlay extends Overlay {
    private final Client client;
    private final TestConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private PlayerPositionOverlay(Client client, TestConfig config) {
        setPosition(OverlayPosition.BOTTOM_LEFT);//  .ABOVE_CHATBOX_RIGHT);
        this.client = client;
        this.config = config;
    }

    // currently just doing camera position
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

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("X:")
                    .right(Integer.toString(client.getLocalPlayer().getWorldLocation().getX()))
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Y:")
                    .right(Integer.toString(client.getLocalPlayer().getWorldLocation().getY()))
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Plane:")
                    .right(Integer.toString(client.getLocalPlayer().getWorldLocation().getPlane()))
                    .build());

//            panelComponent.getChildren().add(LineComponent.builder()
//                    .left("WA X:")
//                    .right(Integer.toString(client.getLocalPlayer().getWorldArea().getX()))
//                    .build());
//
//            panelComponent.getChildren().add(LineComponent.builder()
//                    .left("WA Y:")
//                    .right(Integer.toString(client.getLocalPlayer().getWorldArea().getY()))
//                    .build());
//
//            panelComponent.getChildren().add(LineComponent.builder()
//                    .left("WA Plane:")
//                    .right(Integer.toString(client.getLocalPlayer().getWorldArea().getPlane()))
//                    .build());

        } else {
            panelComponent.getChildren().clear();
        }

        return panelComponent.render(graphics);
    }
}
