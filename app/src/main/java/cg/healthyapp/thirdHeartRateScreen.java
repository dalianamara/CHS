package cg.healthyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class thirdHeartRateScreen extends AppCompatActivity {
    Button next_Activity_button;
    double resultHeartRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_heart_rate_screen);
        next_Activity_button = (Button) findViewById(R.id.backBtn);
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
    }
}