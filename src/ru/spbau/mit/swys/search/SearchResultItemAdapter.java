package ru.spbau.mit.swys.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import ru.spbau.mit.swys.R;

public class SearchResultItemAdapter extends ArrayAdapter<SearchResultItem> {
    private SearchResultItem[] data;
    private int layoutResourceId;
    private Context context;

    public SearchResultItemAdapter(Context context, int layoutResourceId, SearchResultItem[] data) {
        super(context, layoutResourceId, data);
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (row == null) {
            row = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, parent, false);
        }

        TextView titleView = (TextView) row.findViewById(R.id.item_title);
        titleView.setText(data[position].getTitle());

        ImageView imageView = (ImageView) row.findViewById(R.id.image);

        new ImageDownloadTask(
                imageView,
                row.findViewById(R.id.image_loading)
        ).execute(data[position].getPictureUrl());

        row.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) context);

        return row;
    }

    private class ImageDownloadTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView imageView;
        private View      preloaderView;

        private ImageDownloadTask(ImageView imageView, View preloaderView) {
            this.imageView = imageView;
            this.preloaderView = preloaderView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            return ImageLoader.getInstance().loadImageSync(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);

            preloaderView.setVisibility(View.GONE);
        }
    }
}

