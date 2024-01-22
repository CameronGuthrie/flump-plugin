package com.flump;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.input.MouseListener;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@PluginDescriptor(
        name = "Test Mouse Plugin",
        description = "A plugin that is testing mouse event listeners",
        tags = {"", ""}
)
@SuppressWarnings("unused")
public class MousePlugin_OLD extends Plugin implements MouseListener {

    @Inject
    private Client client;

    @Inject
    private MouseManager mouseManager;

    @Inject
    private MouseControllerOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    @Getter
    @Setter
    private Point mouseCoords;

    @Getter
    @Setter
    private Color drawColor = Color.LIGHT_GRAY;

    @Getter
    @Setter
    private Point startPoint = new Point(0,0);

    @Getter
    @Setter
    private Point moveMe;

    private Timer timer;

    @Override
    protected void startUp() {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                for (NPC npc : client.getNpcs()) {
                    if (npc != null && npc.getId() == 306) {
                        setMoveMe(Perspective.localToCanvas(client, new LocalPoint(npc.getLocalLocation().getX() - 3, npc.getLocalLocation().getY()), npc.getWorldLocation().getPlane()));
                    }
                }
                System.out.println(getStartPoint());
                System.out.println(getMoveMe());
                //simulateHumanLikeMovement(client.getCanvas(), getStartPoint(), getMoveMe());
                virtualClick(client.getCanvas(), getMoveMe().getX(), getMoveMe().getY());
                setStartPoint(client.getMouseCanvasPosition());
            }
        };
        timer.schedule(task, 20000);

        overlayManager.add(overlay);
        mouseManager.registerMouseListener(0, this);
        log.info("Custom plugin started!");
    }

    @Override
    protected void shutDown() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        overlayManager.remove(overlay);
        mouseManager.unregisterMouseListener(this);
        log.info("Custom plugin stopped!");
    }

    private void calculateMousePosition(MouseEvent mouseEvent) {
        mouseCoords = new Point(mouseEvent.getX(), mouseEvent.getY());
    }

    private static void virtualClick(Component target, int x, int y) {
        MouseEvent press,release,click;

        Point point = new Point(x,y);
        long time = System.currentTimeMillis();

        press   = new MouseEvent(target, MouseEvent.MOUSE_PRESSED, time, 0, x, y, point.getX(), point.getY(), 1, false, MouseEvent.BUTTON1);
        release = new MouseEvent(target, MouseEvent.MOUSE_RELEASED, time, 0, x, y, point.getX(), point.getY(), 1, false, MouseEvent.BUTTON1);
        click   = new MouseEvent(target, MouseEvent.MOUSE_CLICKED, time, 0, x, y, point.getX(), point.getY(), 1, false, MouseEvent.BUTTON1);

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

    public static void simulateHumanLikeMovement(Component target, Point startPoint, Point endPoint) {
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

//        Point endPoint = randomRectanglePoint(client.getWidget(ComponentID.FIXED_VIEWPORT_INVENTORY_ICON).getBounds());
//        simulateHumanLikeMovement(client.getCanvas(), getStartPoint(), endPoint);
//        setStartPoint(client.getMouseCanvasPosition());

        if ( client.getSelectedSceneTile() != null){
            System.out.println("Currently selected tile: " + client.getSelectedSceneTile().getWorldLocation());
        }

        if ( client.getSelectedWidget() != null){
            System.out.println("Currently selected widget: " + client.getSelectedWidget());
        }



        /*
        for (NPC npc : client.getNpcs()) {
            if (npc != null && npc.getId() == 306) {
                //System.out.println(Perspective.localToCanvas(client, npc.getLocalLocation(), npc.getWorldLocation().getPlane())); //npc.getLogicalHeight()));
                setMoveMe(Perspective.localToCanvas(client, npc.getLocalLocation(), npc.getWorldLocation().getPlane()));
                System.out.println("NPC {");
                System.out.println("name: " + npc.getName() + ",");
                System.out.println("level: " + npc.getCombatLevel() + ",");
                System.out.println("id: " + npc.getId() + ",");
                System.out.println("local location: " + npc.getLocalLocation() + ",");
                System.out.println("world area: " + npc.getWorldArea() + ",");
                System.out.println("world location: " + npc.getWorldLocation() + ",");
                System.out.println("canvas tile polygon: " + npc.getCanvasTilePoly());
                System.out.println("}\n");

            }
        }

        System.out.println("Local Player {");
        System.out.println("name: " + client.getLocalPlayer().getName() + ",");
        System.out.println("level: " + client.getLocalPlayer().getCombatLevel() + ",");
        System.out.println("id: " + client.getLocalPlayer().getId() + ",");
        System.out.println("local location: " + client.getLocalPlayer().getLocalLocation() + ",");
        System.out.println("world area: " + client.getLocalPlayer().getWorldArea() + ",");
        System.out.println("world location: " + client.getLocalPlayer().getWorldLocation() + ",");
        System.out.println("canvas tile polygon: " + client.getLocalPlayer().getCanvasTilePoly());
        System.out.println("}\n");
        */

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

        if (client.getMouseCanvasPosition() != null){
            setStartPoint(client.getMouseCanvasPosition());
        } else {
            setStartPoint(new Point(0,0));
        }

        return mouseEvent;
    }

}
