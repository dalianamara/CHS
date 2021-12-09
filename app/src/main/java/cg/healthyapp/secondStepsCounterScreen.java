package cg.healthyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class secondStepsCounterScreen extends AppCompatActivity {
    Button next_Activity_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_steps_counter_screen);
        next_Activity_button = (Button) findViewById(R.id.backBtn);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(secondStepsCounterScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}