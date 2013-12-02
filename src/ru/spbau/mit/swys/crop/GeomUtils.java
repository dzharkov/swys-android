package ru.spbau.mit.swys.crop;

import android.graphics.Point;

public class GeomUtils {
    public static int squaredDist(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    public static int vectorsMultiplication(int x1, int y1, int x2, int y2) {
        return x1 * y2 - x2 * y1;
    }

    public static int angleSignBetweenVectors(int x1, int y1, int x2, int y2) {
        int res = vectorsMultiplication(x1, y1, x2, y2);

        return res > 0 ? 1 : (res == 0 ? 0 : -1);
    }
}
