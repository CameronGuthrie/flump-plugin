package com.flump;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;

@Singleton
public class KeyboardController implements KeyListener {

    private final InventoryManager inventoryManager;

    @Inject
    private KeyboardController(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
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
        if (e.getKeyCode() == e.VK_DOWN) {
            inventoryManager.mouseOver();
        }
    }

//    @Override
//    public boolean isEnabledOnLoginScreen() {
//        return KeyListener.super.isEnabledOnLoginScreen();
//    }
}
