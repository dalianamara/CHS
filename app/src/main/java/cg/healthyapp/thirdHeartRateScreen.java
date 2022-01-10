package cg.healthyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class thirdHeartRateScreen extends AppCompatActivity {
    Button next_Activity_button, readHeartRateBtn;
    double resultHeartRate;
    long DateHolder;
    String HourHolder;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    Calendar today = Calendar.getInstance();
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_heart_rate_screen);
        next_Activity_button = (Button) findViewById(R.id.backBtn);
        dbHandler = new DBHandler(thirdHeartRateScreen.this);

        next_Activity_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(thirdHeartRateScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        TextView mResultHeartRate = this.findViewById(R.id.resultHeartRate);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            resultHeartRate = bundle.getDouble("resultHeartRate");
            mResultHeartRate.setText(String.valueOf(resultHeartRate));
        }
        DateHolder = new Date().getTime();
        HourHolder = sdf.format(today.getTime());
        dbHandler.addNewPulse(DateHolder, HourHolder, String.valueOf(resultHeartRate), "heart");
    }
}