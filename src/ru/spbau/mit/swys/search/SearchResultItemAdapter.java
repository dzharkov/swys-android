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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
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

        final ImageView imageView = (ImageView) row.findViewById(R.id.image);
        final View imageLoading = row.findViewById(R.id.image_loading);

        ImageLoader.getInstance().displayImage(data[position].getPictureUrl(), imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                imageView.setVisibility(View.GONE);
                imageLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                imageView.setVisibility(View.VISIBLE);
                imageLoading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        row.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) context);

        return row;
    }
}

