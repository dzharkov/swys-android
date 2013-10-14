package ru.spbau.mit.swys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class SearchActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Bitmap bm = getIntent().getExtras().getParcelable("image");

        ImageView iv = (ImageView)findViewById(R.id.target_image);
        iv.setImageBitmap(bm);
    }
}