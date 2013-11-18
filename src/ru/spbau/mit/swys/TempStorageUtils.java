package ru.spbau.mit.swys;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TempStorageUtils {
    private static final SimpleDateFormat tempFileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private static File mediaStorageDir = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "SWYS"
    );

    public static boolean prepareTempDir() {
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }

        return true;
    }

    public static File getTempImageFile() {
        String timeStamp = tempFileDateFormat.format(new Date());

        return new File(
                mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg"
        );
    }
}
