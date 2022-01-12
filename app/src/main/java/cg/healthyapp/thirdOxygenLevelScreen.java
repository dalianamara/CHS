package cg.healthyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class thirdOxygenLevelScreen extends AppCompatActivity {
    Button next_Activity_button, readCourseBtn;
    long DateHolder;
    String HourHolder;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    Calendar today = Calendar.getInstance();
    int o2;
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_oxygen_level_screen);
        next_Activity_button = (Button) findViewById(R.id.backBtn);
        dbHandler = new DBHandler(thirdOxygenLevelScreen.this);

        next_Activity_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(thirdOxygenLevelScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        TextView mtextO2Count = this.findViewById(R.id.textO2Count);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            o2 = bundle.getInt("textO2Count");
            mtextO2Count.setText(String.valueOf(o2));
        }

        DateHolder = new Date().getTime();
        HourHolder = sdf.format(today.getTime());
        dbHandler.addNewPulse(DateHolder, HourHolder, String.valueOf(o2), "oxygen");
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
    }
