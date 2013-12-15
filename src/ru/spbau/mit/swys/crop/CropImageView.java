package ru.spbau.mit.swys.crop;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class CropImageView extends ImageView {

    public CropImageView(Context context) {
        super(context);
        init();
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        currentBitmap = bm;
    }

    public Point[] getCropPoints() {
        return cropPolygon.getCropPoints();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);

        overlayPaint = new Paint();
        overlayPaint.setColor(Color.GRAY);
        overlayPaint.setAlpha(150);
        overlayPaint.setStyle(Paint.Style.FILL);

        circlesPaint = new Paint();
        circlesPaint.setColor(Color.WHITE);
        circlesPaint.setStyle(Paint.Style.FILL);
        circlesPaint.setAntiAlias(true);

        polygonPaint = new Paint();
        polygonPaint.setColor(Color.WHITE);
        polygonPaint.setStyle(Paint.Style.STROKE);
        polygonPaint.setAntiAlias(true);

        upperSidePaint = new Paint(polygonPaint);
        upperSidePaint.setColor(Color.RED);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float bmWidth = currentBitmap.getWidth();
        float bmHeight = currentBitmap.getHeight();

        float imgScaleX = viewWidth / bmWidth;
        float imgScaleY = viewHeight / bmHeight;

        float imgScale = Math.min(imgScaleX, imgScaleY);

        bmLayoutMatrix.reset();
        bmLayoutMatrix.setScale(imgScale, imgScale);
        bmLayoutMatrix.postTranslate(
                (viewWidth - bmWidth * imgScale) / 2F,
                (viewHeight - bmHeight * imgScale) / 2F
        );

        setImageMatrix(bmLayoutMatrix);

        cropPolygon = new CropPolygon(bmLayoutMatrix, new Rect(0, 0, (int) bmWidth, (int) bmHeight));
        cropPolygon.initDefaultPosition();
    }

    private Path buildCropPath() {
        Path path = new Path();

        Point[] points = cropPolygon.getLayoutPoints();

        path.moveTo(points[0].x, points[0].y);

        for (int i = 0; i < 4; i++) {
            int pointIndex = (i + 1) % 4;
            path.lineTo(points[pointIndex].x, points[pointIndex].y);
        }

        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path cropPath = buildCropPath();

        //drawing overlay
        canvas.clipPath(cropPath, Region.Op.DIFFERENCE);
        canvas.drawRect(cropPolygon.getBmLayoutRect(), overlayPaint);
        canvas.restore();

        //drawing white border
        canvas.drawPath(cropPath, polygonPaint);

        //drawing upper side
        Point[] upperSide = cropPolygon.getUppedSide();
        canvas.drawLine(
                upperSide[0].x, upperSide[0].y,
                upperSide[1].x, upperSide[1].y,
                upperSidePaint
        );

        //drawing control circles
        for (Point p : cropPolygon.getLayoutPoints()) {
            canvas.drawCircle(p.x, p.y, CropPolygon.POINT_RADIUS, circlesPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionCode = event.getActionMasked();

        int actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);

        if (actionCode == MotionEvent.ACTION_DOWN || actionCode == MotionEvent.ACTION_POINTER_DOWN) {

            Point downPoint = new Point((int) event.getX(actionIndex), (int) event.getY(actionIndex));
            int hitPointId = cropPolygon.getHitPointId(downPoint);

            if (hitPointId == CropPolygon.NULL_POINT_ID) {
                pointersMap.remove(pointerId);
                return false;
            }

            if (actionCode == MotionEvent.ACTION_DOWN) {
                isStateMoving = true;
            }

            pointersMap.put(pointerId, hitPointId);

            return true;
        } else if (isStateMoving) {
            boolean wereChanges = false;
            if (actionCode == MotionEvent.ACTION_MOVE) {
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int curPointerId = event.getPointerId(i);
                    Integer pointId = pointersMap.get(curPointerId);

                    if (pointId == null) {
                        continue;
                    }

                    if (cropPolygon.movePoint(pointId, (int) event.getX(i), (int) event.getY(i))) {
                        wereChanges = true;
                    }
                }

                if (wereChanges) {
                    invalidate();
                    return true;
                }
            } else if (actionCode == MotionEvent.ACTION_POINTER_UP || actionCode == MotionEvent.ACTION_UP) {

                pointersMap.remove(pointerId);

                if (actionCode == MotionEvent.ACTION_UP) {
                    isStateMoving = false;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    private Bitmap currentBitmap;

    private CropPolygon cropPolygon;

    private Matrix bmLayoutMatrix = new Matrix();

    private Paint overlayPaint;
    private Paint circlesPaint;
    private Paint polygonPaint;
    private Paint upperSidePaint;

    private boolean isStateMoving = false;

    private Map<Integer, Integer> pointersMap = new HashMap<Integer, Integer>();
}
