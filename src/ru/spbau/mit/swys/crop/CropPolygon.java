package ru.spbau.mit.swys.crop;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public class CropPolygon {

    public static final int POINT_RADIUS = 20;
    private static final int HIT_RADIUS_SQR = POINT_RADIUS * POINT_RADIUS * 2;

    public static final int NULL_POINT_ID = -1;

    private static final int POINTS_COUNT = 4;

    private Rect bmLayoutRect;
    private Matrix bmLayoutMatrix;
    private Matrix bmLayoutInvertMatrix = new Matrix();
    private Point[] points = new Point[POINTS_COUNT];

    public CropPolygon(Matrix bmLayoutMatrix, Rect bmRect) {
        this.bmLayoutMatrix = bmLayoutMatrix;

        RectF bmRectF = new RectF(bmRect.left, bmRect.top, bmRect.right, bmRect.bottom);

        bmLayoutMatrix.mapRect(bmRectF);

        bmLayoutRect = new Rect(
                Math.round(bmRectF.left),
                Math.round(bmRectF.top),
                Math.round(bmRectF.right),
                Math.round(bmRectF.bottom)
        );

        bmLayoutMatrix.invert(bmLayoutInvertMatrix);
    }

    public Rect getBmLayoutRect() {
        return bmLayoutRect;
    }

    public void initDefaultPosition() {
        points[0] = new Point(bmLayoutRect.centerX() - bmLayoutRect.width() / 4, bmLayoutRect.centerY() - bmLayoutRect.height() / 4);
        points[1] = new Point(bmLayoutRect.centerX() - bmLayoutRect.width() / 4, bmLayoutRect.centerY() + bmLayoutRect.height() / 4);
        points[2] = new Point(bmLayoutRect.centerX() + bmLayoutRect.width() / 4, bmLayoutRect.centerY() + bmLayoutRect.height() / 4);
        points[3] = new Point(bmLayoutRect.centerX() + bmLayoutRect.width() / 4, bmLayoutRect.centerY() - bmLayoutRect.height() / 4);
    }

    public Point[] getLayoutPoints() {
        return points;
    }

    public Point[] getCropPoints() {
        Point[] sortedPoints = new Point[POINTS_COUNT];

        int leftTopIndex = 0;

        for (int i = 0; i < POINTS_COUNT; i++) {
            int prev = (i - 1 + POINTS_COUNT) % POINTS_COUNT;
            int next = (i + 1) % POINTS_COUNT;

            if (points[i].y > points[prev].y && points[next].y > points[i - 1].y) {
                leftTopIndex = i;
                break;
            }
        }

        float[] pointsForMatrix = new float[2 * POINTS_COUNT];

        for (int i = 0; i < POINTS_COUNT; i++) {
            pointsForMatrix[2 * i] = points[i].x;
            pointsForMatrix[2 * i + 1] = points[i].y;
        }

        bmLayoutInvertMatrix.mapPoints(pointsForMatrix);

        for (int i = 0, j = leftTopIndex; i < POINTS_COUNT; i++, j = (j + 1) % POINTS_COUNT) {
            sortedPoints[i] = new Point((int) pointsForMatrix[2 * j], (int) pointsForMatrix[2 * j + 1]);
        }

        return sortedPoints;
    }

    public int getHitPointId(Point downPoint) {
        for (int i = 0; i < POINTS_COUNT; i++) {
            Point p = points[i];
            int sqrDist = (p.x - downPoint.x) * (p.x - downPoint.x) + (p.y - downPoint.y) * (p.y - downPoint.y);

            if (sqrDist <= HIT_RADIUS_SQR) {
                return i;
            }
        }

        return NULL_POINT_ID;
    }

    public boolean movePoint(int pointId, int x, int y) {

        if (!bmLayoutRect.contains(x, y)) {
            return false;
        }

        int oldX = points[pointId].x;
        int oldY = points[pointId].y;

        points[pointId].set(x, y);

        if (!isConvex()) {
            points[pointId].set(oldX, oldY);

            return false;
        }

        return true;
    }

    private int angleSign(int p) {
        int p1 = (p - 1 + POINTS_COUNT) % POINTS_COUNT;
        int p2 = (p + 1) % POINTS_COUNT;

        int x1 = points[p1].x - points[p].x;
        int y1 = points[p1].y - points[p].y;
        int x2 = points[p2].x - points[p].x;
        int y2 = points[p2].y - points[p].y;

        int res = (x1 * y2 - x2 * y1);

        return res > 0 ? 1 : (res == 0 ? 0 : -1);
    }

    private boolean isConvex() {

        for (int p = 0; p < POINTS_COUNT - 1; p++) {
            //if different signs of angles
            if (angleSign(p) * angleSign(p + 1) <= 0) {
                return false;
            }
        }

        return true;
    }
}
