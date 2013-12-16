package ru.spbau.mit.swys;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import ru.spbau.mit.swys.search.SearchResult;
import ru.spbau.mit.swys.search.SearchResultAdapter;
import ru.spbau.mit.swys.search.SearchResultsDatabase;

import java.sql.SQLException;
import java.util.List;


public class SearchHistoryActivity extends BaseActivity {
    private SearchResultsDatabase database = new SearchResultsDatabase(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_history);
        updateHistory();
    }

    private void updateHistory() {
        List<SearchResult> history = null;
        try {
            history = database.getResults();
        } catch (SQLException e) {
            showErrorToastAndFinish(R.string.db_load_unsuccessful);
            return;
        }

        SearchResultAdapter adapter = new SearchResultAdapter(this, R.layout.search_history_item, history);

        ListView listView = (ListView) findViewById(R.id.results_list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history, menu);
        return true;
    }

    private void clearHistory() {
        try {
           database.clear();
           updateHistory();
        } catch (SQLException e) {
            showErrorToastAndFinish(R.string.db_load_unsuccessful);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_history:
                clearHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}