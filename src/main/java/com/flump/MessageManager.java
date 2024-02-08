package com.flump;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.util.ColorUtil;

import java.awt.*;

public class MessageManager {

    public void sendMessage(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.BLACK), "");
    }
    public void sendInfo(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.BLUE), "");
    }
    public void sendWarning(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.YELLOW), "");
    }
    public void sendERROR(Client client, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag(message, Color.RED), "");
    }

}
