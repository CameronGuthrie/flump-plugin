package com.flump;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.input.MouseListener;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.input.MouseManager;
import javax.inject.Inject;
import java.awt.*;
import java.awt.event.MouseEvent;

@Slf4j
@PluginDescriptor(
        name = "Test Mouse Plugin",
        description = "A plugin that is testing mouse event listeners",
        tags = {"", ""}
)
@SuppressWarnings("unused")
public class MousePlugin extends Plugin implements MouseListener {

    @Inject
    private Client client;

    @Inject
    private MouseManager mouseManager;

    @Inject
    private MousePluginOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    private Point mouseCoords;
    private Color drawColor = Color.WHITE;

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
        mouseManager.registerMouseListener(0, this);
        log.info("Custom plugin started!");
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        mouseManager.unregisterMouseListener(this);
        log.info("Custom plugin stopped!");
    }

    private void calculateMousePosition(MouseEvent mouseEvent) {
        mouseCoords = new Point(mouseEvent.getX(), mouseEvent.getY());
    }

    public void onGameTick(GameTick event) {
        setDrawColor(Color.LIGHT_GRAY);
        System.out.println(getDrawColor().toString());
    }

    private static void virtualClick(Component target, int x, int y) {
        MouseEvent press,release,click;
        Point point;
        long time;

        point = new Point(x,y);

        time    = System.currentTimeMillis();
        press   = new MouseEvent(target, MouseEvent.MOUSE_PRESSED, time, 0, x, y, point.getX(), point.getY(), 1, false, MouseEvent.BUTTON1);
        release = new MouseEvent(target, MouseEvent.MOUSE_RELEASED, time, 0, x, y, point.getX(), point.getY(), 1, false, MouseEvent.BUTTON1);
        click   = new MouseEvent(target, MouseEvent.MOUSE_CLICKED, time, 0, x, y, point.getX(), point.getY(), 1, false, MouseEvent.BUTTON1);

        target.dispatchEvent(press);
        target.dispatchEvent(release);
        target.dispatchEvent(click);
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getComponent());
        return mouseEvent;
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent) {
        setDrawColor(Color.YELLOW);
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent) {
        setDrawColor(Color.LIGHT_GRAY);
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent) {
        calculateMousePosition(mouseEvent);
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent) {
        calculateMousePosition(mouseEvent);
        return mouseEvent;
    }

    public Point getMouseCoords() {
        return mouseCoords;
    }

    public Color getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
    }

    public void setMouseCoords(Point mouseCoords) {
        this.mouseCoords = mouseCoords;
    }

}


