package ru.spbau.mit.swys.search;

import android.graphics.Bitmap;

public class Image {
    private Bitmap bitmap;

    public Image(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
