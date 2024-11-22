package com.flump;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.util.ColorUtil;

import java.awt.*;
/**
 * Manages the sending of different types of messages in the game chat.
 */
public class MessageManager {

    /**
     * Sends a standard game message to the chat.
     * @param client The game client.
     * @param message The message to be sent.
     */
    public void sendMessage(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.BLACK), "");
    }

    /**
     * Sends an informational message to the chat.
     * @param client The game client.
     * @param message The informational message to be sent.
     */
    public void sendInfo(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.BLUE), "");
    }

    /**
     * Sends a warning message to the chat.
     * @param client The game client.
     * @param message The warning message to be sent.
     */
    public void sendWarning(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.YELLOW), "");
    }

    /**
     * Sends an error message to the chat.
     * @param client The game client.
     * @param message The error message to be sent.
     */
    public void sendERROR(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.RED), "");
    }

}
