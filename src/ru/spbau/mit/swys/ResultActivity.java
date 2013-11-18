package ru.spbau.mit.swys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import ru.spbau.mit.swys.search.SearchResult;
import ru.spbau.mit.swys.search.SearchResultItemAdapter;

public class ResultActivity extends Activity {
    private SearchResult result;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.result);

        result = getIntent().getParcelableExtra("result");

        ListAdapter adapter = new SearchResultItemAdapter(this, R.layout.result_item, result.getItems());

        ListView listView = (ListView) findViewById(R.id.result_list);
        listView.setAdapter(adapter);
    }
}
