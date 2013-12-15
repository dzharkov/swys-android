package ru.spbau.mit.swys;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

abstract public class BaseActivity extends Activity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void showErrorToastAndFinish(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        finish();
    }

    protected void showErrorToastAndFinish(int msgResId) {
        showErrorToastAndFinish(getString(msgResId));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (!TempStorageUtils.getInstance().prepareTempDir()) {
            showErrorToastAndFinish(R.string.prepare_temp_dir_error);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    protected File writeBitmapToTempFile(Bitmap bitmap, boolean isWordAvailable) {
        //TODO: delete it somehow later
        File tmpFile = TempStorageUtils.getInstance().getTempImageFile(isWordAvailable);

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, new FileOutputStream(tmpFile));
        } catch(IOException e) {
            showErrorToastAndFinish(R.string.cant_write_bitmap_error_msg);
        }

        return tmpFile;
    }
}
