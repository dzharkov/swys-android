package ru.spbau.mit.swys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    private static final int TAKE_PICTURE_REQUEST = 1000;

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

        imgTempUri = Uri.fromFile(Utils.getTempImageFile());
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgTempUri);
        startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK)   {

            Intent startCropActivity = new Intent(this, CropImageActivity.class);
            startCropActivity.setData(imgTempUri);

            startActivity(startCropActivity);
        }
    }
}

