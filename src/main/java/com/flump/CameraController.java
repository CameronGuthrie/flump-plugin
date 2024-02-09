package com.flump;

import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;

@Singleton
public class CameraController {

    private final Client client;
    private final KeyboardController keyboardController;

    private int yawTolerance = 50;
    private int pitchTolerance = 20;

    @Inject
    public CameraController(Client client, KeyboardController keyboardController) {
        this.client = client;
        this.keyboardController = keyboardController;
    }

    public void adjustCamera(int targetYaw, int targetPitch) {
        // SwingWorker to handle background processing
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Adjust yaw
                if (!MathStuff.isWithinTolerance(client.getCameraYaw(), targetYaw, yawTolerance)) {
                    simulateKeyHoldForOrientation(VK_LEFT, VK_RIGHT, targetYaw, true); // Assuming VK_LEFT and VK_RIGHT adjust yaw
                }
                // Adjust pitch
                if (!MathStuff.isWithinTolerance(client.getCameraPitch(), targetPitch, pitchTolerance)) {
                    simulateKeyHoldForOrientation(VK_DOWN, VK_UP, targetPitch, false); // Assuming VK_DOWN and VK_UP adjust pitch
                }
                return null;
            }
        };
        worker.execute();
    }

    private void simulateKeyHoldForOrientation(int decreaseKey, int increaseKey, int target, boolean isYaw) {
        final int tolerance = isYaw ? yawTolerance : pitchTolerance;
        int currentValue = isYaw ? client.getCameraYaw() : client.getCameraPitch();

        // Decide which key to press based on whether we need to increase or decrease the value
        int keyCodeToPress = currentValue < target ? increaseKey : decreaseKey;
        keyboardController.press(keyCodeToPress);

        // Keep checking until the target is within tolerance
        while (!MathStuff.isWithinTolerance(currentValue, target, tolerance) && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10); // Small delay to avoid high CPU usage
                currentValue = isYaw ? client.getCameraYaw() : client.getCameraPitch(); // Update current value
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        keyboardController.release(keyCodeToPress);
    }

//    public void moveCamera(Client client, KeyboardController keyboardController) {
//        // int target = random number between 235 and 285
//        // Using a fixed value of 250 for demonstration purposes
//
//        if (client.getCameraPitch() > 250) {
//            do {
//                keyboardController.press(VK_DOWN);
//            } while (client.getCameraPitch() != 250);
//            keyboardController.release(VK_DOWN);
//        }
//
//        if (client.getCameraPitch() < 250) {
//            do {
//                keyboardController.press(VK_UP);
//            } while (client.getCameraPitch() != 250);
//            keyboardController.release(VK_UP);
//        }
//    }

}
