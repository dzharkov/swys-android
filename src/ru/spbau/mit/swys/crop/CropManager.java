package ru.spbau.mit.swys.crop;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

public class CropManager {
    private static final int RESULT_BITMAP_SIZE = 400;

    public Bitmap cropBitmap(Bitmap srcBitmap, Point[] points) {

        Point topLeft     = points[0];
        Point topRight    = points[1];
        Point bottomRight = points[2];
        Point bottomLeft  = points[3];

        OrientedSegment topSide    = new OrientedSegment(topLeft, topRight);
        OrientedSegment rightSide  = new OrientedSegment(topRight, bottomRight);
        OrientedSegment bottomSide = new OrientedSegment(bottomLeft, bottomRight);
        OrientedSegment leftSide   = new OrientedSegment(topLeft, bottomLeft);

        int[] srcBitmapColors = new int[srcBitmap.getWidth() * srcBitmap.getHeight()];
        srcBitmap.getPixels(
                srcBitmapColors,
                0, srcBitmap.getWidth(),
                0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()
        );

        int[] resultBitmapColors = new int[RESULT_BITMAP_SIZE * RESULT_BITMAP_SIZE];

        PointF onTopSide = new PointF();
        PointF onLeftSide = new PointF();
        PointF onRightSide = new PointF();
        PointF onBottomSide = new PointF();
        PointF srcPoint = new PointF();

        for (int row = 0; row < RESULT_BITMAP_SIZE; row++) {
            float verticalProportion = getProportionByIndex(row);

            leftSide.pointAt(verticalProportion, onLeftSide);
            rightSide.pointAt(verticalProportion, onRightSide);

            for (int column = 0; column < RESULT_BITMAP_SIZE; column++) {
                float horizontalProportion = getProportionByIndex(column);

                topSide.pointAt(horizontalProportion, onTopSide);
                bottomSide.pointAt(horizontalProportion, onBottomSide);

                GeomUtils.segmentsIntersection(
                        onTopSide, onBottomSide,
                        onLeftSide, onRightSide,
                        srcPoint
                );

                resultBitmapColors[row * RESULT_BITMAP_SIZE + column] = getColorFromSource(
                        srcPoint,
                        srcBitmapColors,
                        srcBitmap.getWidth(), srcBitmap.getHeight()
                );

            }
        }

        return Bitmap.createBitmap(resultBitmapColors, RESULT_BITMAP_SIZE, RESULT_BITMAP_SIZE, srcBitmap.getConfig());
    }

    private int getColorFromSource(PointF p, int[] srcBitmapColors, int bitmapWidth, int bitmapHeight) {
        int i = Math.round(p.y);
        int j = Math.round(p.x);

        return srcBitmapColors[i * bitmapWidth + j];
    }

    private float getProportionByIndex(int index) {
        return (float) index / (float) RESULT_BITMAP_SIZE;
    }
}
