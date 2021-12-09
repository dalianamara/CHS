package cg.healthyapp;

import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import cg.healthyapp.Math.Fft;

public class secondOxygenLevelScreen extends AppCompatActivity {
    Button next_Activity_button;
    private Button stop_button;

    private static final String TAG = "OxygenMeasure";
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private Camera camera = null;
    private static SurfaceHolder previewHolder = null;
    private SurfaceView preview = null;
    private static PowerManager.WakeLock makeLock = null;

    private Toast o2Toast;
    private ProgressBar progressBarO2;
    public int ProgP = 0;
    public int inc = 0;
    private static long startTime = 0;
    private double SamplingFreq;
    private static final double RedBlueRadio = 0;
    double Stdr = 0;
    double Stdb = 0;
    double sumred = 0;
    double sumblue = 0;
    public int o2;

    public ArrayList<Double> RedAvgList = new ArrayList<>();
    public ArrayList<Double> BlueAvgList = new ArrayList<>();
    public int counter = 0;


    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_oxygen_level_screen);

        next_Activity_button = (Button) findViewById(R.id.backBtn);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(secondOxygenLevelScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        stop_button = (Button) findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), firstOxygenLevelScreen.class));
            }
        });

        preview = findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        progressBarO2 = findViewById(R.id.progressBarO2);
        progressBarO2.setProgress(0);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        makeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "NOTDIMSCREEN");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeLock.acquire();
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private final Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (data == null)
                throw new NullPointerException();
            Camera.Size size = camera.getParameters().getPreviewSize();
            if (size == null)
                throw new NullPointerException();
            if (!processing.compareAndSet(false, true))
                return;

            int width = size.width;
            int height = size.height;
            double RedAvg;
            double BlueAvg;

            RedAvg = ImageProcessing.colordecoderRGB(data.clone(), height, width,1);
            sumred += RedAvg;
            BlueAvg = ImageProcessing.colordecoderRGB(data.clone(), height, width,2);
            sumblue += BlueAvg;

            RedAvgList.add(RedAvg);
            BlueAvgList.add(BlueAvg);
            ++counter;

            if (RedAvg < 200) {
                inc = 0;
                ProgP = inc;
                progressBarO2.setProgress(ProgP);
                processing.set(false);
            }

            long endTime = System.currentTimeMillis();
            double totalTimeSecs = (endTime - startTime) / 1000d;

            if (totalTimeSecs >= 30) {
                startTime = System.currentTimeMillis();
                SamplingFreq = (counter / totalTimeSecs);
                Double[] Red = RedAvgList.toArray(new Double[RedAvgList.size()]);
                Double[] Blue = BlueAvgList.toArray(new Double[BlueAvgList.size()]);
                double HRFreq = Fft.FFT(Red, counter, SamplingFreq);
                double bpm = (int) ceil(HRFreq * 60);
                double meanr = sumred / counter;
                double meanb = sumblue / counter;

                for (int i = 0; i < counter - 1; i++) {
                    Double bufferb = Blue[i];
                    Stdb += ((bufferb - meanb) * (bufferb - meanb));

                    Double bufferr = Red[i];
                    Stdr += ((bufferr - meanr) * (bufferr - meanr));
                }

                double varr = sqrt(Stdr / (counter - 1));
                double varb = sqrt(Stdb / (counter - 1));
                double R = (varr / meanr) / (varb / meanb);
                double spo2 = 100 - 5 * R;
                o2 = (int) (spo2);

                if ((o2 < 80 || o2 > 99) || (bpm < 45 || bpm > 200)) {
                    inc = 0;
                    ProgP = inc;
                    progressBarO2.setProgress(ProgP);
                    //o2Toast = Toast.makeText(getApplicationContext(), "Measurement Failure", Toast.LENGTH_SHORT);
                    //o2Toast.show();
                    startTime = System.currentTimeMillis();
                    counter = 0;
                    processing.set(false);

                    Intent i = new Intent(secondOxygenLevelScreen.this, firstOxygenLevelScreen.class);
                    startActivity(i);
                    finish();
                    return;
                }

                }
            if (o2 != 0) {
                Intent i = new Intent(secondOxygenLevelScreen.this, thirdOxygenLevelScreen.class);
                i.putExtra("textO2Count", o2);
                startActivity(i);
                finish();

            }

            if (RedAvg != 0) {
                ProgP = inc++ / 34;
                progressBarO2.setProgress(ProgP);
            }
            processing.set(false);
        }
    };

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.d(TAG, "surfaceCreated: ", t);
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);

            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        }
    };

    private Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {

        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea)
                        result = size;
                }
            }
        }
        return result;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent i = new Intent(secondOxygenLevelScreen.this, firstOxygenLevelScreen.class);
        startActivity(i);
        finish();
    }
}

