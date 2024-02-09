package com.flump;

import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_UP;

@Singleton
public class CameraController {

    private final Client client;
    private final KeyboardController keyboardController;

    @Inject
    public CameraController(Client client, KeyboardController keyboardController) {
        this.client = client;
        this.keyboardController = keyboardController;
    }

    public void adjustCamera(int targetPitch) {
        // SwingWorker to handle background processing
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (client.getCameraPitch() > targetPitch) {
                    simulateKeyHold(VK_DOWN, targetPitch);
                } else if (client.getCameraPitch() < targetPitch) {
                    simulateKeyHold(VK_UP, targetPitch);
                }
                return null;
            }
        };
        worker.execute();
    }

    private void simulateKeyHold(int keyCode, int targetPitch) {
        // Define a tolerance for the camera pitch to be considered "close enough" to the target
        final int tolerance = 10;

        keyboardController.press(keyCode);
        // Logic to hold the key down until the camera pitch is adjusted to the target
        // This loop needs to be non-blocking and should ideally run in a background thread
        while (!MathStuff.isWithinTolerance(client.getCameraPitch(), targetPitch, tolerance) && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10); // Small delay to check the condition without hogging the CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                break; // Exit the loop if the thread is interrupted
            }
        }
        keyboardController.release(keyCode);
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
