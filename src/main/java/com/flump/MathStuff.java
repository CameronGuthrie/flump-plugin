package com.flump;

import net.runelite.api.Point;

import java.awt.*;
import java.util.Random;

public class MathStuff {

    public Point randomRectanglePoint(Rectangle rect) {
        Random rand = new Random();

        int x = rect.x + (int)(0.3*rect.width) + rand.nextInt((int)(0.3*rect.width));
        int y = rect.y + (int)(0.3*rect.height) + rand.nextInt((int)(0.3*rect.height));

        return new Point(x, y);
    }

}
