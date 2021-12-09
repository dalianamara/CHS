package cg.healthyapp.Components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import cg.healthyapp.BuildConfig;
import cg.healthyapp.util.API26Wrapper;
import cg.healthyapp.util.Logger;

public class AppUpdateReceiever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(BuildConfig.DEBUG) Logger.log("app updated");
        if(Build.VERSION.SDK_INT>=26){
            API26Wrapper.startForegroundService(context,new Intent(context,SensorListener.class));
        }
        else{
           context.startService(new Intent(context,SensorListener.class));
        }
    }
}
