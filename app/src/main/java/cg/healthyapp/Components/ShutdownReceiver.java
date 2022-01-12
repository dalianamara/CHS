package cg.healthyapp.Components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cg.healthyapp.BuildConfig;
import cg.healthyapp.Database.Database;
import cg.healthyapp.util.Logger;
import cg.healthyapp.util.Util;


public class ShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (BuildConfig.DEBUG) Logger.log("shutting down");

        context.startService(new Intent(context, SensorListener.class));
        context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit()
                .putBoolean("correctShutdown", true).commit();

        Database db = Database.getInstance(context);
        if (db.getSteps(Util.getToday()) == Integer.MIN_VALUE) {
            int steps = db.getCurrentSteps();
            db.insertNewDay(Util.getToday(), steps);
        } else {
            db.addToLast(db.getCurrentSteps());
        }
        db.close();
    }

}