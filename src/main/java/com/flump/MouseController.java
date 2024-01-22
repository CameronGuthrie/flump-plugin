package com.flump;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.input.MouseListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

@Singleton
public class MouseController implements MouseListener {

    private final Client client;

    @Inject
    private MouseController(Client client) {
        this.client = client;
        System.out.println("MouseController instance created: " + this.hashCode());
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
        virtualLeftClick(client.getCanvas(), client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), MouseEvent.BUTTON1);
    }

    public void rightClick() {
        virtualLeftClick(client.getCanvas(), client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), MouseEvent.BUTTON2);
    }

    public void wheelClick() {
        virtualLeftClick(client.getCanvas(), client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), MouseEvent.MOUSE_WHEEL);
    }

    private static void virtualLeftClick(Component target, int x, int y, int button) {
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

    private static void simulateHumanLikeMovement(Component target, Point startPoint, Point endPoint) {
        try {
            // Add reaction time delay
            Thread.sleep(100 + new Random().nextInt(100));

            // Define control points for bezier curve
            Point controlPoint1 = getRandomControlPoint(startPoint, endPoint);
            Point controlPoint2 = getRandomControlPoint(startPoint, endPoint);

            // Movement duration and current time
            final long duration = 200; // Adjust duration as needed
            long startTime = System.currentTimeMillis();

            // Animate the movement
            while (System.currentTimeMillis() - startTime < duration) {
                double t = (double) (System.currentTimeMillis() - startTime) / duration;
                t = easeInOut(t); // Apply easing function for more natural speed

                // Calculate next point
                Point nextPoint = calculateBezierPoint(t, startPoint, controlPoint1, controlPoint2, endPoint);
                virtualMove(target, new Point(nextPoint.getX(), nextPoint.getY()));

                // Add micro-movements
                if (t > 0.5) { // Adding micro movements in the second half of the movement
                    addMicroMovements(target, nextPoint);
                }

                // Control the movement speed
                Thread.sleep(10);
            }

            // Ensure the mouse ends at the target point
            virtualMove(target, new Point(endPoint.getX(), endPoint.getY()));
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    private static void addMicroMovements(Component target, Point point) throws InterruptedException {
        Random rand = new Random();
        int dx = rand.nextInt(3) - 1;
        int dy = rand.nextInt(3) - 1;
        int x = point.getX() + dx;
        int y = point.getY() + dy;

        Point nextPoint = new Point(x, y);
        virtualMove(target, nextPoint);
        Thread.sleep(5);
    }

    private Point randomRectanglePoint(Rectangle rect) {
        Random rand = new Random();

        int x = rect.x + rand.nextInt(rect.width);
        int y = rect.y + rand.nextInt(rect.height);

        return new Point(x, y);
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent) {
        setDrawColor(Color.YELLOW);
        System.out.println("Mouse Pressed: Color set to YELLOW");
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent) {
        setDrawColor(Color.LIGHT_GRAY);
        System.out.println("Mouse Released: Color set to LIGHT_GRAY");
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

        if (getMouseCoords() != null){
            setStartPoint(getMouseCoords());
        } else {
            setStartPoint(new Point(0,0));
        }

        return mouseEvent;
    }

}
