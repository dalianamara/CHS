package cg.healthyapp;

import android.content.Intent;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.os.Build;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class AuthenticationActivity extends FragmentActivity { // AppCompatActivity,

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);


        //Select button and txt_msg_authentication
        TextView txt_msg_authentication = findViewById(R.id.txt_msg_authentication);
        Button login_btn = (Button) findViewById(R.id.login_btn);

        //Create the biometric manager and check if user can use the fingerprint or not
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                //txt_msg_authentication.setText("You can use the fingerprint sensor to login");
                //txt_msg_authentication.setTextColor(Color.parseColor("#fafafa"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                txt_msg_authentication.setText("This device don't have a fingerprint sensor");
                login_btn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                txt_msg_authentication.setText("The fingerprint sensor is currently unavailable");
                login_btn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                txt_msg_authentication.setText("Your device haven't any fingerprint saved");
                login_btn.setVisibility(View.GONE);
                break;
        }

        //Creat a biometric dialog box;
        Executor executor = ContextCompat.getMainExecutor(this);
        FragmentActivity activity = this;

        //Need to create a biometric callback which give the result of the authentication
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login success !", Toast.LENGTH_SHORT).show();
                openApp();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        //Create Biometric dialog
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                //.setDescription("Use the fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();

        login_btn.setOnClickListener((View v) -> {
            biometricPrompt.authenticate(promptInfo);
        });

    }

    public void openApp(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}