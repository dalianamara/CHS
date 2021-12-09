package cg.healthyapp.BottomNavigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cg.healthyapp.Components.SensorListener;
import cg.healthyapp.R;
import cg.healthyapp.util.API26Wrapper;

import java.util.Locale;


public class MyProfile extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final float DEFAULT_STEP_SIZE = Locale.getDefault() == Locale.US ? 2.5f : 75f;;
    public static final String DEFAULT_STEP_UNIT =Locale.getDefault() == Locale.US ? "ft" : "cm" ;
    public static final float DEFAULT_GOAL = 10000;
    private LinearLayout setgoal,stepsize,shownotification;
    private TextView goal;
    public static int Position;
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_my_profile,null);
        setgoal=view.findViewById(R.id.setgoallayout);
        stepsize=view.findViewById(R.id.stepsizelayout);
       shownotification=view.findViewById(R.id.shownotilayout);
        goal=view.findViewById(R.id.goaltext);
       setgoal.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loadsetgoal();
           }
       });
        stepsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences prefs= getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
                 v= getActivity().getLayoutInflater().inflate(R.layout.stepsize, null);
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                final RadioGroup unit =v.findViewById(R.id.unit);
                final EditText value=v.findViewById(R.id.value);
                unit.check(prefs.getString("stepsize_unit",DEFAULT_STEP_UNIT).equals("cm") ? R.id.cm : R.id.ft);
                value.setText(String.valueOf(prefs.getFloat("stepsize_value",DEFAULT_STEP_SIZE)));
                builder.setView(v);
                builder.setTitle(R.string.set_step_size);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      try{
                          prefs.edit().putFloat("stepsize_value",
                                  Float.valueOf(value.getText().toString()))
                                  .putString("stepsize_unit",unit.getCheckedRadioButtonId()==R.id.cm ? "cm" : "ft")
                                  .apply();
                          value.setText(getString(R.string.step_size_summary,
                                  Float.valueOf(value.getText().toString()),
                                  unit.getCheckedRadioButtonId()==R.id.cm ? "cm" : "ft"));
                      } catch (NumberFormatException e) {
                          e.printStackTrace();
                      }
                      dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                Dialog dialog = builder.create();
                dialog.show();

            }
        });
         shownotification.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 loadNotification();
             }
         });
        return view;
    }

    private void loadNotification() {
        NotificationManager manager=(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if((Boolean) check()){
            manager.notify(SensorListener.NOTIFICATION_ID,SensorListener.getNotification(getActivity()));

        }else {
            manager.cancel(SensorListener.NOTIFICATION_ID);
        }

    }

    private Boolean check() {
        API26Wrapper.launchNotificationSettings(getActivity());
        return true;
    }

    private void loadsetgoal() {
        final SharedPreferences prefs= getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final NumberPicker np = new NumberPicker(getActivity());
        np.setMinValue(1);
        np.setMaxValue(100000);
        np.setValue(prefs.getInt("goal",10000));
        builder.setView(np);
        builder.setTitle(R.string.set_goal);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                np.clearFocus();
                prefs.edit().putInt("goal",np.getValue()).commit();
                goal.setText(getString(R.string.goal_summary,np.getValue()));
                dialog.dismiss();
                getActivity().startService(new Intent(getActivity(), SensorListener.class).putExtra("updateNotificationState",true));
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  Position=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}