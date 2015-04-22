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
    private static final String BIT_TYPE = " BIT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_POINTS =
            "CREATE TABLE " + FeedReaderContract.PointEntry.TABLE_NAME + " (" +
                    FeedReaderContract.PointEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.PointEntry.COLUMN_NAME_SHORTNAME + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.PointEntry.COLUMN_NAME_FULLNAME + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.PointEntry.COLUMN_NAME_ISIMPORTANT + BIT_TYPE +
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
            FeedReaderContract.PointEntry.COLUMN_NAME_FULLNAME + ", " +
            FeedReaderContract.PointEntry.COLUMN_NAME_ISIMPORTANT +
            ") VALUES " +
            "(1, \"Кафедра САПР\", \"Кафедра САПР\", 1), " +
            "(2, \"6-302\", \"6-302\", 0), " +
            "(3, \"6-301\", \"6-301\", 0), " +
            "(4, \"Выход на лестницу\", \"Выход на лестницу\", 0), " +
            "(5, \"2 этажа вниз\", \"2 этажа вниз\", 0), " +
            "(6, \"Переход в корпус 3\", \"Переход в корпус 3\", 0), " +
            "(7, \"1 этаж наверх\", \"1 этаж наверх\", 0), " +
            "(8, \"Переход в корпус 1\", \"Переход в корпус 1\", 0), " +
            "(9, \"1 этаж вниз\", \"1 этаж вниз\", 0), " +
            "(10, \"Библиотека\", \"Библиотека\", 1), " +
            "(11, \"Напротив\", \"Напротив\", 0), " +
            "(12, \"Читальный зал\", \"Читальный зал\", 1), " +
            "(13, \"1 этаж наверх\", \"1 этаж наверх\", 0), " +
            "(14, \"Выход на летницу\", \"Выход на летницу\", 0), " +
            "(15, \"2 этажа вниз\", \"2 этажа вниз\", 0), " +
            "(16, \"Выход на летницу\", \"Выход на летницу\", 0), " +
            "(17, \"Прямо по коридору\", \"Прямо по коридору\", 0), " +
            "(18, \"Гардероб\", \"Гардероб\", 1), " +
            "(19, \"Выход\", \"Выход\", 1), " +
            "(20, \"Переход в корпус 1\", \"Переход в корпус 1\", 0), " +
            "(21, \"Прямо по коридору\", \"Прямо по коридору\", 0), " +
            "(22, \"Столовая\", \"Столовая\", 1), " +
            "(23, \"1 этаж вниз\", \"1 этаж вниз\", 0), " +
            "(24, \"6-201\", \"6-201\", 0), " +
            "(25, \"6-202\", \"6-202\", 0), " +
            "(26, \"Прямо по коридору\", \"Прямо по коридору\", 0), " +
            "(27, \"Буфет\", \"Буфет\", 1); " +
            "INSERT INTO " + FeedReaderContract.LengthEntry.TABLE_NAME +
            "(" +
            FeedReaderContract.LengthEntry._ID + ", " +
            FeedReaderContract.LengthEntry.COLUMN_NAME_STARTPOINTID + ", " +
            FeedReaderContract.LengthEntry.COLUMN_NAME_ENDPOINTID + ", " +
            FeedReaderContract.LengthEntry.COLUMN_NAME_LENGTH +
            ") VALUES " +
            "(1, 1, 2, 1), " +
            "(1, 2, 3, 1), " +
            "(1, 3, 4, 1), " +
            "(1, 4, 5, 6), " +
            "(1, 5, 6, 2), " +
            "(1, 4, 7, 3), " +
            "(1, 7, 8, 10), " +
            "(1, 8, 9, 8), " +
            "(1, 9, 10, 5), " +
            "(1, 10, 11, 1), " +
            "(1, 11, 12, 1), " +
            "(1, 12, 13, 5), " +
            "(1, 10, 13, 3), " +
            "(1, 9, 12, 5), " +
            "(1, 13, 14, 3), " +
            "(1, 14, 15, 6), " +
            "(1, 15, 16, 3), " +
            "(1, 16, 17, 3), " +
            "(1, 17, 18, 4), " +
            "(1, 18, 19, 1), " +
            "(1, 14, 20, 3), " +
            "(1, 20, 21, 8), " +
            "(1, 21, 22, 4), " +
            "(1, 20, 23, 5), " +
            "(1, 23, 24, 2), " +
            "(1, 24, 25, 1), " +
            "(1, 25, 26, 1), " +
            "(1, 26, 27, 1), " +
            "(1, 4, 23, 3); ";


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