package com.flump;

import net.runelite.api.Point;
import java.awt.*;
import java.util.Random;

/**
 * Utility class containing mathematical methods used throughout the plugin.
 */
public class MathStuff {

    /**
     * Generates a random point within a rectangle, avoiding the edges for more natural mouse movements.
     *
     * @param rect The rectangle within which to generate the point.
     * @return A random point within the specified rectangle.
     */
    public static Point randomRectanglePoint(Rectangle rect) {
        Random rand = new Random();

        int x = rect.x + (int) (0.3 * rect.width) + rand.nextInt((int) (0.4 * rect.width));
        int y = rect.y + (int) (0.3 * rect.height) + rand.nextInt((int) (0.4 * rect.height));

        return new Point(x, y);
    }

    /**
     * Checks if the actual value is within a specified tolerance of the target value.
     *
     * @param actual    The actual value.
     * @param target    The target value.
     * @param tolerance The acceptable tolerance.
     * @return True if within tolerance, false otherwise.
     */
    public static boolean isWithinTolerance(int actual, int target, int tolerance) {
        return Math.abs(actual - target) <= tolerance;
    }
}
