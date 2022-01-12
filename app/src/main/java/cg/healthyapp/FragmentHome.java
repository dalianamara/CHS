package cg.healthyapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cg.healthyapp.Components.SensorListener;
import cg.healthyapp.Database.Database;
import cg.healthyapp.util.API26Wrapper;
import cg.healthyapp.util.Logger;
import cg.healthyapp.util.Util;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.j4velin.lib.colorpicker.BuildConfig;

public class FragmentHome extends Fragment  implements SensorEventListener{
    public static NumberFormat formatter=NumberFormat.getInstance(Locale.getDefault());
    private TextView steps_View,total_View,average_View,calories;
    private PieModel sliceCurrent;
    private PieChart pg;
    public static int total_steps=0;
    private int todays_offset, total_start,since_boot, total_days;
    private boolean showSteps = true;
    Button next_Activity_button;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(Build.VERSION.SDK_INT>=26){
            API26Wrapper.startForegroundService(getActivity(),new Intent(getActivity(), SensorListener.class));
        }
        else{
            getActivity().startService(new Intent(getActivity(), SensorListener.class));
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,  final  ViewGroup container, final  Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_home,null);
        steps_View=view.findViewById(R.id.stepspiechart);
        total_View=view.findViewById(R.id.total);
        average_View=view.findViewById(R.id.average);
        calories=view.findViewById(R.id.calories);
        pg=view.findViewById(R.id.graph);
        setPiechart();
        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSteps = !showSteps;
                stepsDistanceChanges();
            }
        });

        next_Activity_button = view.findViewById(R.id.backBtnSteps);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Database db = Database.getInstance(getActivity());
        if (BuildConfig.DEBUG) db.logState();
        todays_offset = db.getSteps(Util.getToday());
        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        since_boot = db.getCurrentSteps();
        int pauseDifference = since_boot - prefs.getInt("pauseCount", since_boot);

        // register a sensorlistener to live update the UI if a step is taken
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.no_sensor)
                    .setMessage(R.string.no_sensor_explain)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(final DialogInterface dialogInterface) {
                            getActivity().finish();
                        }
                    }).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
        }
        since_boot -= pauseDifference;
        total_start = db.getTotalStepsWithoutToday();
        total_days = db.getDays();
        db.close();
        stepsDistanceChanges();
    }

    private void stepsDistanceChanges() {
        if(showSteps){
            ((TextView) getView().findViewById(R.id.unit)).setText(getString(R.string.steps));
        }
        updatePie();
        updateBars();

    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            SensorManager sm=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            if(BuildConfig.DEBUG) Logger.log(e);
            e.printStackTrace();
        }
        Database db= Database.getInstance(getActivity());
        db.saveCurrentSteps(since_boot);
        db.close();
    }

    private void updatePie() {
        if(BuildConfig.DEBUG) Logger.log("UI-updatesteps:"+since_boot);
        int steps_today=Math.max(todays_offset+since_boot,0);
        sliceCurrent.setValue(steps_today);
        if(steps_today>0) {
            pg.clearChart();
            pg.addPieSlice(sliceCurrent);
        }
        pg.update();
        if(showSteps){
            steps_View.setText(formatter.format(steps_today));
            double kcal=steps_today*0.04;
            calories.setText(formatter.format(kcal));
            total_View.setText(formatter.format(total_start+steps_today));
            average_View.setText(formatter.format((total_start+steps_today)/total_days));
            total_steps=total_start+steps_today;
        }
        else{
            SharedPreferences prefs=getActivity().getSharedPreferences("pedometer",Context.MODE_PRIVATE);
            total_steps=total_start+steps_today;
        }
    }
    private void updateBars() {

        SimpleDateFormat df= new SimpleDateFormat("E",Locale.getDefault());
        BarChart barChart =(BarChart) getView().findViewById(R.id.bargraph);
        if(barChart.getData().size()>0) barChart.clearChart();
        int steps;

        SharedPreferences prefs = getActivity().getSharedPreferences("pedometer",Context.MODE_PRIVATE);
        barChart.setShowDecimal(!showSteps);
        BarModel bm;
        Database db=Database.getInstance(getActivity());
        List<Pair<Long,Integer>> last = db.getLastEntries(7);
        db.close();
        for(int i=last.size()-1;i>0;i--) {
            Pair<Long, Integer> current = last.get(i);
            steps = current.second;
            if (steps > 0) {
                bm = new BarModel(df.format(new Date(current.first)), 0,Color.parseColor("#81B622"));
                if (showSteps) {
                    bm.setValue(steps);
                }
                barChart.addBar(bm);
            }
        }
        if(barChart.getData().size()>0){
            barChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog_Statistics.getDialog(getActivity(),since_boot).show();
                }
            });
        }
    }

    private void setPiechart() {
        sliceCurrent=new PieModel(0, Color.parseColor("#69F0AE"));
        pg.addPieSlice(sliceCurrent);
        pg.setDrawValueInPie(false);
        pg.setUsePieRotation(true);
        pg.startAnimation();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(BuildConfig.DEBUG) Logger.log(
                "UI- sensorchanged| todatyoffset:"+todays_offset+"sinceboot:"+since_boot+event.values[0]);
        if(event.values[0]>Integer.MAX_VALUE || event.values[0]==0){
            todays_offset=-(int) event.values[0];
            Database db= Database.getInstance(getActivity());
            db.insertNewDay(Util.getToday(),(int)event.values[0]);
            db.close();

        }
        since_boot=(int)event.values[0];
        updatePie();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //will not change
    }
}