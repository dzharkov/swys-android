package ru.spbau.mit.swys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import ru.spbau.mit.swys.crop.CropImageView;
import ru.spbau.mit.swys.crop.CropManager;
import ru.spbau.mit.swys.search.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropImageActivity extends BaseActivity {
    private Bitmap currentBitmap;

    private CropManager cropManager = new CropManager();
    private SearchService searchService = new StubSearchService();

    private AsyncTask currentTask;
    private ProgressDialog progressDialog;

    private boolean searchAfterCrop = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);

        Uri currentImageUri = getIntent().getData();
        currentBitmap = loadBitmapByUri(currentImageUri);

        CropImageView cropImageView = (CropImageView) findViewById(R.id.image);
        cropImageView.setImageBitmap(currentBitmap);

        cropImageView.refreshDrawableState();
    }

    private void cropImage() {
        progressDialog = ProgressDialog.show(this,
                getString(R.string.progress_dialog_title),
                getString(R.string.progress_dialog_msg_cropping),
                true, true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        currentTask.cancel(true);
                    }
                }
        );

        CropImageView cropImageView = (CropImageView) findViewById(R.id.image);

        currentTask = new CroppingAsyncTask().
                execute(cropImageView.getCropPoints());
    }

    public void startCropButtonProcess(View view) {
        searchAfterCrop = false;
        cropImage();
    }

    public void startSearchButtonProcess(View view) {
        searchAfterCrop = true;
        cropImage();
    }

    private void processCroppingCompletion(File imgFile) {
        if (searchAfterCrop) {
            progressDialog.setMessage(getString(R.string.progress_dialog_msg_searching));

            currentTask = new SearchServiceGateway(this).execute(imgFile);
        } else {
            if (progressDialog != null) {
                progressDialog.hide();
            }

            Intent searchIntent = new Intent(this, SearchActivity.class);
            searchIntent.putExtra(RequestCodes.PICTURE_CROP_EXTRA_FIELD, Uri.fromFile(imgFile));

            startActivity(searchIntent);
        }
    }

    private void processSearchCompletion() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    private void processSearchResult(SearchResult result) {
        if (!result.isSuccessful()) {
            Toast.makeText(this, getString(R.string.search_query_unsuccessful_result), Toast.LENGTH_LONG).show();
            return;
        }

        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra(RequestCodes.SEARCH_RESULT_EXTRA_FIELD, result);

        startActivity(resultIntent);
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

    private class SearchServiceGateway extends AsyncTask<File, Void, SearchResult> {
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
        protected SearchResult doInBackground(File... files) {
            try {
                return searchService.search(new Image(files[0]));
            } catch (SearchQueryException e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(SearchResult result) {
            processSearchCompletion();

            if (exception != null) {
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                processSearchResult(result);
            }
        }
    }
}
