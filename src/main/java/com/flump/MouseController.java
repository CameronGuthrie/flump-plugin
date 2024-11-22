package com.flump;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.input.MouseListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Controls mouse interactions in the game, including movements and clicks.
 * Simulates human-like movement using Bezier curves.
 */
@Singleton
public class MouseController implements MouseListener {

    private final Client client;

    /**
     * Constructor for MouseController.
     *
     * @param client The game client instance.
     */
    @Inject
    private MouseController(Client client) {
        this.client = client;
    }

    @Getter
    @Setter
    private Color drawColor = Color.LIGHT_GRAY; // Color used for mouse overlay visuals

    @Getter
    @Setter
    private Point mouseCoords; // Current mouse coordinates

    @Getter
    @Setter
    private Point startPoint = new Point(0, 0); // Starting point for mouse movements

    /**
     * Updates the current mouse coordinates based on a MouseEvent.
     *
     * @param mouseEvent The MouseEvent containing new mouse position.
     */
    private void calculateMousePosition(MouseEvent mouseEvent) {
        mouseCoords = new Point(mouseEvent.getX(), mouseEvent.getY());
    }

    /**
     * Moves the mouse to a specified point on the game canvas, simulating human-like movement.
     *
     * @param point The target point for mouse movement.
     */
    public void move(Point point) {
        // Check for null to prevent exceptions
        if (client == null || client.getCanvas() == null || point == null) {
            System.out.println("Client, canvas, or point is null");
            return;
        }
        // Simulate movement from the current startPoint to the target point
        simulateHumanLikeMovement(client.getCanvas(), this.getStartPoint(), point);
    }

    /**
     * Simulates a left mouse click at the current mouse position.
     */
    public void leftClick() {
        virtualClick(client.getCanvas(), client.getMouseCanvasPosition().getX(),
                client.getMouseCanvasPosition().getY(), MouseEvent.BUTTON1);
    }

    /**
     * Simulates a right mouse click at the current mouse position.
     */
    public void rightClick() {
        virtualClick(client.getCanvas(), client.getMouseCanvasPosition().getX(),
                client.getMouseCanvasPosition().getY(), MouseEvent.BUTTON3);
    }

    /**
     * Simulates a mouse wheel click at the current mouse position.
     */
    public void wheelClick() {
        virtualClick(client.getCanvas(), client.getMouseCanvasPosition().getX(),
                client.getMouseCanvasPosition().getY(), MouseEvent.BUTTON2);
    }

    /**
     * Simulates a mouse click event on the target component.
     *
     * @param target The component to receive the event.
     * @param x      The x-coordinate of the click.
     * @param y      The y-coordinate of the click.
     * @param button The mouse button to simulate.
     */
    private static void virtualClick(Component target, int x, int y, int button) {
        MouseEvent press, release, click;

        Point point = new Point(x, y);
        long time = System.currentTimeMillis();

        // Create mouse press event
        press = new MouseEvent(target, MouseEvent.MOUSE_PRESSED, time, 0, x, y,
                point.getX(), point.getY(), 1, false, button);
        // Create mouse release event
        release = new MouseEvent(target, MouseEvent.MOUSE_RELEASED, time, 0, x, y,
                point.getX(), point.getY(), 1, false, button);
        // Create mouse click event
        click = new MouseEvent(target, MouseEvent.MOUSE_CLICKED, time, 0, x, y,
                point.getX(), point.getY(), 1, false, button);

        // Dispatch the events to the target component
        target.dispatchEvent(press);
        target.dispatchEvent(release);
        target.dispatchEvent(click);
    }

    /**
     * Simulates a mouse move event to a specific point on the target component.
     *
     * @param target The component to receive the event.
     * @param point  The point to move the mouse to.
     */
    private static void virtualMove(Component target, Point point) {
        long time = System.currentTimeMillis();
        MouseEvent move = new MouseEvent(target, MouseEvent.MOUSE_MOVED, time, 0,
                point.getX(), point.getY(), 0, false);
        target.dispatchEvent(move);
    }

    /**
     * Simulates scrolling the mouse wheel up.
     */
    public void virtualScrollUp() {
        simulateMouseWheelMovement(-1); // Negative units to scroll up
    }

    /**
     * Simulates scrolling the mouse wheel down.
     */
    public void virtualScrollDown() {
        simulateMouseWheelMovement(1); // Positive units to scroll down
    }

    /**
     * Simulates human-like mouse movement from the start point to the end point using Bezier curves.
     *
     * @param target     The component to receive the mouse movement events.
     * @param startPoint The starting point of the movement.
     * @param endPoint   The ending point of the movement.
     */
    private void simulateHumanLikeMovement(Component target, Point startPoint, Point endPoint) {
        // Use a SwingWorker to perform the movement asynchronously
        SwingWorker<Void, Point> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Generate control points for the Bezier curve
                Point controlPoint1 = getRandomControlPoint(startPoint, endPoint);
                Point controlPoint2 = getRandomControlPoint(startPoint, endPoint);
                final long duration = 200; // Total duration of the movement in milliseconds
                final long startTime = System.currentTimeMillis();
                final int refreshRate = 10; // Update every 10 milliseconds

                while (System.currentTimeMillis() - startTime < duration) {
                    double t = (double) (System.currentTimeMillis() - startTime) / duration;
                    t = easeInOut(t); // Apply easing function for more natural movement
                    Point nextPoint = calculateBezierPoint(t, startPoint, controlPoint1, controlPoint2, endPoint);

                    // Add micro-movements in the latter half of the movement
                    if (t > 0.5) {
                        nextPoint = addMicroMovements(nextPoint);
                    }

                    publish(nextPoint); // Send the next point to the process method

                    try {
                        Thread.sleep(refreshRate);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupt status
                        break;
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Point> chunks) {
                // Move the mouse to the latest point for smoother movement
                Point latestPoint = chunks.get(chunks.size() - 1);
                virtualMove(target, latestPoint);
            }

            @Override
            protected void done() {
                // Ensure the mouse ends at the exact endPoint
                SwingUtilities.invokeLater(() -> virtualMove(target, endPoint));
            }
        };

        worker.execute();
    }

