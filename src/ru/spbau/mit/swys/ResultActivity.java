package ru.spbau.mit.swys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import ru.spbau.mit.swys.search.SearchResult;
import ru.spbau.mit.swys.search.SearchResultItem;
import ru.spbau.mit.swys.search.SearchResultItemAdapter;

import java.io.File;

public class ResultActivity extends BaseActivity {
    private SearchResult result;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.result);

        result = getIntent().getParcelableExtra(RequestCodes.SEARCH_RESULT_EXTRA_FIELD);

        ListAdapter adapter = new SearchResultItemAdapter(this, R.layout.result_item, result.getItems());

        ListView listView = (ListView) findViewById(R.id.result_list);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.result_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.result_item_context_menu, menu);

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(result.getItem(info.position).getTitle());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemId = info.position;
        SearchResultItem searchResultItem = result.getItem(itemId);

        switch (item.getItemId()) {
            case R.id.open_description:
                openDescriptionInBrowser(searchResultItem);
                return true;
            case R.id.share:
                startSharingProcess(searchResultItem);
                return true;
            case R.id.save_to_gallery:
                saveToGallery(searchResultItem);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void openDescriptionInBrowser(SearchResultItem item) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(item.getUrl())
        );

        browserIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        startActivity(browserIntent);
    }

    private File writeItemImageToTempFile(SearchResultItem item) {
        return writeBitmapToTempFile(
                ImageLoader.getInstance().loadImageSync(item.getPictureUrl())
        );
    }

    private void startSharingProcess(SearchResultItem item) {
        File imageFile = writeItemImageToTempFile(item);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " \"" + item.getTitle() + "\"");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        sendIntent.setType("image/*");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_title)));
    }

    private void saveToGallery(SearchResultItem item) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri contentUri = Uri.fromFile(writeItemImageToTempFile(item));

        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}
