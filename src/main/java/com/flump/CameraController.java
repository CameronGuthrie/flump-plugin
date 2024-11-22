package com.flump;

import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.awt.event.KeyEvent.*;

/**
 * Controls the camera movements within the game, including yaw, pitch, and zoom adjustments.
 */
@Singleton
public class CameraController {

    private final Client client;
    private final KeyboardController keyboardController;
    private final MouseController mouseController;

    // Tolerances for camera adjustments to prevent over-adjusting
    private final int yawTolerance = 50;
    private final int pitchTolerance = 20;
    private final int zoomTolerance = 50;

    // Atomic booleans to track if camera adjustments are completed
    private final AtomicBoolean adjustYawDone = new AtomicBoolean(false);
    private final AtomicBoolean adjustPitchDone = new AtomicBoolean(false);

    /**
     * Constructor for CameraController.
     *
     * @param client              The game client instance.
     * @param keyboardController  Controller to simulate keyboard inputs.
     * @param mouseController     Controller to simulate mouse inputs.
     */
    @Inject
    public CameraController(Client client, KeyboardController keyboardController, MouseController mouseController) {
        this.client = client;
        this.keyboardController = keyboardController;
        this.mouseController = mouseController;
    }

    /**
     * Adjusts the camera to a specified yaw and pitch asynchronously.
     *
     * @param targetYaw   The target yaw (horizontal rotation).
     * @param targetPitch The target pitch (vertical rotation).
     */
    public void adjustCamera(int targetYaw, int targetPitch) {
        adjustYawDone.set(false);
        adjustPitchDone.set(false);

        // Use a SwingWorker to perform camera adjustments without blocking the main thread
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Adjust yaw first
                adjustOrientation(VK_LEFT, VK_RIGHT, targetYaw, yawTolerance, true);
                adjustYawDone.set(true);

                // Then adjust pitch
                adjustOrientation(VK_DOWN, VK_UP, targetPitch, pitchTolerance, false);
                adjustPitchDone.set(true);
                return null;
            }
        };
        worker.execute();
    }

    /**
     * Adjusts the zoom level of the camera asynchronously.
     *
     * @param targetZoom The target zoom level.
     */
    public void adjustZoom(int targetZoom) {
        SwingWorker<Void, Void> zoomWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                boolean zoomIn = client.getScale() < targetZoom; // Determine if we need to zoom in or out
                try {
                    while (!MathStuff.isWithinTolerance(client.getScale(), targetZoom, zoomTolerance)) {
                        if (zoomIn) {
                            mouseController.virtualScrollUp(); // Simulate scroll up to zoom in
                        } else {
                            mouseController.virtualScrollDown(); // Simulate scroll down to zoom out
                        }
                        Thread.sleep(100); // Sleep to prevent overwhelming the client
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                }
                return null;
            }
        };
        zoomWorker.execute();
    }

    /**
     * Determines if the camera should turn right to reach the target yaw based on the shortest path.
     *
     * @param current Current yaw value.
     * @param target  Target yaw value.
     * @return True if the camera should turn right, false otherwise.
     */
    private boolean shouldTurnRightToTargetYaw(int current, int target) {
        int diff = (target - current + 2048) % 2048;
        return diff <= 1024;
    }

    /**
     * Adjusts the camera's orientation (yaw or pitch) by simulating key presses.
     *
     * @param decreaseKey Key code for decreasing the orientation (e.g., VK_LEFT or VK_DOWN).
     * @param increaseKey Key code for increasing the orientation (e.g., VK_RIGHT or VK_UP).
     * @param target      The target orientation value.
     * @param tolerance   Acceptable tolerance for stopping the adjustment.
     * @param isYaw       True if adjusting yaw, false if adjusting pitch.
     */
    private void adjustOrientation(int decreaseKey, int increaseKey, int target, int tolerance, boolean isYaw) {
        boolean increase = isYaw
                ? shouldTurnRightToTargetYaw(client.getCameraYaw(), target)
                : client.getCameraPitch() < target;

        int keyCodeToPress = increase ? increaseKey : decreaseKey;

        // Simulate key press to start adjusting the camera
        keyboardController.press(keyCodeToPress);
        try {
            while (!MathStuff.isWithinTolerance(
                    isYaw ? client.getCameraYaw() : client.getCameraPitch(), target, tolerance)) {
                Thread.sleep(10); // Small delay to prevent overloading the client

                // If the direction needs to change during adjustment
                if (isYaw && increase != shouldTurnRightToTargetYaw(client.getCameraYaw(), target)) {
                    keyboardController.release(keyCodeToPress); // Release current key
                    increase = !increase; // Switch direction
                    keyCodeToPress = increase ? increaseKey : decreaseKey;
                    keyboardController.press(keyCodeToPress); // Press new key
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle interruption
        } finally {
            keyboardController.release(keyCodeToPress); // Ensure the key is released after adjustment
        }
    }
}
