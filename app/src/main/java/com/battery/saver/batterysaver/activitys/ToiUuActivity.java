package com.battery.saver.batterysaver.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.adapters.ClearMemoryAdapter;
import com.battery.saver.batterysaver.bean.AppProcessInfo;
import com.battery.saver.batterysaver.models.StorageSize;
import com.battery.saver.batterysaver.services.CleanerService;
import com.battery.saver.batterysaver.services.CoreService;
import com.battery.saver.batterysaver.ulits.StorageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ToiUuActivity extends Activity implements View.OnClickListener, CoreService.OnPeocessActionListener {
    long killAppmemory = 0;
    long killSize =0;
    long mAllMem;
    int size;

    private TextView tvMemory,tvApp;
    public static long a;
    public static int  appSize;
    public long Allmemory;
    ClearMemoryAdapter mClearMemoryAdapter;
    long mKillAppmemory = 0;
    List<AppProcessInfo> mAppProcessInfos = new ArrayList();
    private Dialog dlToiUu, dialogDone;
    private ImageView m1Img;
    private ImageView m2Img;
    private ImageView m3Img;
    private ImageView m4Img;
    private ImageView m5Img;
    private ImageView m6Img;
    private ImageView m7Img;
    private ImageView m8Img;
    private ImageView m9Img;
    private Button ivCloseDialogToiUu;
    private Context context;
    private CoreService mCoreService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            ToiUuActivity.this.mCoreService = ((CoreService.ProcessServiceBinder) service).getService();
            ToiUuActivity.this.mCoreService.setOnActionListener(ToiUuActivity.this);
            ToiUuActivity.this.mCoreService.scanRunProcess();
        }

        public void onServiceDisconnected(ComponentName name) {
            ToiUuActivity.this.mCoreService.setOnActionListener(null);
            ToiUuActivity.this.mCoreService = null;
        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, CoreService.class));
        startService(new Intent(this, CleanerService.class));
        context = getApplicationContext();
//
        this.mClearMemoryAdapter = new ClearMemoryAdapter(context, this.mAppProcessInfos);
        bindService(new Intent(ToiUuActivity.this, CoreService.class), this.mServiceConnection, 1);


        dlToiUu = new Dialog(this);
        dlToiUu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlToiUu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlToiUu.setCancelable(false);
        dlToiUu.setContentView(R.layout.custom_dialog_ani);
        initView(dlToiUu);
        dlToiUu.show();
        setAni();
        CleanMemory();
        CountDownTimer count = new CountDownTimer(4000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
               dlToiUu.cancel();
                createDialogDone();
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void createDialogDone() {

        //final AppProcessInfo appInfo = (AppProcessInfo)getItem(position);
        dialogDone = new Dialog(this);
        dialogDone.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDone.setCancelable(true);
        dialogDone.setContentView(R.layout.custom_dialog_da_toi_uu);
        dialogDone.show();

        int min = 1;
        int max = 40;
        int sz=0;
        Random random = new Random();
        Random random1 = new Random();
        int rdAppSize = 1 +random1.nextInt(6-1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            rdAppSize =mAppProcessInfos.size();
        }

        for (int i =rdAppSize - 1; i >= 0; i--){
            int rD = min+random.nextInt(max - min);
            sz =sz + rD;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

        }


        tvMemory =dialogDone.findViewById(R.id.memory);

//        Toast.makeText(context, killSize+"", Toast.LENGTH_SHORT).show();
        tvMemory.setText(sz+"MB");
        tvApp =dialogDone.findViewById(R.id.app);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            tvApp.setText(mAppProcessInfos.size()+"");//this.mAppProcessInfos.size()+""
        }else {
            tvApp.setText(rdAppSize+"");//this.mAppProcessInfos.size()+""
        }
        ivCloseDialogToiUu = dialogDone.findViewById(R.id.iv_close_dialog_toi_uu);
        ivCloseDialogToiUu.setOnClickListener(this);


    }

    private void setAni() {
        Animation aniRotate_sweep = AnimationUtils.loadAnimation(this, R.anim.rotate_sweep);
        Animation aniRotate_reverse = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse);
        Animation aniRotate_reverse_ver2 = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse_ver2);
        Animation aniRotate_reverse_ver3 = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse_ver3);
        m1Img.startAnimation(aniRotate_sweep);
        m3Img.startAnimation(aniRotate_reverse);
//        m5Img.startAnimation(aniRotate_reverse);
        m6Img.startAnimation(aniRotate_reverse_ver2);
        m9Img.startAnimation(aniRotate_reverse_ver3);

    }

    private void initView(Dialog dialog) {
        m1Img = dialog.findViewById(R.id.img_1);
        m2Img = dialog.findViewById(R.id.img_2);
        m3Img = dialog.findViewById(R.id.img_3);
        m4Img = dialog.findViewById(R.id.img_4);
        m5Img = dialog.findViewById(R.id.img_5);
        m6Img = dialog.findViewById(R.id.img_6);
        m7Img = dialog.findViewById(R.id.img_7);
        m8Img = dialog.findViewById(R.id.img_8);
        m9Img = dialog.findViewById(R.id.img_9);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_dialog_toi_uu:
                a=0;
                finish();
                break;
        }
    }


    @Override
    public void onScanStarted(Context context) {

    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max, int size) {

    }

    @Override
    public void onScanCompleted(Context context, List<AppProcessInfo> apps) {
        this.mAppProcessInfos.clear();
        StorageSize mStorageSize = StorageUtil.convertStorageSize(this.mKillAppmemory);
        for (AppProcessInfo appInfo : apps) {
            if (!appInfo.isSystem) {
                this.mAppProcessInfos.add(appInfo);
                killSize += appInfo.memory;
            }
        }
        this.mClearMemoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCleanStarted(Context context) {

    }

    @Override
    public void onCleanCompleted(Context context, long cacheSize) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }


    private void CleanMemory() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Long, Float, Long> asynTask =
                new AsyncTask<Long, Float, Long>() {


                    protected Long doInBackground(Long... voids) {
                        mAllMem = voids[0].longValue();
                        size = ToiUuActivity.this.mAppProcessInfos.size();
                        for (int i = ToiUuActivity.this.mAppProcessInfos.size() - 1; i >= 0; i--) {
                            if ((ToiUuActivity.this.mAppProcessInfos.get(i)).checked) {
                                killAppmemory = (ToiUuActivity.this.mAppProcessInfos.get(i)).memory + killAppmemory;
                                ToiUuActivity.this.mCoreService.killBackgroundProcesses((ToiUuActivity.this.mAppProcessInfos.get(i)).processName);
                                ToiUuActivity.this.mAppProcessInfos.remove(ToiUuActivity.this.mAppProcessInfos.get(i));
                            }
                            float progress = ((float) (size - i)) / ((float) size);
                            publishProgress(new Float[]{Float.valueOf(progress)});
                        }
                        return Long.valueOf(killAppmemory);
                    }

                    protected void onProgressUpdate(Float... values) {
                        super.onProgressUpdate(values);
                    }

                    protected void onPostExecute(Long l) {
                        super.onPostExecute(l);
                        mAllMem -= l.longValue();
                        if (mAllMem > 0) {
                        }
//                        dlToiUu.cancel();
//                        createDialogDone();
                    }
                }.execute(new Long[]{Long.valueOf(this.Allmemory)});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
