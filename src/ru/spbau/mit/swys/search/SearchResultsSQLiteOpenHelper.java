package ru.spbau.mit.swys.search;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchResultsSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_RESULTS = "results";
    public static final String TABLE_RESULTS_COLUMN_ID = "_id";
    public static final String TABLE_RESULTS_COLUMN_TIMESTAMP = "timestamp";
    public static final String TABLE_RESULTS_COLUMN_IMAGE_PATH = "image_path";

    public static final String TABLE_ITEMS = "items";
    public static final String TABLE_ITEMS_COLUMN_ID = "_id";
    public static final String TABLE_ITEMS_COLUMN_RESULT_ID = "result_id";
    public static final String TABLE_ITEMS_COLUMN_TITLE = "title";
    public static final String TABLE_ITEMS_COLUMN_URL = "url";
    public static final String TABLE_ITEMS_COLUMN_PICTURE_URL = "picture_url";


    private static final String DATABASE_NAME = "search_results.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_RESULTS = "CREATE TABLE "
            + TABLE_RESULTS + " ("
            + TABLE_RESULTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TABLE_RESULTS_COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + TABLE_RESULTS_COLUMN_IMAGE_PATH + " VARCHAR NOT NULL );";

    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE "
            + TABLE_ITEMS + " ("
            + TABLE_ITEMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TABLE_ITEMS_COLUMN_RESULT_ID + " INTEGER NOT NULL,"
            + TABLE_ITEMS_COLUMN_TITLE + " VARCHAR NOT NULL,"
            + TABLE_ITEMS_COLUMN_URL + " VARCHAR NOT NULL,"
            + TABLE_ITEMS_COLUMN_PICTURE_URL + " VARCHAR NOT NULL );";

    public SearchResultsSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_RESULTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        onCreate(sqLiteDatabase);
    }
}
