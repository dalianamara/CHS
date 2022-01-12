package cg.healthyapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "details";
    private static final String TABLE_NAME = "details";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "gender";
    private static DatabaseHandler dbHandler;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHandler getInstance(final Context c) {
        if (dbHandler == null) {
            dbHandler = new DatabaseHandler(c.getApplicationContext());
        }
        return dbHandler;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_ITEM_TABLE = "CREATE TABLE    " + TABLE_NAME + "( "
                + COLUMN_ID + "   INTEGER PRIMARY KEY, " + COLUMN_NAME + "   TEXT)";
        db.execSQL(CREATE_ITEM_TABLE);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}