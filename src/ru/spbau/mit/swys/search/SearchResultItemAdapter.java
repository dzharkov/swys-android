package ru.spbau.mit.swys.search;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ru.spbau.mit.swys.R;

public class SearchResultItemAdapter extends ArrayAdapter< SearchResultItem > {
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
            row = ((Activity)context).getLayoutInflater().inflate(layoutResourceId, parent, false);
        }

        TextView titleView = (TextView)row.findViewById(R.id.item_title);
        titleView.setText(data[position].getTitle());

        TextView linkView  = (TextView)row.findViewById(R.id.item_link);
        linkView.setMovementMethod(LinkMovementMethod.getInstance());
        linkView.setText(Html.fromHtml("<a href=\"" + data[position].getUrl() + "\">Открыть</a>"));

        return row;
    }
}
