package com.battery.saver.batterysaver.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class ServiceWindowManager extends Service {
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    public ServiceWindowManager() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "create done", Toast.LENGTH_SHORT).show();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "aaa":
                        Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction("aaa");
        registerReceiver(receiver,filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
