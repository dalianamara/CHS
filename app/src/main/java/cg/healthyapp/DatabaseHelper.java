package cg.healthyapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DatabaseHelper extends SQLiteOpenHelper {

    String DB_PATH = null;
    private static String DB_NAME = "data";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "oxygen";

    private static final String TABLE_NAME_HEART = "heart";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our date column
    private static final String DATE_COL = "date";

    // below variable id for our value column.
    private static final String VALUE_COL = "value";

    // below variable id for our value column.
    private static final String HOUR_COL = "hour";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
//        Log.e("Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                        + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DATE_COL + " TEXT,"+ HOUR_COL + " TEXT,"
                        + VALUE_COL + " TEXT)";

                String query2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_HEART + " ("
                        + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DATE_COL + " TEXT,"+ HOUR_COL + " TEXT,"
                        + VALUE_COL + " TEXT)";

                myDataBase.execSQL(query2);
                myDataBase.execSQL(query);
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + "oxygen" + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"+ HOUR_COL + " TEXT,"
                + VALUE_COL + " TEXT)";

        String query2 = "CREATE TABLE IF NOT EXISTS " + "heart" + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"+ HOUR_COL + " TEXT,"
                + VALUE_COL + " TEXT)";
        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query(table, null, null, null, null, null, null);
    }


}
