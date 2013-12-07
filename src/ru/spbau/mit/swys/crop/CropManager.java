package ru.spbau.mit.swys.crop;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.Arrays;

public class CropManager {
    private static final int RESULT_BITMAP_SIZE = 400;

    private Matrix buildProjectionMatrix(Point[] points) {
        /*
            Construction of linear equations' matrix:
            Ax_i + By_i + C  + D0   + E0   + F0 - ax_ix'_i - by_ix'_i = x'_i
            A0   + B0   + C0 + Dx_i + Ey_i + F  - ax_iy'_i - by_iy'_i = y'_i

            i = 0..3

            with unknown variables A,B,C,D,E,F,a,b,c
        */

        float[][] equations = new float[8][8];
        float[] values = new float[8];

        Point[] targetPoints = new Point[4];
        targetPoints[0] = new Point(0, 0);
        targetPoints[1] = new Point(RESULT_BITMAP_SIZE, 0);
        targetPoints[2] = new Point(RESULT_BITMAP_SIZE, RESULT_BITMAP_SIZE);
        targetPoints[3] = new Point(0, RESULT_BITMAP_SIZE);


        for (int i = 0; i < 4; i++) {
            //src point
            Point p1 = points[i];
            //dst point
            Point p2 = targetPoints[i];

            int firstLineIndex = 2 * i;
            int secondLineIndex = 2 * i + 1;

            float[] firstLine = equations[firstLineIndex];
            float[] secondLine = equations[secondLineIndex];

            firstLine[0] = p1.x;             firstLine[1] = p1.y;  firstLine[2] = 1;
            firstLine[6] = - (p1.x * p2.x);  firstLine[7] = - (p1.y * p2.x);

            secondLine[3] = p1.x;            secondLine[4] = p1.y; secondLine[5] = 1;
            secondLine[6] = - (p1.x * p2.y); secondLine[7] = - (p1.y * p2.y);

            values[firstLineIndex] = p2.x;
            values[secondLineIndex] = p2.y;
        }

        float[] solution = GaussianElimination.lsolve(equations, values);

        /*
         * A B C
         * D E F
         * a b c
         * c = 1
        */
        float[] matrixValues = Arrays.copyOf(solution, 9);
        matrixValues[8] = 1;

        Matrix result = new Matrix();
        result.setValues(matrixValues);

        return result;
    }

    private Matrix buildInverseProjectionMatrix(Point[] points) {
        Matrix projectionMatrix = buildProjectionMatrix(points);
        Matrix inverseProjectionMatrix = new Matrix();

        if (!projectionMatrix.invert(inverseProjectionMatrix)) {
            throw new RuntimeException("Det of matrix shouldn't be near zero");
        }

        return inverseProjectionMatrix;
    }

    public Bitmap cropBitmap(Bitmap srcBitmap, Point[] points) {
        Matrix inverseProjectionMatrix = buildInverseProjectionMatrix(points);

        int[] srcBitmapColors = new int[srcBitmap.getWidth() * srcBitmap.getHeight()];
        srcBitmap.getPixels(
                srcBitmapColors,
                0, srcBitmap.getWidth(),
                0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()
        );

        int[] resultBitmapColors = new int[RESULT_BITMAP_SIZE * RESULT_BITMAP_SIZE];
        float[] dstBitmapPointDesc = new float[2];
        float[] srcBitmapPointDesc = new float[2];
        PointF srcBitmapPoint = new PointF();

        for (int row = 0; row < RESULT_BITMAP_SIZE; row++) {
            for (int column = 0; column < RESULT_BITMAP_SIZE; column++) {
                dstBitmapPointDesc[0] = column; dstBitmapPointDesc[1] = row;

                inverseProjectionMatrix.mapPoints(srcBitmapPointDesc, dstBitmapPointDesc);

                srcBitmapPoint.x = srcBitmapPointDesc[0]; srcBitmapPoint.y = srcBitmapPointDesc[1];

                resultBitmapColors[row * RESULT_BITMAP_SIZE + column] = getColorFromSource(
                        srcBitmapPoint,
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
}
