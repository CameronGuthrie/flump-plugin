package com.flump;

import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;

/**
 * Manages the camera movements in the game, such as yaw, pitch, and zoom.
 */
@Singleton
public class CameraController {

    private final Client client;
    private final KeyboardController keyboardController;

    private final MouseController mouseController;

    private final int yawTolerance = 50;
    private final int pitchTolerance = 20;
    private final int zoomTolerance = 50;
    private volatile boolean adjustYawDone = false;
    private volatile boolean adjustPitchDone = false;

    @Inject
    public CameraController(Client client, KeyboardController keyboardController, MouseController mouseController) {
        this.client = client;
        this.keyboardController = keyboardController;
        this.mouseController = mouseController;
    }

    /**
     * Adjusts the camera to a specified yaw and pitch.
     * @param targetYaw The target yaw.
     * @param targetPitch The target pitch.
     */
    public void adjustCamera(int targetYaw, int targetPitch) {
        // Logic to adjust camera.
        adjustYawDone = false;
        adjustPitchDone = false;

        // Use a SwingWorker to manage the adjustment process in the background
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Start adjusting yaw in a separate thread
                new Thread(() -> {
                    adjustOrientation(VK_LEFT, VK_RIGHT, targetYaw, yawTolerance, true);
                    adjustYawDone = true;
//                    checkAndFinalizeAdjustment();
                }).start();

                // Start adjusting pitch in the main SwingWorker thread for simplicity
                adjustOrientation(VK_DOWN, VK_UP, targetPitch, pitchTolerance, false);
                adjustPitchDone = true;
//                checkAndFinalizeAdjustment();

                return null;
            }
        };
        worker.execute();
    }

    private int facing(Compass c) {

        int direction;

        switch(c) {
            case NORTH:
                direction = 0;
                break;
            case NORTHEAST:
                direction = 1792;
                break;
            case EAST:
                direction = 1536;
                break;
            case SOUTHEAST:
                direction = 1280;
                break;
            case SOUTH:
                direction = 1024;
                break;
            case SOUTHWEST:
                direction = 768;
                break;
            case WEST:
                direction = 512;
                break;
            case NORTHWEST:
                direction = 256;
                break;
            default:
                System.out.println("DEFAULT CASE");
                direction = 0;
                break;
        }

        return direction;
    }

    /**
     * Adjusts the zoom level of the camera.
     * @param targetZoom The target zoom level.
     */
    public void adjustZoom(int targetZoom) {
        // Logic to adjust zoom.
        SwingWorker<Void, Void> zoomWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                boolean zoomIn = client.getScale() < targetZoom;
                while (!MathStuff.isWithinTolerance(client.getScale(), targetZoom, zoomTolerance) && !isCancelled()) {
                    // Simulate mouse wheel scrolling to adjust zoom
                    if (zoomIn) {
                        mouseController.virtualScrollUp(); // Assuming true zooms in
                    } else {
                        mouseController.virtualScrollDown();  // Assuming false zooms out
                    }
                    try {
                        Thread.sleep(100); // Adjust the sleep time as needed for responsiveness
                    } catch (InterruptedException e) {
                        // Handle interruption appropriately
                        break;
                    }
                }
                return null;
            }
        };
        zoomWorker.execute();
    }

    private boolean shouldTurnRightToTargetYaw(int current, int target) {
        int diff = target - current;
        if (diff < 0) {
            diff += 2047 + 1; // Correct negative difference for circular logic
        }
        // If the difference is less than or equal to half the maximum, it's shorter to increase (turn right).
        // Otherwise, it's shorter to decrease (turn left).
        return diff <= (2047 + 1) / 2;
    }

    private void adjustOrientation(int decreaseKey, int increaseKey, int target, int tolerance, boolean isYaw) {

        boolean increase; // Determines if we should increase (true) or decrease (false) the value

        if (isYaw) {
            increase = shouldTurnRightToTargetYaw(client.getCameraYaw(), target);
        } else {
            // For pitch or other non-circular values, determine direction based on comparison
            increase = client.getCameraPitch() < target;
        }

        int keyCodeToPress = increase ? increaseKey : decreaseKey;

//        int keyCodeToPress = isYaw ? (client.getCameraYaw() < target ? increaseKey : decreaseKey)
//                : (client.getCameraPitch() < target ? increaseKey : decreaseKey);

        keyboardController.press(keyCodeToPress);

        while (!MathStuff.isWithinTolerance(isYaw ? client.getCameraYaw() : client.getCameraPitch(), target, tolerance)) {
            try {
                Thread.sleep(10); // Adjust based on responsiveness needs
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            // For yaw, continuously check if we need to switch direction due to circular logic, unlikely but safe to check
            if (isYaw && increase != shouldTurnRightToTargetYaw(client.getCameraYaw(), target)) {
                keyboardController.release(keyCodeToPress); // Release the previous direction key
                increase = !increase; // Switch direction
                keyCodeToPress = increase ? increaseKey : decreaseKey;
                keyboardController.press(keyCodeToPress); // Press the new direction key
            }
        }

        keyboardController.release(keyCodeToPress);
    }

}
