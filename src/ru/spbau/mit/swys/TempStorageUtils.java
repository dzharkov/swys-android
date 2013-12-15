package ru.spbau.mit.swys;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TempStorageUtils {
    private static final SimpleDateFormat tempFileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private static TempStorageUtils instance = new TempStorageUtils();

    public static TempStorageUtils getInstance() {
        return instance;
    }

    private File mediaStorageDir = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "SWYS"
    );

    private File privateDir;

    private TempStorageUtils() {
    }

    public void init(Context context) {
        privateDir = context.getCacheDir();
    }

    public boolean prepareTempDir() {
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }

        return true;
    }

    private File getTempImageFile(File dir) {
        String timeStamp = tempFileDateFormat.format(new Date());

        return new File(
                dir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg"
        );
    }

    public File getTempWordAvailableImageFile() {
        return getTempImageFile(mediaStorageDir);
    }

    public File getTempPrivateImageFile() {
        return getTempImageFile(privateDir);
    }

    public File getTempImageFile(boolean isWordAvailable) {
        if (isWordAvailable) {
            return getTempWordAvailableImageFile();
        }

        return getTempPrivateImageFile();
    }
}
