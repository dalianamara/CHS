package cg.healthyapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class HeartRateFirstScreen extends AppCompatActivity {


    Button startHeart,next_Activity_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heartrate);


        next_Activity_button = (Button)findViewById(R.id.backBtn);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent intent = new Intent(HeartRateFirstScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        startHeart = findViewById(R.id.startHeart);
        startHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), secondHeartRateScreen.class));
            }
        });
    }
}





