package com.flump;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.input.MouseListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

@Singleton
public class MouseController implements MouseListener {

    private final Client client;

    @Inject
    private MouseController(Client client) {
        this.client = client;
//        System.out.println("MouseController instance created: " + this.hashCode());
    }

    @Getter
    @Setter
    private Color drawColor = Color.LIGHT_GRAY;

    @Getter
    @Setter
    private Point mouseCoords;

    @Getter
    @Setter
    private Point startPoint = new Point(0,0);

    private void calculateMousePosition(MouseEvent mouseEvent) {
        mouseCoords = new Point(mouseEvent.getX(), mouseEvent.getY());
    }

    public void move(Point point) {
        if (client == null || client.getCanvas() == null || point == null) {
            System.out.println("Client, canvas, or point is null");
            return;
        }
        simulateHumanLikeMovement(client.getCanvas(), this.getStartPoint(), point);
    }

    public void leftClick() {
        virtualClick(client.getCanvas(), client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), MouseEvent.BUTTON1);
    }

    public void rightClick() {
        virtualClick(client.getCanvas(), client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), MouseEvent.BUTTON2);
    }

    public void wheelClick() {
        virtualClick(client.getCanvas(), client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), MouseEvent.MOUSE_WHEEL);
    }

    private static void virtualClick(Component target, int x, int y, int button) {
        MouseEvent press,release,click;

        Point point = new Point(x,y);
        long time = System.currentTimeMillis();

        press   = new MouseEvent(target, MouseEvent.MOUSE_PRESSED, time, 0, x, y, point.getX(), point.getY(), 1, false, button);
        release = new MouseEvent(target, MouseEvent.MOUSE_RELEASED, time, 0, x, y, point.getX(), point.getY(), 1, false, button);
        click   = new MouseEvent(target, MouseEvent.MOUSE_CLICKED, time, 0, x, y, point.getX(), point.getY(), 1, false, button);

        target.dispatchEvent(press);
        target.dispatchEvent(release);
        target.dispatchEvent(click);
    }

    private static void virtualMove(Component target, Point point) {
        MouseEvent move;
        long time = System.currentTimeMillis();

        move = new MouseEvent(target, MouseEvent.MOUSE_MOVED, time, 0, point.getX(), point.getY(), 0, false);

        target.dispatchEvent(move);
    }

    public void virtualScrollUp() {
        simulateMouseWheelMovement(-1); // Negative units to scroll up
    }

    public void virtualScrollDown() {
        simulateMouseWheelMovement(1); // Positive units to scroll down
    }

    private void simulateHumanLikeMovement(Component target, Point startPoint, Point endPoint) {
        SwingWorker<Void, Point> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                Point controlPoint1 = getRandomControlPoint(startPoint, endPoint);
                Point controlPoint2 = getRandomControlPoint(startPoint, endPoint);
                final long duration = 200; // total duration of the movement
                final long startTime = System.currentTimeMillis();
                final int refreshRate = 10; // in milliseconds

                while (System.currentTimeMillis() - startTime < duration) {
                    double t = (double) (System.currentTimeMillis() - startTime) / duration;
                    t = easeInOut(t);
                    Point nextPoint = calculateBezierPoint(t, startPoint, controlPoint1, controlPoint2, endPoint);

                    // Add micro-movements in the latter half of the movement
                    if (t > 0.5) {
                        nextPoint = addMicroMovements(nextPoint);
                    }

                    publish(nextPoint);

                    try {
                        Thread.sleep(refreshRate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @Override
            protected void process(List<Point> chunks) {
                // Process the latest point for smoother update
                Point latestPoint = chunks.get(chunks.size() - 1);
                virtualMove(target, latestPoint);
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> virtualMove(target, endPoint));
            }
        };

        worker.execute();
    }

    private void simulateMouseWheelMovement(int wheelAmt) {
        Component targetComponent = client.getCanvas(); // Assume this is the component to receive the event
        if (targetComponent != null) {
            MouseWheelEvent wheelEvent = new MouseWheelEvent(
                    targetComponent,
                    MouseWheelEvent.MOUSE_WHEEL,
                    System.currentTimeMillis(),
                    0, // No modifiers
                    mouseCoords.getX(), // x coordinate of the mouse cursor in the component's coordinate space
                    mouseCoords.getY(), // y coordinate
                    0, // click count
                    false, // not a popup trigger
                    MouseWheelEvent.WHEEL_UNIT_SCROLL,
                    1, // scroll amount
                    wheelAmt // wheel rotation
            );
            targetComponent.dispatchEvent(wheelEvent);
        }
    }

    private static Point getRandomControlPoint(Point start, Point end) {
        Random rand = new Random();
        int x = (int)(Math.min(start.getX(), end.getX()) + rand.nextDouble() * Math.abs(start.getX() - end.getX()));
        int y = (int)(Math.min(start.getY(), end.getY()) + rand.nextDouble() * Math.abs(start.getY() - end.getY()));
        return new Point(x, y);
    }

    private static Point calculateBezierPoint(double t, Point p0, Point p1, Point p2, Point p3) {
        int x = (int)(Math.pow(1 - t, 3) * p0.getX()
                + 3 * t * Math.pow(1 - t, 2) * p1.getX()
                + 3 * Math.pow(t, 2) * (1 - t) * p2.getX()
                + Math.pow(t, 3) * p3.getX());
        int y = (int)(Math.pow(1 - t, 3) * p0.getY()
                + 3 * t * Math.pow(1 - t, 2) * p1.getY()
                + 3 * Math.pow(t, 2) * (1 - t) * p2.getY()
                + Math.pow(t, 3) * p3.getY());
        return new Point(x, y);
    }

    private static double easeInOut(double t) {
        return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }

    private Point addMicroMovements(Point point) {
        Random rand = new Random();
        int dx = rand.nextInt(3) - 1;
        int dy = rand.nextInt(3) - 1;
        int x = point.getX() + dx;
        int y = point.getY() + dy;

        return new Point(x, y);
//        Thread.sleep(5);
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent) {
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
        Point exit = new Point((int)mouseEvent.getPoint().getX(), (int)mouseEvent.getPoint().getY());
        Point output = new Point(0,0);

        if (exit.getY() < 0 && exit.getX() > 0) {
            output = new Point(exit.getX(), 0);
        }

        if (exit.getX() < 0 && exit.getY() > 0) {
            output = new Point(0, exit.getY());
        }

        if (exit.getY() > client.getCanvasHeight() && exit.getX() > 0) {
            output = new Point(exit.getX(), client.getCanvasHeight());
        }

        if (exit.getX() > client.getCanvasWidth() && exit.getY() > 0) {
            output = new Point(client.getCanvasWidth(), exit.getY());
        }

        System.out.println(exit);
        System.out.println("EXIT = x: " + exit.getX() + ", y: " + exit.getY());
        System.out.println(output);
        setStartPoint(output);
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

        if (getMouseCoords() != null){
            setStartPoint(getMouseCoords());
        }

        return mouseEvent;
    }

}
