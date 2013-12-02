package ru.spbau.mit.swys.crop;

import android.graphics.Point;
import android.graphics.PointF;

public class OrientedSegment {
    private Point begin;

    private Point vector;

    public OrientedSegment(Point begin, Point end) {
        this.begin = begin;
        this.vector = new Point(end.x - begin.x, end.y - begin.y);
    }

    public void pointAt(float proportion, PointF dst) {
        dst.x = begin.x + proportion * vector.x;
        dst.y = begin.y + proportion * vector.y;
    }
}
