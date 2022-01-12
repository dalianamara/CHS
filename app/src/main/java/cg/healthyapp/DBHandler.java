package cg.healthyapp;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "data";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "oxygen";

    private static final String TABLE_NAME_HEART = "heart";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our date column
    private static final String DATE_COL = "date";

    // below variable is for our value column
    private static final String VALUE_COL = "value";

    private static final String HOUR_COL = "hour";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is used for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // a sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"+ HOUR_COL + " TEXT,"
                + VALUE_COL + " TEXT)";

        String queryHeart = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_HEART + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"+ HOUR_COL + " TEXT,"
                + VALUE_COL + " TEXT)";
//
//         at last we are calling an exec sql
//         method to execute above sql query
        db.execSQL(query);
        db.execSQL(queryHeart);
    }

    // this method is used to add new heart rate/oxygen to our sqlite database.
    public void addNewPulse(long date, String hour, String value, String TABLE) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(DATE_COL, date);
        values.put(HOUR_COL, hour);
        values.put(VALUE_COL, value);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE, null, values);
        // at last we are closing our
        // database after adding database.
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HEART);
        onCreate(db);
    }
}