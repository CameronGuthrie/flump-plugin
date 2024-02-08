package com.flump;

import net.runelite.api.Client;

import javax.swing.*;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_UP;


public class CameraController {

    public void moveCamera(Client client, KeyboardController keyboardController) {
        // int target = random number between 235 and 285
        // Using a fixed value of 250 for demonstration purposes

        if (client.getCameraPitch() > 250) {
            do {
                keyboardController.press(VK_DOWN);
            } while (client.getCameraPitch() != 250);
            keyboardController.release(VK_DOWN);
        }

        if (client.getCameraPitch() < 250) {
            do {
                keyboardController.press(VK_UP);
            } while (client.getCameraPitch() != 250);
            keyboardController.release(VK_UP);
        }
    }

}
