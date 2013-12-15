package ru.spbau.mit.swys.crop;

import android.graphics.Point;
import android.graphics.PointF;

public class GeomUtils {
    private static final double EPS = 1e-9;

    //for optimisation's purposes
    private static final Line l1 = new Line();
    private static final Line l2 = new Line();

    public static boolean linesIntersection(PointF p1, PointF p2, PointF p3, PointF p4, PointF dst) {
        l1.applyFrom(p1, p2);
        l2.applyFrom(p3, p4);

        return l1.intersection(l2, dst);
    }

    public static void segmentsIntersection(PointF p1, PointF p2, PointF p3, PointF p4, PointF dst) {
        linesIntersection(p1, p2, p3, p4, dst);

        //TODO: check if each of segments actually contains result
    }

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

    private static class Line {
        private float a;
        private float b;
        private float c;

        private void applyFrom(PointF p1, PointF p2) {
            this.a = p1.y - p2.y;
            this.b = p2.x - p1.x;
            this.c = -a * p1.x - b * p1.y;
        }

        private static float det(float a, float b, float c, float d) {
            return a * d - b * c;
        }

        boolean intersection(Line other, PointF dst) {
            float denominatorDet = det(a, b, other.a, other.b);

            if (Math.abs(denominatorDet) < EPS) {
                return false;
            }

            dst.x = -det(c, b, other.c, other.b) / denominatorDet;
            dst.y = -det(a, c, other.a, other.c) / denominatorDet;

            return true;
        }
    }
}
