package ru.spbau.mit.swys.search;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.w3c.dom.Comment;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchResultsDatabase {
    private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String[] resultsColumns = {
            SearchResultsSQLiteOpenHelper.TABLE_RESULTS_COLUMN_ID,
            SearchResultsSQLiteOpenHelper.TABLE_RESULTS_COLUMN_TIMESTAMP,
            SearchResultsSQLiteOpenHelper.TABLE_RESULTS_COLUMN_IMAGE_PATH,
    };

    private static String[] itemsColumns = {
            SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_TITLE,
            SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_PICTURE_URL,
            SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_URL,
    };


    private SQLiteDatabase database;
    private SearchResultsSQLiteOpenHelper dbHelper;

    public SearchResultsDatabase(Context context) {
        dbHelper = new SearchResultsSQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void save(SearchResult result) throws SQLException {
        open();

        ContentValues resultValues = new ContentValues();
        resultValues.put(SearchResultsSQLiteOpenHelper.TABLE_RESULTS_COLUMN_IMAGE_PATH, result.getImage().getFile().getAbsolutePath());

        long resultId = database.insert(SearchResultsSQLiteOpenHelper.TABLE_RESULTS, null, resultValues);

        for (SearchResultItem item : result.getItems()) {
            ContentValues itemValues = new ContentValues();

            itemValues.put(SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_RESULT_ID, resultId);
            itemValues.put(SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_TITLE, item.getTitle());
            itemValues.put(SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_URL, item.getUrl());
            itemValues.put(SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_PICTURE_URL, item.getPictureUrl());

            database.insert(SearchResultsSQLiteOpenHelper.TABLE_ITEMS, null, itemValues);
        }

        close();
    }

    public List<SearchResult> getResults() throws SQLException {
        open();

        List<SearchResult> results = new ArrayList<SearchResult>();

        Cursor cursor = database.query(
                SearchResultsSQLiteOpenHelper.TABLE_RESULTS,
                resultsColumns,
                null, null, null, null,
                SearchResultsSQLiteOpenHelper.TABLE_RESULTS_COLUMN_TIMESTAMP + " DESC"
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SearchResult result = loadResult(cursor);
            results.add(result);
            cursor.moveToNext();
        }

        cursor.close();

        close();

        return results;
    }

    public void clear() throws SQLException {
        open();

        database.delete(SearchResultsSQLiteOpenHelper.TABLE_ITEMS, null, null);
        database.delete(SearchResultsSQLiteOpenHelper.TABLE_RESULTS, null, null);

        close();
    }

    private SearchResult loadResult(Cursor resultCursor) {
        long resultId = resultCursor.getLong(0);
        String timestamp = resultCursor.getString(1);
        String imagePath = resultCursor.getString(2);

        SearchResult result = new SearchResult(new Image(new File(imagePath)));
        try {
            result.setCreatedAt(timestampFormat.parse(timestamp));
        } catch (ParseException e) {
            //result without date
        }

        Cursor itemCursor = database.query(
                SearchResultsSQLiteOpenHelper.TABLE_ITEMS,
                itemsColumns,
                SearchResultsSQLiteOpenHelper.TABLE_ITEMS_COLUMN_RESULT_ID + "=" + resultId,
                null, null, null, null
        );

        itemCursor.moveToFirst();
        while (!itemCursor.isAfterLast()) {
            result.addItem(loadResultItem(itemCursor));
            itemCursor.moveToNext();
        }

        itemCursor.close();

        return result;
    }

    private SearchResultItem loadResultItem(Cursor itemCursor) {
        return new SearchResultItem(
            itemCursor.getString(0),
            itemCursor.getString(1),
            itemCursor.getString(2)
        );
    }
}
