package com.flump;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.input.KeyListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.KeyEvent;

@Singleton
public class KeyboardController implements KeyListener {

    private final Client client;
//    private final CameraController cameraController;
    //private final InventoryManager inventoryManager;


    @Inject
    private KeyboardController( Client client) { //CameraController cameraController, Client client) { //, CameraController cameraController) {

        //this.inventoryManager = inventoryManager;
        this.client = client;
//        this.cameraController = cameraController;
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

        System.out.println("Released: " + KeyEvent.getKeyText(e.getKeyCode()) + ", KeyCode: " +  e.getKeyCode());
//        if (e.getKeyCode() == e.VK_SPACE) {
//            cameraController.adjustCamera(250);//(client, this);
//        }

    }

    public void press(int keyCode) {
        virtualKeyPress(client.getCanvas(), keyCode);
    }
    public void release(int keyCode) {
        virtualKeyRelease(client.getCanvas(), keyCode);
    }

    private static void virtualKeyPress(Component target, int keyCode) {
        long time = System.currentTimeMillis();

        KeyEvent press = new KeyEvent(target, KeyEvent.KEY_PRESSED, time, 0, keyCode, KeyEvent.CHAR_UNDEFINED);
        target.dispatchEvent(press);
    }

    private static void virtualKeyRelease(Component target, int keyCode) {
        long time = System.currentTimeMillis();

        KeyEvent release = new KeyEvent(target, KeyEvent.KEY_RELEASED, time, 0, keyCode, KeyEvent.CHAR_UNDEFINED);
        target.dispatchEvent(release);
    }

//    @Override
//    public boolean isEnabledOnLoginScreen() {
//        return KeyListener.super.isEnabledOnLoginScreen();
//    }

}
