package com.battery.saver.batterysaver.activitys;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.models.SharedPreferencesManager;
import com.battery.saver.batterysaver.ulits.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ChucNangActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean aBoolean_rung = false;
    public static boolean aBoolean_Bluetooth = false;
    public static boolean aBoolean_Wifi = false;
    public static boolean aBoolean_sound = false;
    public static boolean aBoolean_upload = false;
    public int brightness = 10;
    private TextView mTitleChucNangTv;
    private TextView mDoSangTv;
    private TextView mRungTv;
    private TextView mTimeScreenTv;
    private TextView mBlueToothTv;
    private TextView mWifiTv;
    private TextView mSoundTv;
    private TextView mUploadTv;
    private TextView tvDoSang, tvRung, tvTimeScreen, tvBlueTooth, tvWifi, tvSound, tvUpload, tvApDung;
    private ImageView mBackChucNangTv;
    private ImageView mApDungTv;
    private SeekBar mSeekBarChucNang;
    private RadioButton rad_15s;
    private RadioButton rad_30s;
    private RadioButton rad_1p;
    private RadioButton rad_15p;
    private RadioButton rad_30p;
    private RadioGroup rad_group;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;

    private Context mContext;
    private WifiManager wifiManager;
    private AudioManager audioManager;
    private AdView mAdView;

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    public static boolean setWifi(boolean enable, WifiManager wifi) {
        // BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean wifiEnabled = wifi.isWifiEnabled();
        if (enable && !wifiEnabled) {
            return wifi.setWifiEnabled(true);
        } else if (!enable && wifiEnabled) {
            return wifi.setWifiEnabled(false);
        }
        // No need to change bluetooth state
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuc_nang);
        createVariable();
        initView();
        evenClick();
        checkDK();
        readSharePreChucNang();
        createAds();

    }

    private void createAds() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void createVariable() {
        mContext = getApplicationContext();
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        //Get the content resolver
        cResolver = getContentResolver();

//Get the current window
        window = getWindow();
        try {
            // To handle the auto
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

    }

    private void checkDK() {
        int a = SharedPreferencesManager.getPosition_Che_Do(this);
        Log.e("1122", a+"");

        if (a == Constants.POSITION_TUY_CHINH) {
            mDoSangTv.setClickable(true);
            mRungTv.setClickable(true);
            mTimeScreenTv.setClickable(true);
            mBlueToothTv.setClickable(true);
            mWifiTv.setClickable(true);
            mSoundTv.setClickable(true);
            mUploadTv.setClickable(true);
        } else {
            mDoSangTv.setClickable(false);
            mRungTv.setClickable(false);
            mTimeScreenTv.setClickable(false);
            mBlueToothTv.setClickable(false);
            mWifiTv.setClickable(false);
            mSoundTv.setClickable(false);
            mUploadTv.setClickable(false);
        }
    }

    private void evenClick() {
        mApDungTv.setOnClickListener(this);
        mBackChucNangTv.setOnClickListener(this);
        mDoSangTv.setOnClickListener(this);
        mRungTv.setOnClickListener(this);
        mTimeScreenTv.setOnClickListener(this);
        mBlueToothTv.setOnClickListener(this);
        mWifiTv.setOnClickListener(this);
        mSoundTv.setOnClickListener(this);
        mUploadTv.setOnClickListener(this);
    }

    private void initView() {
        tvApDung = findViewById(R.id.apdung);
        tvDoSang = findViewById(R.id.dosang);
        tvRung = findViewById(R.id.rung);
        tvTimeScreen = findViewById(R.id.timeScreen);
        tvBlueTooth = findViewById(R.id.blueTooth);
        tvWifi = findViewById(R.id.wifi);
        tvSound = findViewById(R.id.sound);
        tvUpload = findViewById(R.id.upload);

        mApDungTv = findViewById(R.id.iv_ap_dung);
        mTitleChucNangTv = (TextView) findViewById(R.id.tv_title_chuc_nang);
        mDoSangTv = (TextView) findViewById(R.id.tv_do_sang);
        mRungTv = (TextView) findViewById(R.id.tv_rung);
        mTimeScreenTv = (TextView) findViewById(R.id.tv_time_screen);
        mBlueToothTv = (TextView) findViewById(R.id.tv_blueTooth);
        mWifiTv = (TextView) findViewById(R.id.tv_wifi);
        mSoundTv = (TextView) findViewById(R.id.tv_sound);
        mUploadTv = (TextView) findViewById(R.id.tv_upload);
        mBackChucNangTv = (ImageView) findViewById(R.id.tv_back_chuc_nang);

        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mTitleChucNangTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mDoSangTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mRungTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mTimeScreenTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mBlueToothTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mWifiTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mSoundTv);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mUploadTv);

        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvApDung);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvDoSang);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvRung);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvTimeScreen);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvBlueTooth);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvWifi);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvSound);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, tvUpload);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void readSharePreChucNang() {
        boolean mRung = SharedPreferencesManager.getRung(this);
        boolean BlueTooth = SharedPreferencesManager.getBlueTooth(this);
        boolean Sound = SharedPreferencesManager.getSound(this);
        boolean Upload = SharedPreferencesManager.getUpload(this);

        if (mRung == true) {
            mRungTv.setText(R.string.On);
        } else {
            mRungTv.setText(R.string.OFF);
        }

        if (BlueTooth == true) {
            mBlueToothTv.setText(R.string.On);
        } else {
            mBlueToothTv.setText(R.string.OFF);
        }

        if (Sound == true) {
            mSoundTv.setText(R.string.On);
        } else {
            mSoundTv.setText(R.string.OFF);

        }

        if (Upload == true) {
            mUploadTv.setText(R.string.On);
        } else {
            mUploadTv.setText(R.string.OFF);

        }
        mTitleChucNangTv.setText(SharedPreferencesManager.getTitle_chuc_nang(this));
        int c = SharedPreferencesManager.getData_light(this);
        brightness = c;
        mDoSangTv.setText(c + "%");
        mTimeScreenTv.setText(SharedPreferencesManager.getTurn_off_screen(this));

        boolean s = SharedPreferencesManager.getWifi(this);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
        if (wifiEnabled == false && s == false) {
            wifiManager.setWifiEnabled(true);
            SharedPreferencesManager.setWifi(this, true);
        }
        boolean Wifi = SharedPreferencesManager.getWifi(this);
        if (Wifi == true) {
            mWifiTv.setText(R.string.On);
        } else {
            mWifiTv.setText(R.string.OFF);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back_chuc_nang:
                back();
                onBackPressed();
                break;
            case R.id.tv_do_sang:
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.custom_dialog_light);
                dialog.show();
                mSeekBarChucNang = dialog.findViewById(R.id.seekbar_light);
                mSeekBarChucNang.setMax(255);
                int a = SharedPreferencesManager.getData_light(this);
                int b = (int) (a * 2.55);
                mSeekBarChucNang.setProgress(b);

                onChangeSeekBar();


                break;
            case R.id.tv_rung:
                if (aBoolean_rung == false) {
                    aBoolean_rung = true;
                    mRungTv.setText(R.string.On);
                } else {
                    aBoolean_rung = false;
                    mRungTv.setText(R.string.OFF);
                }
                break;
            case R.id.tv_time_screen:
                Dialog dialog1 = new Dialog(this);
                dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setCancelable(true);
                dialog1.setContentView(R.layout.custom_dialog_time_screen);
                dialog1.show();
                initViewDialog1(dialog1);
                switch (SharedPreferencesManager.getPosion_Time(this)) {
                    case Constants.position_fifteen_Second_dialog:
                        rad_group.check(R.id.rad_15s);
                        break;
                    case Constants.position_Thirty_Second_dialog:
                        rad_group.check(R.id.rad_30s);
                        break;
                    case Constants.position_one_minute_dialog:
                        rad_group.check(R.id.rad_1p);
                        break;
                    case Constants.position_fifteen_minute_dialog:
                        rad_group.check(R.id.rad_15p);
                        break;
                    case Constants.position_thirty_minute_dialog:
                        rad_group.check(R.id.rad_30p);
                        break;
                    case Constants.no_check_position_dialog:
                        rad_group.clearCheck();
                        break;
                }
                onClickRadioButton();
                break;
            case R.id.tv_blueTooth:
                if (aBoolean_Bluetooth == false) {
                    aBoolean_Bluetooth = true;
                    mBlueToothTv.setText(R.string.On);
                    SharedPreferencesManager.setBlueTooth(this, true);
                } else {
                    aBoolean_Bluetooth = false;
                    mBlueToothTv.setText(R.string.OFF);
                    SharedPreferencesManager.setBlueTooth(this, false);
                }

                break;
            case R.id.tv_wifi:
                if (aBoolean_Wifi == false) {
                    aBoolean_Wifi = true;
                    mWifiTv.setText(R.string.OFF);
                    SharedPreferencesManager.setWifi(this, false);
                } else {
                    aBoolean_Wifi = false;
                    mWifiTv.setText(R.string.On);
                    SharedPreferencesManager.setWifi(this, true);
                }
                break;
            case R.id.tv_sound:
                if (aBoolean_sound == false) {
                    aBoolean_sound = true;
                    mSoundTv.setText(R.string.On);
                    SharedPreferencesManager.setSound(this, true);
                } else {
                    aBoolean_sound = false;
                    mSoundTv.setText(R.string.OFF);
                    SharedPreferencesManager.setSound(this, false);
                }
                break;
            case R.id.tv_upload:
                if (aBoolean_upload == false) {
                    aBoolean_upload = true;
                    mUploadTv.setText(R.string.On);
                    SharedPreferencesManager.setUpload(this, true);

                } else {
                    aBoolean_upload = false;
                    mUploadTv.setText(R.string.OFF);
                    SharedPreferencesManager.setUpload(this, false);
                    ContentResolver.setMasterSyncAutomatically(false);

                }
                break;
            case R.id.rad_15s:
                mTimeScreenTv.setText(getString(R.string.fifteenSecond));
                SharedPreferencesManager.setPosion_Time(this, 1);
                break;
            case R.id.rad_30s:
                mTimeScreenTv.setText(getString(R.string.Thirty_Second));
                SharedPreferencesManager.setPosion_Time(this, 2);
                break;
            case R.id.rad_1p:
                mTimeScreenTv.setText(getString(R.string.one_minute));
                SharedPreferencesManager.setPosion_Time(this, 3);
                break;
            case R.id.rad_15p:
                mTimeScreenTv.setText(getString(R.string.fifteen_minute));
                SharedPreferencesManager.setPosion_Time(this, 4);
                break;
            case R.id.rad_30p:
                mTimeScreenTv.setText(getString(R.string.thirty_minute));
                SharedPreferencesManager.setPosion_Time(this, 5);
                break;
            case R.id.iv_ap_dung:
                // createPer();
                Log.e("1122",SharedPreferencesManager.getPosition_Che_Do(this)+"" );
                SharedPreferencesManager.setData_light(ChucNangActivity.this, brightness);
                int k = (int) (brightness * 2.55);
                setScreenBrightness(k);
                apDungTime();
                apDungRung();
                apDungBlueTooth();
                apDungWifi();
                apDungSound();
                apDungUpload();
                saveChedo();
                this.finish();
                break;
            default:
                break;
        }
    }


    public void saveChedo() {
        int dk = SharedPreferencesManager.getPosition_Che_Do(this);
        switch (dk) {
            case Constants.POSITION_SIEU_TOI_UU:
                SharedPreferencesManager.setString_Che_Do(this, getString(R.string.SieuToiUu));
                break;
            case Constants.POSITION_CHUNG:
                SharedPreferencesManager.setString_Che_Do(this, getString(R.string.Chung));
                break;
            case Constants.POSITION_THOI_GIAN_NGU:
                SharedPreferencesManager.setString_Che_Do(this, getString(R.string.ThoiGianNgu));
                break;
            case Constants.POSITION_TUY_CHINH:
                SharedPreferencesManager.setString_Che_Do(this, getString(R.string.TuyChinh));
                break;

        }
    }

    public void apDungRung() {
        boolean e = SharedPreferencesManager.getRung(this);
    }

    public void apDungUpload() {
        boolean d = SharedPreferencesManager.getUpload(this);
        if (d == true) {
            ContentResolver.setMasterSyncAutomatically(true);
        } else {
            ContentResolver.setMasterSyncAutomatically(false);
        }
    }

    public void apDungSound() {
        boolean c = SharedPreferencesManager.getSound(this);
        if (c == true) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }


    }

    public void apDungWifi() {
        boolean b = SharedPreferencesManager.getWifi(this);
        if (b == true) {
            setWifi(true, wifiManager);
        } else {
            setWifi(false, wifiManager);
        }

    }

    public void apDungBlueTooth() {
        boolean a = SharedPreferencesManager.getBlueTooth(this);
        if (a == true) {
            setBluetooth(true);
        } else {
            setBluetooth(false);
        }
    }


    public void apDungTime() {
        int i = SharedPreferencesManager.getPosion_Time(this);
        switch (i) {
            case Constants.position_fifteen_Second_dialog:
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.fifteenSecond));
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Constants.Fifteen_Seconds);
                SharedPreferencesManager.setPosion_Time(this, Constants.position_fifteen_Second_dialog);
                break;
            case Constants.position_Thirty_Second_dialog:
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.Thirty_Second));
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Constants.Thirty_Seconds);
                SharedPreferencesManager.setPosion_Time(this, Constants.position_Thirty_Second_dialog);
                break;
            case Constants.position_one_minute_dialog:
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.one_minute));
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Constants.One_Minute);
                SharedPreferencesManager.setPosion_Time(this, Constants.position_one_minute_dialog);
                break;
            case Constants.position_fifteen_minute_dialog:
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.fifteen_minute));
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Constants.Fifteen_Minutes);
                SharedPreferencesManager.setPosion_Time(this, Constants.position_fifteen_minute_dialog);
                break;
            case Constants.position_thirty_minute_dialog:
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.thirty_minute));
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Constants.Thirty_Minutes);
                SharedPreferencesManager.setPosion_Time(this, Constants.position_thirty_minute_dialog);
                break;
            case Constants.no_check_position_dialog:
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.Five_Second));
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Constants.Five_Seconds);
                SharedPreferencesManager.setPosion_Time(this, Constants.no_check_position_dialog);
                break;
            default:

                break;

        }
    }

    private void onClickRadioButton() {
        rad_15s.setOnClickListener(this);
        rad_30s.setOnClickListener(this);
        rad_1p.setOnClickListener(this);
        rad_15p.setOnClickListener(this);
        rad_30p.setOnClickListener(this);
    }

    private void initViewDialog1(Dialog dialog1) {
        rad_group = dialog1.findViewById(R.id.rd_group);
        rad_15s = dialog1.findViewById(R.id.rad_15s);
        rad_30s = dialog1.findViewById(R.id.rad_30s);
        rad_1p = dialog1.findViewById(R.id.rad_1p);
        rad_15p = dialog1.findViewById(R.id.rad_15p);
        rad_30p = dialog1.findViewById(R.id.rad_30p);

        TextView tv_15_second = dialog1.findViewById(R.id.second_15);
        TextView tv_30_second = dialog1.findViewById(R.id.second_30);
        TextView tv_1_minute = dialog1.findViewById(R.id.minute_1);
        TextView tv_15_minute = dialog1.findViewById(R.id.minute_15);
        TextView tv_30_minute = dialog1.findViewById(R.id.minute_30);

        MainActivity.Font.QUICKSAND_LIGHT.apply(this, tv_15_second);
        MainActivity.Font.QUICKSAND_LIGHT.apply(this, tv_30_second);
        MainActivity.Font.QUICKSAND_LIGHT.apply(this, tv_1_minute);
        MainActivity.Font.QUICKSAND_LIGHT.apply(this, tv_15_minute);
        MainActivity.Font.QUICKSAND_LIGHT.apply(this, tv_30_minute);

    }

    // Get the screen current brightness

    private void onChangeSeekBar() {
        mSeekBarChucNang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                Log.e("1123", "onProgressChanged: ");
                brightness = (int) (i / 2.55);
                mDoSangTv.setText(brightness + "%");
                setScreenBrightness(i);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected int getScreenBrightness() {

        int brightnessValue = Settings.System.getInt(
                mContext.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                0
        );
        return brightnessValue;
    }

    public void setScreenBrightness(int brightnessValue) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
            WindowManager.LayoutParams layoutpars = window.getAttributes();
            layoutpars.screenBrightness = brightness / (float) 255;
            window.setAttributes(layoutpars);
        } else {
            if (brightnessValue >= 0 && brightnessValue <= 255) {
                Settings.System.putInt(
                        mContext.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, brightnessValue
                );
            }
        }

    }

    public void back() {
        int defaultTurnOffTime = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 60000);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, defaultTurnOffTime);
    }


}
