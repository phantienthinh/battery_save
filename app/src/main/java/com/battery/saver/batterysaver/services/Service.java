package com.battery.saver.batterysaver.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.activitys.CheDoActivity;
import com.battery.saver.batterysaver.activitys.TuyChonNangLuong;
import com.battery.saver.batterysaver.models.SharedPreferencesManager;
import com.battery.saver.batterysaver.ulits.Constants;

public class Service extends android.app.Service {

    private Notification notification;
    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;
    private Notification notificationDanger;
    private NotificationCompat.Builder builderDanger;
    private RemoteViews remoteViewsDanger;
    private NotificationManager notificationManager;
    private NotificationManager notificationManagerDanger;
    private IntentFilter filter;
    private BroadcastReceiver receiver;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerReceiver(this.mBatInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "android.intent.action.BATTERY_LOW":
                        if (SharedPreferencesManager.getDanger(context) == true) {
                            createNotiDanger();
                        }
                        break;
                    case Intent.ACTION_BATTERY_CHANGED:
                        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                        float temperature = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
                        int temp = (int) temperature;
                        SharedPreferencesManager.setNhietDo(context, temp + "ᵒC");
                        try {
                            remoteViews.setTextViewText(R.id.tv_nhietDo_noti, SharedPreferencesManager.getNhietDo(context));
                        } catch (Exception e) {

                        }
                        break;

                }
            }
        };
        filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_LOW");
        registerReceiver(receiver, filter);

    }

    private void createNotiDanger() {
        builderDanger = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(Constants.CHANNEL_ID_DANGER)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_LOW);


        notificationDanger = builderDanger.build();
        remoteViewsDanger = new RemoteViews(getPackageName(), R.layout.custom_layout_noti_danger);
        notificationDanger.contentView = remoteViewsDanger;
        notificationManagerDanger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String s = "go_tiet_kiem_pin";
        Intent go_app = new Intent(getBaseContext(), CheDoActivity.class);
        go_app.setFlags(go_app.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 4567, go_app, PendingIntent.FLAG_UPDATE_CURRENT);
        builderDanger.addAction(R.id.btn_noti_danger, s, pendingIntent);
        remoteViewsDanger.setOnClickPendingIntent(R.id.btn_noti_danger, pendingIntent);

        String channelName1 = "BatteryDanger";
        int importance = NotificationManager.IMPORTANCE_LOW;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID_DANGER, channelName1, importance);
            notificationManagerDanger.createNotificationChannel(mChannel);
            startForeground(Constants.NOTIFICATION_ID_DANGER, notificationDanger);
        } else {
            notificationManagerDanger.notify(Constants.NOTIFICATION_ID_DANGER, notificationDanger);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        khoiTaoNoification1();
        return START_STICKY;
    }

    private void khoiTaoNoification1() {

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(Constants.CHANNEL_ID)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_LOW);


        notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.tvTime_noti, SharedPreferencesManager.getTime(this));
        remoteViews.setTextViewText(R.id.tv_nhietDo_noti, SharedPreferencesManager.getNhietDo(this));
        notification.contentView = remoteViews;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;


        String s = "go_app";
        Intent go_app = new Intent(getBaseContext(), TuyChonNangLuong.class);
        go_app.putExtra("go_app", true);
        go_app.setFlags(go_app.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1234, go_app, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.id.btn_noti, s, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.btn_noti, pendingIntent);


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String channelName = "Battery";
        int importance = NotificationManager.IMPORTANCE_LOW;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
            startForeground(Constants.NOTIFICATION_ID, notification);
        } else {
            notificationManager.notify(Constants.NOTIFICATION_ID, notification);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(Constants.NOTIFICATION_ID);
        unregisterReceiver(this.mBatInfoReceiver);
        unregisterReceiver(receiver);
    }
}
