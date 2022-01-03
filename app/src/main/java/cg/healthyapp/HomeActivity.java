package cg.healthyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cg.healthyapp.ui.ActivityMain;
public class HomeActivity extends AppCompatActivity {

    private Button heartRateBtn;
    private Button oxygenBtn;
    private Button stepBtn;
    private Button stepBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        heartRateBtn = (Button) findViewById(R.id.heartRateBtn);
        heartRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HeartRateFirstScreen.class));
            }
        });

        oxygenBtn = (Button) findViewById(R.id.oxygenBtn);
        oxygenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), firstOxygenLevelScreen.class));
            }
        });

        stepBtn = (Button) findViewById(R.id.stepBtn);
        stepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityMain.class));
            }
        });

        stepBtn1 = (Button) findViewById(R.id.stepBtn1);
        stepBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Statistics.class));
            }
        });


    }
}