package cg.healthyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class firstOxygenLevelScreen extends AppCompatActivity {
    Button next_Activity_button;
    private Button startO2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_oxygen_level_screen);
        next_Activity_button = (Button)findViewById(R.id.backBtn);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent intent = new Intent(firstOxygenLevelScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        startO2 = (Button) findViewById(R.id.startO2);
        startO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), secondOxygenLevelScreen.class));
            }
        });
    }
}