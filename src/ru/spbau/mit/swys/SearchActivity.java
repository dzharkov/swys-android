package ru.spbau.mit.swys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import ru.spbau.mit.swys.search.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SearchActivity extends Activity {

    private Bitmap currentImageBitmap;
    private SearchService searchService = new SwysSearchService(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Bitmap bm = getIntent().getExtras().getParcelable("image");
        currentImageBitmap = bm;

        ImageView iv = (ImageView) findViewById(R.id.target_image);
        iv.setImageBitmap(bm);
    }

    private void writeBitmapToFile(File file) throws FileNotFoundException {
        currentImageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, new FileOutputStream(file));
    }

    private void processSearchCompletion() {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.INVISIBLE);
    }

    private void startResultActivity(SearchResult result) {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("result", result);

        startActivity(resultIntent);
    }

    public void searchButtonProcess(View view) {
        ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);

        new SearchServiceGateway(this).execute();
    }

    private class SearchServiceGateway extends AsyncTask<Void, Void, SearchResult> {
        private File tmpFile;
        private SearchQueryException exception;

        private Context context;

        private SearchServiceGateway(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            exception = null;
        }

        @Override
        protected SearchResult doInBackground(Void... voids) {
            tmpFile = Utils.getTempImageFile();

            try {
                writeBitmapToFile(tmpFile);
            } catch (FileNotFoundException e) {
                exception = new SearchQueryException(getResources().getString(R.string.cant_write_bitmap_error_msg));

                return null;
            }

            try {
                return searchService.search(new Image(tmpFile));
            } catch (SearchQueryException e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(SearchResult result) {
            processSearchCompletion();

            tmpFile.delete();

            if (exception != null) {
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                startResultActivity(result);
            }
        }
    }
}

