package com.battery.saver.batterysaver.activitys;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.models.SharedPreferencesManager;
import com.battery.saver.batterysaver.services.Service;
import com.battery.saver.batterysaver.services.ServiceWindowManager;
import com.battery.saver.batterysaver.ulits.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mSettingBackIv;
    private ToggleButton mNotificationTg;
    private ToggleButton mCreateToiUuTg;
    private ToggleButton mCreateBtTg;
    private ToggleButton mDangerTg;
    private boolean isChecked, isChecked1, isChecked2, isChecked3;
    private TextView mTitleSettingTv;
    private TextView mThongbaoTv;
    private TextView mToiUuHoaTv;
    private TextView mTietKiemPinTv;
    private TextView mPinYeuTv;
    private TextView mChiaSeTv;
    private TextView mDanhGiaTv;
    private Intent shortcutintentTKP, shortcutintentToiUu;
    private AdView mAdView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        readSharePreSetting();
        createAds();

    }

    private void createAds() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initView() {
        mSettingBackIv = (ImageView) findViewById(R.id.iv_setting_back);
        mSettingBackIv.setOnClickListener(this);

        mNotificationTg = (ToggleButton) findViewById(R.id.Tg_notification);
        mCreateToiUuTg = (ToggleButton) findViewById(R.id.Tg_create_toi_uu);
        mCreateBtTg = (ToggleButton) findViewById(R.id.Tg_create_bt);
        mDangerTg = (ToggleButton) findViewById(R.id.Tg_danger);

        mTitleSettingTv = findViewById(R.id.tv_title_setting);
        mThongbaoTv = findViewById(R.id.tv_thongbao);
        mToiUuHoaTv = findViewById(R.id.tv_toi_uu_hoa);
        mTietKiemPinTv = findViewById(R.id.tv_tiet_kiem_pin);
        mPinYeuTv = findViewById(R.id.tv_pin_yeu);
        mChiaSeTv = findViewById(R.id.tv_chia_se);
        mDanhGiaTv = findViewById(R.id.tv_danh_gia);


        mNotificationTg.setOnClickListener(this);
        mCreateToiUuTg.setOnClickListener(this);
        mCreateBtTg.setOnClickListener(this);
        mDangerTg.setOnClickListener(this);
        mChiaSeTv.setOnClickListener(this);
        mDanhGiaTv.setOnClickListener(this);

        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mTitleSettingTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mThongbaoTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mToiUuHoaTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mTietKiemPinTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mPinYeuTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mChiaSeTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mDanhGiaTv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_back:
                onBackPressed();
                break;
            case R.id.Tg_notification:
                if (isChecked == false) {
                    isChecked = true;
                    Intent intent = new Intent(this, Service.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                    SharedPreferencesManager.setCheck_Noti(this, true);
                } else {
                    isChecked = false;
                    stopService(new Intent(this, Service.class));
                    SharedPreferencesManager.setCheck_Noti(this, false);
                }
                break;
            case R.id.Tg_create_toi_uu:
                if (isChecked1 == false) {
                    isChecked1 = true;

                    createShortCut_Toi_Uu();
                    SharedPreferencesManager.setToi_Uu(this, true);
                } else {
                    isChecked1 = false;
                    SharedPreferencesManager.setToi_Uu(this, false);
                }
                break;
            case R.id.Tg_create_bt:
                if (isChecked2 == false) {
                    isChecked2 = true;
                    createShortCut_tiet_kiem_pin();
                    SharedPreferencesManager.setTiet_Kiem(this, true);
                } else {
                    unShortCut();
                    isChecked2 = false;
                    SharedPreferencesManager.setTiet_Kiem(this, false);
                }
                break;
            case R.id.Tg_danger:
                if (isChecked3 == false) {
                    isChecked2 = true;
                    // demoShortcut();
                    SharedPreferencesManager.setDanger(this, true);
                } else {
                    isChecked2 = false;
                    SharedPreferencesManager.setDanger(this, false);
                }
                break;
            case R.id.tv_chia_se:
                String linkApp = "https://play.google.com/store/apps/details?id=" + getPackageName();
                Intent intent3 = new Intent("android.intent.action.SEND");
                intent3.setType("text/plain");
                intent3.addFlags(12345);
                intent3.putExtra("android.intent.extra.SUBJECT", getString(R.string.new_app_name));
                intent3.putExtra("android.intent.extra.TEXT", linkApp);
                SettingActivity.this.startActivity(Intent.createChooser(intent3, getResources().getString(R.string.Share)));
                break;
            case R.id.tv_danh_gia:
                MainActivity.ratingApp(this);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void readSharePreSetting() {
        mNotificationTg.setChecked(SharedPreferencesManager.getCheck_Noti(this));
        mCreateToiUuTg.setChecked(SharedPreferencesManager.getToi_Uu(this));
        mCreateBtTg.setChecked(SharedPreferencesManager.getTiet_Kiem(this));
        mDangerTg.setChecked(SharedPreferencesManager.getDanger(this));
        isChecked = SharedPreferencesManager.getCheck_Noti(this);
        isChecked1 = SharedPreferencesManager.getToi_Uu(this);
        isChecked2 = SharedPreferencesManager.getTiet_Kiem(this);
        isChecked3 = SharedPreferencesManager.getDanger(this);
    }

    public void createShortCut_Toi_Uu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
                final ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, Constants.ID_CREAETE_SHORCUT_TUH)
                        .setIcon(Icon.createWithResource(this, R.drawable.toi_uu))
                        .setShortLabel(getResources().getString(R.string.che_do_toi_uu))
                        .setIntent(new Intent(this, ToiUuActivity.class).setAction(Intent.ACTION_MAIN))
                        .build();
                shortcutManager.requestPinShortcut(pinShortcutInfo, null);
            }

        } else {
            shortcutintentToiUu = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            Intent demo = new Intent(this, ToiUuActivity.class);
            shortcutintentToiUu.putExtra("duplicate", false);
            shortcutintentToiUu.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.che_do_toi_uu));
            Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.toi_uu);
            shortcutintentToiUu.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            shortcutintentToiUu.putExtra(Intent.EXTRA_SHORTCUT_INTENT, demo);
            sendBroadcast(shortcutintentToiUu);
        }
    }

    public void createShortCut_tiet_kiem_pin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
                final ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, Constants.ID_CREAETE_SHORCUT_TKP)
                        .setIcon(Icon.createWithResource(this, R.drawable.chedo_tietkiempin))
                        .setShortLabel(getResources().getString(R.string.che_do_TKP))
                        .setIntent(new Intent(this, TietKiemPinMotCham.class).setAction(Intent.ACTION_MAIN))
                        .build();
                shortcutManager.requestPinShortcut(pinShortcutInfo, null);
            }
        } else {
            shortcutintentTKP = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            Intent demo = new Intent(this, TietKiemPinMotCham.class);
            shortcutintentTKP.putExtra("duplicate", false);
            shortcutintentTKP.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.che_do_TKP));
            Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.chedo_tietkiempin);
            shortcutintentTKP.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            shortcutintentTKP.putExtra(Intent.EXTRA_SHORTCUT_INTENT, demo);
            sendBroadcast(shortcutintentTKP);
        }
    }

    public void demoShortcut() {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        Intent demo = new Intent(this, TietKiemPinMotCham.class);
        demo.setAction(Intent.ACTION_MAIN);
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, demo);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Test 123");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.app_icon));
        sendBroadcast(intent);
    }

    public void unShortCut() {
        Intent removeIntent = new Intent();
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.che_do_TKP));
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.chedo_tietkiempin);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutintentTKP);
        removeIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        this.sendBroadcast(removeIntent);
    }


    private void removeShortcutIcon(String app_name, Intent app_intent) {
        app_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent removeIntent = new Intent();
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, app_intent);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, app_name);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.app_icon));
        removeIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(removeIntent);
    }

    private void removeShortcut() {

        //Deleting shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "dddd");

        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    public void createShortcutAndroidToiUu() {
        if (Build.VERSION.SDK_INT < 26) {
            Intent shortcutIntent = new Intent(getApplicationContext(), ServiceWindowManager.class);
            startService(shortcutIntent);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getApplicationContext().getString(R.string.app_name));
            Intent.ShortcutIconResource icon =
                    Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_menu_camera);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            addIntent.putExtra("duplicate", false);
            getApplicationContext().sendBroadcast(addIntent);
            Toast.makeText(getApplicationContext()
                    , "Created pre-api26 shortcut"
                    , Toast.LENGTH_LONG)
                    .show();
        } else {
            ShortcutManager shortcutManager
                    = SettingActivity.this.getSystemService(ShortcutManager.class);
            if (shortcutManager.isRequestPinShortcutSupported()) {
                Intent intent = new Intent(
                        SettingActivity.this.getApplicationContext(), SettingActivity.this.getClass());
                intent.setAction(Intent.ACTION_MAIN);
                ShortcutInfo pinShortcutInfo = new ShortcutInfo
                        .Builder(SettingActivity.this, "pinned-shortcut")
                        .setIcon(
                                Icon.createWithResource(SettingActivity.this, R.drawable.ic_launcher_background)
                        )
                        .setIntent(intent)
                        .setShortLabel(SettingActivity.this.getString(R.string.thirty_minute))
                        .build();
                Intent pinnedShortcutCallbackIntent = shortcutManager
                        .createShortcutResultIntent(pinShortcutInfo);
                pinnedShortcutCallbackIntent.putExtra("duplicate", false);
                //Get notified when a shortcut is pinned successfully//
                PendingIntent successCallback
                        = PendingIntent.getBroadcast(
                        SettingActivity.this, 0
                        , pinnedShortcutCallbackIntent, 0
                );
                shortcutManager.requestPinShortcut(
                        pinShortcutInfo, successCallback.getIntentSender()
                );
            }
        }
    }
}

