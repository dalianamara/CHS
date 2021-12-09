package cg.healthyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class thirdOxygenLevelScreen extends AppCompatActivity {
    Button next_Activity_button;

    private String Date;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    int o2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_oxygen_level_screen);
        next_Activity_button = (Button) findViewById(R.id.backBtn);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(thirdOxygenLevelScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        Date = dateFormat.format(today);
        TextView mtextO2Count = this.findViewById(R.id.textO2Count);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            o2 = bundle.getInt("textO2Count");
            mtextO2Count.setText(String.valueOf(o2));
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}