package com.students.routingmstu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mosin on 17.04.2015.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MosinFeedReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_POINTS =
            "CREATE TABLE " + FeedReaderContract.PointEntry.TABLE_NAME + " (" +
                    FeedReaderContract.PointEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.PointEntry.COLUMN_NAME_SHORTNAME + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.PointEntry.COLUMN_NAME_FULLNAME + TEXT_TYPE +
    // Any other options for the CREATE command
            " ); " +
            "CREATE TABLE " + FeedReaderContract.LengthEntry.TABLE_NAME + " (" +
                    FeedReaderContract.LengthEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.LengthEntry.COLUMN_NAME_STARTPOINTID + INTEGER_TYPE + COMMA_SEP +
                    FeedReaderContract.LengthEntry.COLUMN_NAME_ENDPOINTID + INTEGER_TYPE + COMMA_SEP +
                    FeedReaderContract.LengthEntry.COLUMN_NAME_LENGTH + INTEGER_TYPE +
                    // Any other options for the CREATE command
                    " ) " ;

    private static final String SQL_FILL_ENTRIES =
    "INSERT INTO " + FeedReaderContract.PointEntry.TABLE_NAME +
            "(" +
            FeedReaderContract.PointEntry._ID + ", " +
            FeedReaderContract.PointEntry.COLUMN_NAME_SHORTNAME + ", " +
            FeedReaderContract.PointEntry.COLUMN_NAME_FULLNAME +
            ") VALUES " +
            "(1, \"da\", \"abc\"), " +
            "(2, \"da\", \"abc\") ";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.PointEntry.TABLE_NAME
            + "; DROP TABLE IF EXISTS " + FeedReaderContract.LengthEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_POINTS);
        db.execSQL(SQL_FILL_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}