    /**
     * Simulates mouse wheel movement.
     *
     * @param wheelAmt The amount to scroll; negative for up, positive for down.
     */
    private void simulateMouseWheelMovement(int wheelAmt) {
        Component targetComponent = client.getCanvas();
        if (targetComponent != null) {
            MouseWheelEvent wheelEvent = new MouseWheelEvent(
                    targetComponent,
                    MouseWheelEvent.MOUSE_WHEEL,
                    System.currentTimeMillis(),
                    0, // No modifiers
                    mouseCoords.getX(), // x-coordinate of the mouse cursor
                    mouseCoords.getY(), // y-coordinate
                    0, // click count
                    false, // not a popup trigger
                    MouseWheelEvent.WHEEL_UNIT_SCROLL,
                    1, // scroll amount
                    wheelAmt // wheel rotation
            );
            targetComponent.dispatchEvent(wheelEvent);
        }
    }

    /**
     * Generates a random control point for Bezier curve between two points.
     *
     * @param start The starting point.
     * @param end   The ending point.
     * @return A random control point.
     */
    private static Point getRandomControlPoint(Point start, Point end) {
        Random rand = new Random();
        int x = (int) (Math.min(start.getX(), end.getX()) + rand.nextDouble() * Math.abs(start.getX() - end.getX()));
        int y = (int) (Math.min(start.getY(), end.getY()) + rand.nextDouble() * Math.abs(start.getY() - end.getY()));
        return new Point(x, y);
    }

    /**
     * Calculates a point on a cubic Bezier curve for a given t value.
     *
     * @param t  The parameter t (0 <= t <= 1).
     * @param p0 The starting point.
     * @param p1 The first control point.
     * @param p2 The second control point.
     * @param p3 The ending point.
     * @return The calculated point on the curve.
     */
    private static Point calculateBezierPoint(double t, Point p0, Point p1, Point p2, Point p3) {
        int x = (int) (Math.pow(1 - t, 3) * p0.getX()
                + 3 * t * Math.pow(1 - t, 2) * p1.getX()
                + 3 * Math.pow(t, 2) * (1 - t) * p2.getX()
                + Math.pow(t, 3) * p3.getX());
        int y = (int) (Math.pow(1 - t, 3) * p0.getY()
                + 3 * t * Math.pow(1 - t, 2) * p1.getY()
                + 3 * Math.pow(t, 2) * (1 - t) * p2.getY()
                + Math.pow(t, 3) * p3.getY());
        return new Point(x, y);
    }

    /**
     * Easing function for smoother acceleration and deceleration.
     *
     * @param t The parameter t (0 <= t <= 1).
     * @return The adjusted t value after easing.
     */
    private static double easeInOut(double t) {
        return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }

    /**
     * Adds small random movements to a point to simulate micro-movements of the mouse.
     *
     * @param point The original point.
     * @return A new point with micro-movements applied.
     */
    private Point addMicroMovements(Point point) {
        Random rand = new Random();
        int dx = rand.nextInt(3) - 1; // -1, 0, or 1
        int dy = rand.nextInt(3) - 1;
        int x = point.getX() + dx;
        int y = point.getY() + dy;

        return new Point(x, y);
    }

    // MouseListener interface methods

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent) {
        setDrawColor(Color.YELLOW); // Change overlay color when mouse is pressed
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent) {
        setDrawColor(Color.LIGHT_GRAY); // Reset overlay color when mouse is released
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent) {
        Point exit = new Point(mouseEvent.getX(), mouseEvent.getY());
        Point output = new Point(0, 0);

        // Adjust the startPoint based on where the mouse exited the component
        if (exit.getY() < 0 && exit.getX() > 0) {
            output = new Point(exit.getX(), 0);
        } else if (exit.getX() < 0 && exit.getY() > 0) {
            output = new Point(0, exit.getY());
        } else if (exit.getY() > client.getCanvasHeight() && exit.getX() > 0) {
            output = new Point(exit.getX(), client.getCanvasHeight());
        } else if (exit.getX() > client.getCanvasWidth() && exit.getY() > 0) {
            output = new Point(client.getCanvasWidth(), exit.getY());
        }

        setStartPoint(output); // Update the starting point for the next movement
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent) {
        calculateMousePosition(mouseEvent); // Update mouse coordinates
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent) {
        calculateMousePosition(mouseEvent); // Update mouse coordinates
        if (getMouseCoords() != null) {
            setStartPoint(getMouseCoords()); // Update starting point for movements
        }
        return mouseEvent;
    }
}
