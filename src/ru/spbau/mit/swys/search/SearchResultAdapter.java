package ru.spbau.mit.swys.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.spbau.mit.swys.R;
import ru.spbau.mit.swys.RequestCodes;
import ru.spbau.mit.swys.ResultActivity;

import java.text.DateFormat;
import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {
    private int resourceId;
    private List<SearchResult> data;

    public SearchResultAdapter(Context context, int resource, List<SearchResult> objects) {
        super(context, resource, objects);
        resourceId = resource;
        data = objects;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (row == null) {
            row = ((Activity) getContext()).getLayoutInflater().inflate(resourceId, parent, false);
        }

        final SearchResult result = data.get(position);

        TextView titleView = (TextView) row.findViewById(R.id.item_title);

        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(getContext());
        titleView.setText(dateFormat.format(result.getCreatedAt()));

        ImageView imageView = (ImageView) row.findViewById(R.id.image);
        imageView.setImageURI(Uri.fromFile(result.getImage().getFile()));
        imageView.refreshDrawableState();

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(getContext(), ResultActivity.class);
                resultIntent.putExtra(RequestCodes.SEARCH_RESULT_EXTRA_FIELD, result);
                getContext().startActivity(resultIntent);
            }
        });

        return row;
    }
}
