package ru.spbau.mit.swys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;

abstract public class BaseActivity extends Activity {
    protected void showErrorToastAndFinish(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        finish();
    }

    protected void showErrorToastAndFinish(int msgResId) {
        showErrorToastAndFinish(getString(msgResId));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TempStorageUtils.prepareTempDir()) {
            showErrorToastAndFinish(R.string.prepare_temp_dir_error);
        }
    }

    protected Bitmap loadBitmapByUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            showErrorToastAndFinish(R.string.load_bitmap_error);
        }

        return null;
    }
}
