package ru.spbau.mit.swys.search;

import android.graphics.Bitmap;

import java.io.File;

public class Image {
    private File file;

    public Image(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
