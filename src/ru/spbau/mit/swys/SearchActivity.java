package ru.spbau.mit.swys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import ru.spbau.mit.swys.search.Image;
import ru.spbau.mit.swys.search.SearchResult;
import ru.spbau.mit.swys.search.SearchService;
import ru.spbau.mit.swys.search.StubSearchService;

public class SearchActivity extends Activity {

    private Bitmap currentImageBitmap;
    private SearchService searchService = new StubSearchService();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Bitmap bm = getIntent().getExtras().getParcelable("image");
        currentImageBitmap = bm;

        ImageView iv = (ImageView)findViewById(R.id.target_image);
        iv.setImageBitmap(bm);
    }

    private void searchCurrentImage()
    {
        SearchResult result = searchService.search(new Image(currentImageBitmap));

        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("result", result);

        startActivity(resultIntent);
    }

    public void searchButtonProcess(View view) {
        ProgressBar pb = (ProgressBar)findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);

        searchCurrentImage();
    }
}