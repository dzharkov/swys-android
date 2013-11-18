package ru.spbau.mit.swys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CropImageActivity extends Activity {
    private Uri currentImageUri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);

        currentImageUri = getIntent().getData();

        ImageView iv = (ImageView) findViewById(R.id.image);
        iv.setImageURI(currentImageUri);

        iv.refreshDrawableState();
    }

    public void startCropButtonProcess(View view) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(currentImageUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("return-data", true);

        startActivityForResult(cropIntent, RequestCodes.PICTURE_CROP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.PICTURE_CROP_REQUEST && resultCode == RESULT_OK) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            searchIntent.putExtra("image", data.getExtras().getParcelable("data"));

            startActivity(searchIntent);
        }
    }
}
