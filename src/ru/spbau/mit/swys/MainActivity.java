package ru.spbau.mit.swys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class MainActivity extends BaseActivity {
    private Uri imgTempUri;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startButtonProcess(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imgTempUri = Uri.fromFile(TempStorageUtils.getTempImageFile());
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgTempUri);
        startActivityForResult(takePictureIntent, RequestCodes.TAKE_PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {

            Intent startCropActivity = new Intent(this, CropImageActivity.class);
            startCropActivity.setData(imgTempUri);

            startActivity(startCropActivity);
        }
    }
}

