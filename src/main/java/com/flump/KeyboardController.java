package com.flump;

import net.runelite.api.Client;
import net.runelite.client.input.KeyListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Controls keyboard inputs within the game by simulating key presses and releases.
 */
@Singleton
public class KeyboardController implements KeyListener {

    private final Client client;

    /**
     * Constructor for KeyboardController.
     *
     * @param client The game client instance.
     */
    @Inject
    private KeyboardController(Client client) {
        this.client = client;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Typed: " + KeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Released: " + KeyEvent.getKeyText(e.getKeyCode()) + ", KeyCode: " + e.getKeyCode());
    }

    /**
     * Simulates pressing a key.
     *
     * @param keyCode The key code to press.
     */
    public void press(int keyCode) {
        virtualKeyPress(client.getCanvas(), keyCode);
    }

    /**
     * Simulates releasing a key.
     *
     * @param keyCode The key code to release.
     */
    public void release(int keyCode) {
        virtualKeyRelease(client.getCanvas(), keyCode);
    }

    /**
     * Simulates a key press event on the game canvas.
     *
     * @param target  The component to dispatch the event to.
     * @param keyCode The key code to simulate.
     */
    private static void virtualKeyPress(Component target, int keyCode) {
        long time = System.currentTimeMillis();
        KeyEvent press = new KeyEvent(target, KeyEvent.KEY_PRESSED, time, 0, keyCode, KeyEvent.CHAR_UNDEFINED);
        target.dispatchEvent(press);
    }

    /**
     * Simulates a key release event on the game canvas.
     *
     * @param target  The component to dispatch the event to.
     * @param keyCode The key code to simulate.
     */
    private static void virtualKeyRelease(Component target, int keyCode) {
        long time = System.currentTimeMillis();
        KeyEvent release = new KeyEvent(target, KeyEvent.KEY_RELEASED, time, 0, keyCode, KeyEvent.CHAR_UNDEFINED);
        target.dispatchEvent(release);
    }
}
