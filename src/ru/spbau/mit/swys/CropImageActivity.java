package ru.spbau.mit.swys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import ru.spbau.mit.swys.crop.CropImageView;
import ru.spbau.mit.swys.crop.CropManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropImageActivity extends BaseActivity {
    private Bitmap currentBitmap;

    private CropManager cropManager = new CropManager();

    private CroppingAsyncTask currentTask;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);

        Uri currentImageUri = getIntent().getData();
        currentBitmap = loadBitmapByUri(currentImageUri);

        CropImageView cropImageView = (CropImageView) findViewById(R.id.image);
        cropImageView.setImageBitmap(currentBitmap);

        cropImageView.refreshDrawableState();
    }

    private File writeBitmapToTempFile(Bitmap bitmap) {
        //TODO: delete it somehow later
        File tmpFile = TempStorageUtils.getTempImageFile();

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, new FileOutputStream(tmpFile));
        } catch(IOException e) {
            showErrorToastAndFinish(R.string.cant_write_bitmap_error_msg);
        }

        return tmpFile;
    }

    public void startCropButtonProcess(View view) {
        progressDialog = ProgressDialog.show(this,
            getString(R.string.progress_dialog_title),
            getString(R.string.progress_dialog_msg),
            true, true,
            new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    currentTask.cancel(true);
                }
            }
        );

        CropImageView cropImageView = (CropImageView) findViewById(R.id.image);

        currentTask = new CroppingAsyncTask();
        currentTask.execute(cropImageView.getCropPoints());
    }

    private void processCroppingCompletion(File imgFile) {
        if (progressDialog != null) {
            progressDialog.hide();
        }

        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putExtra(RequestCodes.PICTURE_CROP_EXTRA_FIELD, Uri.fromFile(imgFile));

        startActivity(searchIntent);
    }

    private class CroppingAsyncTask extends AsyncTask<Point[],Void,File> {
        @Override
        protected File doInBackground(Point[]... points) {
            Bitmap croppedBitmap = cropManager.cropBitmap(currentBitmap, points[0]);

            return writeBitmapToTempFile(croppedBitmap);
        }

        @Override
        protected void onPostExecute(File file) {
            processCroppingCompletion(file);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
