package com.battery.saver.batterysaver.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.models.SharedPreferencesManager;
import com.battery.saver.batterysaver.ulits.Constants;

public class TietKiemPinMotCham extends Activity implements View.OnClickListener {
    private Dialog dlTietKiemPinMotCham;
    private ImageView mApDungIv;
    private RelativeLayout mSieuToiUuRl;
    private LinearLayout mSieuToiUuLl;
    private ImageView mApDungChungIv;
    private RelativeLayout mChungRl;
    private LinearLayout mChungLl;
    private ImageView mApDungThoiGianNguIv;
    private RelativeLayout mThoiGianNguRl;
    private LinearLayout mThoiGianNguLl;
    private ImageView mApDungTuyChinhIv;
    private RelativeLayout mTuyChinhRl;
    private LinearLayout mTuyChinhLl;
    private ImageView ivClose;
    private WifiManager wifiManager;
    private AudioManager audioManager;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dlTietKiemPinMotCham = new Dialog(this);
        dlTietKiemPinMotCham.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlTietKiemPinMotCham.setCancelable(false);
        dlTietKiemPinMotCham.setContentView(R.layout.custom_dialog_tiet_kiem_pin);
        dlTietKiemPinMotCham.show();
        initView(dlTietKiemPinMotCham);

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        loadSharePre();
    }

    private void loadSharePre() {
        int k = SharedPreferencesManager.getPosition_Che_Do(this);
        switch (k) {
            case Constants.POSITION_SIEU_TOI_UU:
                mSieuToiUuLl.setBackgroundResource(R.color.color_backGround_dialog_chon);
                mSieuToiUuRl.setVisibility(View.VISIBLE);
                break;
            case Constants.POSITION_CHUNG:
                mChungLl.setBackgroundResource(R.color.color_backGround_dialog_chon);
                mChungRl.setVisibility(View.VISIBLE);
                break;
            case Constants.POSITION_THOI_GIAN_NGU:
                mThoiGianNguLl.setBackgroundResource(R.color.color_backGround_dialog_chon);
                mThoiGianNguRl.setVisibility(View.VISIBLE);
                break;
            case Constants.POSITION_TUY_CHINH:
                mTuyChinhLl.setBackgroundResource(R.color.color_backGround_dialog_chon);
                mTuyChinhRl.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initView(Dialog dialog) {
        mApDungIv = dialog.findViewById(R.id.iv_ap_dung);
        mSieuToiUuRl = dialog.findViewById(R.id.Rl_sieu_toi_uu);
        mSieuToiUuRl.setOnClickListener(this);
        mSieuToiUuLl = dialog.findViewById(R.id.ll_sieu_toi_uu);
        mSieuToiUuLl.setOnClickListener(this);
        mApDungChungIv = dialog.findViewById(R.id.iv_ap_dung_chung);
        mChungRl = dialog.findViewById(R.id.Rl_chung);
        mChungRl.setOnClickListener(this);
        mChungLl = dialog.findViewById(R.id.ll_chung);
        mChungLl.setOnClickListener(this);
        mApDungThoiGianNguIv = dialog.findViewById(R.id.iv_ap_dung_thoi_gian_ngu);
        mThoiGianNguRl = dialog.findViewById(R.id.Rl_thoi_gian_ngu);
        mThoiGianNguRl.setOnClickListener(this);
        mThoiGianNguLl = dialog.findViewById(R.id.ll_thoi_gian_ngu);
        mThoiGianNguLl.setOnClickListener(this);
        mApDungTuyChinhIv = dialog.findViewById(R.id.iv_ap_dung_tuy_chinh);
        mTuyChinhRl = dialog.findViewById(R.id.Rl_tuy_chinh);
        mTuyChinhRl.setOnClickListener(this);
        mTuyChinhLl = dialog.findViewById(R.id.ll_tuy_chinh);
        mTuyChinhLl.setOnClickListener(this);
        ivClose = dialog.findViewById(R.id.iv_close_dialog_TKP);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sieu_toi_uu:
                setView(Constants.VISIBLE, Constants.GONE, Constants.GONE, Constants.GONE);
                setBackGgroundView(R.color.color_backGround_dialog_chon, R.color.color_backGround_dialog_kchon,
                        R.color.color_backGround_dialog_kchon, R.color.color_backGround_dialog_kchon);
                break;
            case R.id.ll_chung:
                setView(Constants.GONE, Constants.VISIBLE, Constants.GONE, Constants.GONE);
                setBackGgroundView(R.color.color_backGround_dialog_kchon, R.color.color_backGround_dialog_chon,
                        R.color.color_backGround_dialog_kchon, R.color.color_backGround_dialog_kchon);
                break;
            case R.id.ll_thoi_gian_ngu:
                setView(Constants.GONE, Constants.GONE, Constants.VISIBLE, Constants.GONE);
                setBackGgroundView(R.color.color_backGround_dialog_kchon, R.color.color_backGround_dialog_kchon,
                        R.color.color_backGround_dialog_chon, R.color.color_backGround_dialog_kchon);
                break;
            case R.id.ll_tuy_chinh:
                setView(Constants.GONE, Constants.GONE, Constants.GONE, Constants.VISIBLE);
                setBackGgroundView(R.color.color_backGround_dialog_kchon, R.color.color_backGround_dialog_kchon,
                        R.color.color_backGround_dialog_kchon, R.color.color_backGround_dialog_chon);
                break;
            case R.id.Rl_sieu_toi_uu:
                SharedPreferencesManager.setPosition_Che_Do(this, 1);
                SharedPreferencesManager.setData_light(this, 40);
                SharedPreferencesManager.setRung(this, false);
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.Five_Second));
                SharedPreferencesManager.setPosion_Time(this, 6);
                SharedPreferencesManager.setBlueTooth(this, false);
                SharedPreferencesManager.setWifi(this, true);
                SharedPreferencesManager.setSound(this, false);
                SharedPreferencesManager.setUpload(this, false);
                clickApdungDialog(40);
                break;
            case R.id.Rl_chung:
                SharedPreferencesManager.setPosition_Che_Do(this, 2);
                SharedPreferencesManager.setData_light(this, 60);
                SharedPreferencesManager.setRung(this, true);
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.fifteenSecond));
                SharedPreferencesManager.setPosion_Time(this, 1);
                SharedPreferencesManager.setBlueTooth(this, false);
                SharedPreferencesManager.setWifi(this, true);
                SharedPreferencesManager.setSound(this, false);
                SharedPreferencesManager.setUpload(this, false);
                clickApdungDialog(60);
                break;
            case R.id.Rl_thoi_gian_ngu:
                SharedPreferencesManager.setPosition_Che_Do(this, 3);
                SharedPreferencesManager.setData_light(this, 10);
                SharedPreferencesManager.setRung(this, false);
                SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.Five_Second));
                SharedPreferencesManager.setPosion_Time(this, 6);
                SharedPreferencesManager.setBlueTooth(this, false);
                SharedPreferencesManager.setWifi(this, true);
                SharedPreferencesManager.setSound(this, false);
                SharedPreferencesManager.setUpload(this, false);
                clickApdungDialog(10);
                break;
            case R.id.Rl_tuy_chinh:
                SharedPreferencesManager.setPosition_Che_Do(this, 4);
                SharedPreferencesManager.setString_Che_Do(this, getString(R.string.TuyChinh));
                Intent intent3 = new Intent(this, ChucNangActivity.class);
                startActivity(intent3);
                this.finish();
                break;
            case R.id.iv_close_dialog_TKP:
                dlTietKiemPinMotCham.cancel();
                TietKiemPinMotCham.this.finish();
                break;
            default:
                break;
        }
    }

    private void setBackGgroundView(int view1, int view2, int view3, int view4) {
        mSieuToiUuLl.setBackgroundResource(view1);
        mChungLl.setBackgroundResource(view2);
        mThoiGianNguLl.setBackgroundResource(view3);
        mTuyChinhLl.setBackgroundResource(view4);
    }

    public void setView(int view1, int view2, int view3, int view4) {
        mSieuToiUuRl.setVisibility(view1);
        mChungRl.setVisibility(view2);
        mThoiGianNguRl.setVisibility(view3);
        mTuyChinhRl.setVisibility(view4);
    }

    public void clickApdungDialog(int light) {
        SharedPreferencesManager.setData_light(this, light);
        int k = (int) (light * 2.55);
        setScreenBrightness(k);
        apDungTime();
        apDungRung();
        apDungBlueTooth();
        apDungWifi();
        apDungSound();
        apDungUpload();
        saveChedo();
        this.finish();
    }

    public void setScreenBrightness(int brightnessValue) {

        // Make sure brightness value between 0 to 255
        if (brightnessValue >= 0 && brightnessValue <= 255) {
            Settings.System.putInt(
                    this.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
            );
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
}
