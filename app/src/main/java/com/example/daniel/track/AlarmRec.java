package com.example.daniel.track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmRec extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm", "AlarmRec called!");
        context.startService(new Intent(context, WifiService.class));
    }
}
