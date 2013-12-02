package ru.spbau.mit.swys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
        CropImageView cropImageView = (CropImageView) findViewById(R.id.image);

        Bitmap croppedBitmap = cropManager.cropBitmap(currentBitmap, cropImageView.getCropPoints());

        File file = writeBitmapToTempFile(croppedBitmap);

        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putExtra(RequestCodes.PICTURE_CROP_EXTRA_FIELD, Uri.fromFile(file));

        startActivity(searchIntent);
    }
}
