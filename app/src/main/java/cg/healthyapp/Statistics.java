package cg.healthyapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cg.healthyapp.Database.Database;

public class Statistics extends AppCompatActivity {

    private DBHandler dbHandler;
    Cursor cOxygen = null, cHeart = null;
    Date date = new Date();
    GraphView graphViewOxygen, graphViewHeart;
    List<String> dateListOxygen = new ArrayList<>();
    List<String> valueListOxygen = new ArrayList<>();
    List<String> hourListOxygen = new ArrayList<>();
    List<String> dateListHeart = new ArrayList<>();
    List<String> valueListHeart = new ArrayList<>();
    List<String> hourListHeart = new ArrayList<>();
    int valuesCounterOxygen = 0, valuesCounterHeart = 0;
    String[] dateValueOxygen , valueOxygen, hourValueOxygen = new String[0];
    String[] dateValueHeart , valueHeart, hourValueHeart = new String[0];
    SimpleDateFormat df= new SimpleDateFormat("E", Locale.getDefault());
    SQLiteDatabase sqLiteDatabaseObjOxygen;
    LineGraphSeries<DataPoint> seriesOxygen =  new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> seriesHeart =  new LineGraphSeries<>(new DataPoint[0]);
    Button next_Activity_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graphs);
        dbHandler = new DBHandler(Statistics.this);
        DatabaseHelper myDbHelper = new DatabaseHelper(Statistics.this);
        sqLiteDatabaseObjOxygen = dbHandler.getWritableDatabase();
        graphViewOxygen = (GraphView) findViewById(R.id.graphid);
        graphViewHeart = (GraphView) findViewById(R.id.graphheartid);
        next_Activity_button = (Button) findViewById(R.id.backBtn);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM \n hh:mm");
        next_Activity_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Statistics.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        try {
            myDbHelper.createDataBase();
            sqLiteDatabaseObjOxygen = myDbHelper.getWritableDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
            sqLiteDatabaseObjOxygen = myDbHelper.getWritableDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        graphViewOxygen.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX){
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    // return y label as number
                    return super.formatLabel(value, isValueX); // let the y-value be normal-formatted
                }
            }
        });

        graphViewHeart.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX){
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    // return y label as number
                    return super.formatLabel(value, isValueX); // let the y-value be normal-formatted
                }
            }
        });

        cOxygen = myDbHelper.query("oxygen", null, null, null, null, null, null);
        cHeart = myDbHelper.query("heart", null, null, null, null, null, null);

        if (cOxygen.moveToFirst()) {
                    do {
                        dateListOxygen.add(cOxygen.getString(1));
                        hourListOxygen.add(cOxygen.getString(2));
                        valueListOxygen.add(cOxygen.getString(3));
                    } while (cOxygen.moveToNext());
                };
        dateValueOxygen = dateListOxygen.toArray(new String[dateListOxygen.size()]);
        hourValueOxygen = hourListOxygen.toArray(new String[hourListOxygen.size()]);
        valueOxygen = valueListOxygen.toArray(new String[valueListOxygen.size()]);
        valuesCounterOxygen = valueListOxygen.size();
        graphViewOxygen.getGridLabelRenderer().setNumHorizontalLabels(valuesCounterOxygen);
        seriesOxygen = new LineGraphSeries<>(getDataPointOxygen(valuesCounterOxygen));
        graphViewOxygen.addSeries(seriesOxygen);
        seriesOxygen.setColor(Color.rgb(190,229,176));
        seriesOxygen.setDrawDataPoints(true);
        seriesOxygen.setDataPointsRadius(10);
        seriesOxygen.setThickness(8);

        if (cHeart.moveToFirst()) {
                    do {
                        dateListHeart.add(cHeart.getString(1));
                        hourListHeart.add(cHeart.getString(2));
                        valueListHeart.add(cHeart.getString(3));
                    } while (cHeart.moveToNext());
                };
        dateValueHeart = dateListHeart.toArray(new String[dateListHeart.size()]);
        hourValueHeart = hourListHeart.toArray(new String[hourListHeart.size()]);
        valueHeart = valueListHeart.toArray(new String[valueListHeart.size()]);
        valuesCounterHeart = valueListHeart.size();
        graphViewHeart.getGridLabelRenderer().setNumHorizontalLabels(valuesCounterHeart);
        seriesHeart = new LineGraphSeries<>(getDataPointHeart(valuesCounterHeart));
        graphViewHeart.addSeries(seriesHeart);
        //color points for heartRate chart
        seriesHeart.setColor(Color.rgb(190,229,176));
        seriesHeart.setDrawDataPoints(true);
        seriesHeart.setDataPointsRadius(10);
        seriesHeart.setThickness(8);
    }

    private DataPoint[] getDataPointOxygen(int valuesCounter) {
        String[] columns = {"date", "value"};
        Cursor cursor = sqLiteDatabaseObjOxygen.query("oxygen", columns, null, null, null, null, null);
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getDouble(0), cursor.getInt(1));
        }
    return dp;
    }

    private DataPoint[] getDataPointHeart(int valuesCounter) {
        String[] columns = {"date", "value"};
        Cursor cursor = sqLiteDatabaseObjOxygen.query("heart", columns, null, null, null, null, null);
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getDouble(0), cursor.getInt(1));
        }
    return dp;
    }
}