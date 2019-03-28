package com.battery.saver.batterysaver.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.adapters.ClearMemoryAdapter;
import com.battery.saver.batterysaver.bean.AppProcessInfo;
import com.battery.saver.batterysaver.models.SharedPreferencesManager;
import com.battery.saver.batterysaver.models.StorageSize;
import com.battery.saver.batterysaver.services.CleanerService;
import com.battery.saver.batterysaver.services.CoreService;
import com.battery.saver.batterysaver.ulits.Constants;
import com.battery.saver.batterysaver.ulits.StorageUtil;
import com.battery.saver.batterysaver.widgets.RotateLoading;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

public class TuyChonNangLuong extends AppCompatActivity implements CoreService.OnPeocessActionListener, ClearMemoryAdapter.OnItemCheckedChangeListener, View.OnClickListener {
private Dialog dlAni;
    public long Allmemory;
    List<AppProcessInfo> mAppProcessInfos = new ArrayList();
    ClearMemoryAdapter mClearMemoryAdapter;
    private CoreService mCoreService;
    long mKillAppmemory = 0;
    private ListView mListView;
    private Context context;

    private ImageView m1Img;
    private ImageView m2Img;
    private ImageView m3Img;
    private ImageView m4Img;
    private ImageView m5Img;
    private ImageView m6Img;
    private ImageView m7Img;
    private ImageView m8Img;
    private ImageView m9Img;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            TuyChonNangLuong.this.mCoreService = ((CoreService.ProcessServiceBinder) service).getService();
            TuyChonNangLuong.this.mCoreService.setOnActionListener(TuyChonNangLuong.this);
            TuyChonNangLuong.this.mCoreService.scanRunProcess();
        }

        public void onServiceDisconnected(ComponentName name) {
            TuyChonNangLuong.this.mCoreService.setOnActionListener(null);
            TuyChonNangLuong.this.mCoreService = null;
        }
    };
    private Button mClearAppBtn;
    private TextView mNangluongTitle,mProgressBarText;
    private TextView mApp;
    private ImageView mBackNangLuongIv;
    private RotateLoading mRotateLoading;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuy_chon_nang_luong);
        initView();
        startService(new Intent(this, CoreService.class));
        startService(new Intent(this, CleanerService.class));

        context = getApplicationContext();
        this.mKillAppmemory = 0;
        this.mClearMemoryAdapter = new ClearMemoryAdapter(context, this.mAppProcessInfos);
        this.mClearMemoryAdapter.SetOnItemCheckChangeListener(this);
        this.mListView.setAdapter(this.mClearMemoryAdapter);
        bindService(new Intent(TuyChonNangLuong.this, CoreService.class), this.mServiceConnection, 1);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {

                createDialogAni();
                initView(dlAni);
                setAni();
                CleanMemory();
                CountDownTimer count = new CountDownTimer(4000,1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        startActivity(new Intent(TuyChonNangLuong.this,ClearDoneActivity.class));
                        TuyChonNangLuong.this.finish();
                    }
                }.start();


                loadInterstitialAd();
            }
        });
        //Load sẵn quảng cáo khi ứng dụng mở
        loadInterstitialAd();
    }

    private void initView() {
        mRotateLoading =findViewById(R.id.rotate_loading);
        mProgressBarText = findViewById(R.id.progressBarText);
        mListView = findViewById(R.id.lv_list_app);
        mClearAppBtn = (Button) findViewById(R.id.btn_clear_app);
        mClearAppBtn.setOnClickListener(this);

        mNangluongTitle = findViewById(R.id.title_nangluong);
        mApp = findViewById(R.id.app);
        mBackNangLuongIv = findViewById(R.id.iv_back_nang_luong);
        mBackNangLuongIv.setOnClickListener(this);

        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mNangluongTitle);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mApp);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mClearAppBtn);
    }


    @Override
    public void OnCheckedChange(AppProcessInfo appProcessInfo) {

    }

    @Override
    public void onScanStarted(Context context) {
        mProgressBarText.setText(R.string.scanning);
        this.mRotateLoading.start();
    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max, int size) {

    }

    @Override
    public void onScanCompleted(Context context, List<AppProcessInfo> apps) {
        mProgressBarText.setVisibility(View.GONE);
        mRotateLoading.setVisibility(View.GONE);
        this.mAppProcessInfos.clear();
        StorageSize mStorageSize = StorageUtil.convertStorageSize(this.mKillAppmemory);
        this.Allmemory = 0;
        for (AppProcessInfo appInfo : apps) {
            if (!appInfo.isSystem) {
                this.mAppProcessInfos.add(appInfo);
                this.Allmemory += appInfo.memory;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear_app:
                long timeSave = SharedPreferencesManager.getTimeAds(TuyChonNangLuong.this);
                long timeNow = System.currentTimeMillis();
                if (timeNow>timeSave){
                    mInterstitialAd.show();
                    SharedPreferencesManager.setTimeAds(this,timeNow+Constants.THREE_MINUTE);
                }else {
                    createDialogAni();
                    initView(dlAni);
                    setAni();
                    CleanMemory();
                    CountDownTimer count = new CountDownTimer(4000,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            startActivity(new Intent(TuyChonNangLuong.this,ClearDoneActivity.class));
                            TuyChonNangLuong.this.finish();
                        }
                    }.start();
                }



                break;
            case R.id.iv_back_nang_luong:
                onBackPressed();
                break;
            default:
                break;
        }
    }


    public void CleanMemory() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Long, Float, Long> asynTask =
                new AsyncTask<Long, Float, Long>() {
                    long killAppmemory = 0;
                    long mAllMem;
                    int size;

                    protected Long doInBackground(Long... voids) {
                        this.mAllMem = voids[0].longValue();
                        this.size = TuyChonNangLuong.this.mAppProcessInfos.size();
                        for (int i = TuyChonNangLuong.this.mAppProcessInfos.size() - 1; i >= 0; i--) {
                            if ((TuyChonNangLuong.this.mAppProcessInfos.get(i)).checked) {
                                this.killAppmemory = (TuyChonNangLuong.this.mAppProcessInfos.get(i)).memory + this.killAppmemory;
                                TuyChonNangLuong.this.mCoreService.killBackgroundProcesses((TuyChonNangLuong.this.mAppProcessInfos.get(i)).processName);
                                TuyChonNangLuong.this.mAppProcessInfos.remove(TuyChonNangLuong.this.mAppProcessInfos.get(i));
                            }
                            float progress = ((float) (this.size - i)) / ((float) this.size);
                            publishProgress(new Float[]{Float.valueOf(progress)});
                        }
                        return Long.valueOf(this.killAppmemory);
                    }

                    protected void onProgressUpdate(Float... values) {
                        super.onProgressUpdate(values);
                    }

                    protected void onPostExecute(Long l) {
                        super.onPostExecute(l);
                        this.mAllMem -= l.longValue();
                        if (this.mAllMem > 0) {
                        }
                    }
                }.execute(new Long[]{Long.valueOf(this.Allmemory)});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void createDialogAni(){
        dlAni = new Dialog(this);
        dlAni.requestWindowFeature(Window.FEATURE_NO_TITLE );
        dlAni.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlAni.setCancelable(false);
        dlAni.setContentView(R.layout.custom_dialog_ani);
        dlAni.show();

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
    private void setAni() {
        Animation aniRotate_sweep = AnimationUtils.loadAnimation(this, R.anim.rotate_sweep);
        Animation aniRotate_reverse = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse);
        Animation aniRotate_reverse_ver2 = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse_ver2);
        Animation aniRotate_reverse_ver3 = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse_ver3);
        m1Img.startAnimation(aniRotate_sweep);
        m3Img.startAnimation(aniRotate_reverse);
        m6Img.startAnimation(aniRotate_reverse_ver2);
        m9Img.startAnimation(aniRotate_reverse_ver3);

    }
    private void loadInterstitialAd() {
        if (mInterstitialAd != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            mInterstitialAd.loadAd(adRequest);
        }

    }
}


