package com.battery.saver.batterysaver.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.models.SharedPreferencesManager;
import com.battery.saver.batterysaver.ulits.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class CheDoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_chedo_back;
    private boolean showdialog = false;
    private LinearLayout llToiUu, llChung, llTimeSleep, llTuyChinh;
    private RadioButton rdToiUu, rdChung, rdTimeSleep, rdTuyChinh;
    private boolean mCheckSieuToiUu, mCheckChung, mCheckGioDiNgu, mCheckTuyChinh;
    private int position = 0;
    private TextView mCheDoTitle;
    private TextView mSieuToiUu;
    private TextView mNoteSieuToiUu;
    private TextView mChung;
    private TextView mNoteChung;
    private TextView mTimeSleep;
    private TextView mNoteTimeSleep;
    private TextView mCustom;
    private TextView mNoteCustom;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_che_do);
        initView();
        onClick();
        readSharePreCheDo();
        createAds();
        Intent intent = getIntent();
        showdialog = intent.getBooleanExtra("KEY_SHOW", false);
        if (showdialog) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!SharedPreferencesManager.getShowDialogCheDo(this)) {
                    showdialog();
                }
            }
        }
    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheDoActivity.this);
        builder.setMessage(getString(R.string.can_cap_quyen))
                .setTitle(getString(R.string.cap_quyen_cho_ung_dung))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(
                                android.provider.Settings
                                        .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);
                        SharedPreferencesManager.setShowDialogCheDo(CheDoActivity.this, true);
                    }
                }).show();
    }

    private void createAds() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void onClick() {

        iv_chedo_back.setOnClickListener(this);
        rdToiUu.setOnClickListener(this);
        rdChung.setOnClickListener(this);
        rdTimeSleep.setOnClickListener(this);
        rdTuyChinh.setOnClickListener(this);

        llToiUu.setOnClickListener(this);
        llChung.setOnClickListener(this);
        llTimeSleep.setOnClickListener(this);
        llTuyChinh.setOnClickListener(this);

    }


    private void initView() {
        iv_chedo_back = findViewById(R.id.iv_chedo_back);
        llToiUu = findViewById(R.id.ll_toi_uu_che_do);
        llChung = findViewById(R.id.ll_chung_che_do);
        llTimeSleep = findViewById(R.id.ll_Time_sleep_che_do);
        llTuyChinh = findViewById(R.id.ll_tuy_chinh_che_do);

        rdToiUu = findViewById(R.id.rd_sieu_toi_uu);
        rdChung = findViewById(R.id.rd_chung);
        rdTimeSleep = findViewById(R.id.rd_time_sleep);
        rdTuyChinh = findViewById(R.id.rd_tuy_chinh);

        mCheDoTitle = findViewById(R.id.title_cheDo);
        mSieuToiUu = findViewById(R.id.sieuToiUu);
        mNoteSieuToiUu = findViewById(R.id.sieuToiUu_note);
        mChung = findViewById(R.id.Chung);
        mNoteChung = findViewById(R.id.Chung_Note);
        mTimeSleep = findViewById(R.id.TimeSleep);
        mNoteTimeSleep = findViewById(R.id.TimeSleep_note);
        mCustom = findViewById(R.id.Custom);
        mNoteCustom = findViewById(R.id.Custom_Note);

        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mCheDoTitle);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mSieuToiUu);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mChung);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mTimeSleep);
        MainActivity.Font.QUICKSAND_MEDIUM.apply(this, mCustom);

        MainActivity.Font.QUICKSAND_LIGHT.apply(this, mNoteSieuToiUu);
        MainActivity.Font.QUICKSAND_LIGHT.apply(this, mNoteChung);
        MainActivity.Font.QUICKSAND_LIGHT.apply(this, mNoteTimeSleep);
        MainActivity.Font.QUICKSAND_LIGHT.apply(this, mNoteCustom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chedo_back:
                onBackPressed();
                break;
            case R.id.ll_toi_uu_che_do:
                rdToiUuCheDo();
                break;
            case R.id.ll_chung_che_do:
               rdChung();
                break;
            case R.id.ll_Time_sleep_che_do:
                rdTimeSleep();
                break;
            case R.id.ll_tuy_chinh_che_do:
                rdTuyChinh();
                break;
            case R.id.rd_sieu_toi_uu:
                rdToiUuCheDo();
                break;
            case R.id.rd_chung:
                rdChung();
                break;
            case R.id.rd_time_sleep:
                rdTimeSleep();
                break;
            case R.id.rd_tuy_chinh:
                rdTuyChinh();
                break;


        }

    }
    private void rdTuyChinh(){
        if (mCheckTuyChinh == false) {
            mCheckSieuToiUu = false;
            mCheckChung = false;
            mCheckGioDiNgu = false;
            mCheckTuyChinh = true;
            position = Constants.POSITION_TUY_CHINH;
            rdToiUu.setChecked(false);
            rdChung.setChecked(false);
            rdTimeSleep.setChecked(false);
            rdTuyChinh.setChecked(true);

            llToiUu.setBackgroundResource(R.drawable.custom_layout);
            llChung.setBackgroundResource(R.drawable.custom_layout);
            llTimeSleep.setBackgroundResource(R.drawable.custom_layout);
            llTuyChinh.setBackgroundResource(R.drawable.custom_layout_click);

            SharedPreferencesManager.setPosition_Che_Do(CheDoActivity.this, position);
        }

        SharedPreferencesManager.setTitle_chuc_nang(this, getString(R.string.TuyChinh));
        Intent intent3 = new Intent(CheDoActivity.this, ChucNangActivity.class);
        startActivity(intent3);
    }
    private void rdTimeSleep(){
        if (mCheckGioDiNgu == false) {
            mCheckSieuToiUu = false;
            mCheckChung = false;
            mCheckGioDiNgu = true;
            mCheckTuyChinh = false;
            position = Constants.POSITION_THOI_GIAN_NGU;
            rdToiUu.setChecked(false);
            rdChung.setChecked(false);
            rdTimeSleep.setChecked(true);
            rdTuyChinh.setChecked(false);

            llToiUu.setBackgroundResource(R.drawable.custom_layout);
            llChung.setBackgroundResource(R.drawable.custom_layout);
            llTimeSleep.setBackgroundResource(R.drawable.custom_layout_click);
            llTuyChinh.setBackgroundResource(R.drawable.custom_layout);
        }
        SharedPreferencesManager.setTitle_chuc_nang(this, getString(R.string.ThoiGianNgu));
        SharedPreferencesManager.setPosition_Che_Do(CheDoActivity.this, position);

        SharedPreferencesManager.setData_light(this, 10);
        SharedPreferencesManager.setRung(this, false);
        SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.Five_Second));
        SharedPreferencesManager.setPosion_Time(this, 6);
        SharedPreferencesManager.setBlueTooth(this, false);
        SharedPreferencesManager.setWifi(this, true);
        SharedPreferencesManager.setSound(this, false);
        SharedPreferencesManager.setUpload(this, false);
        Intent intent2 = new Intent(CheDoActivity.this, ChucNangActivity.class);
        startActivity(intent2);
    }
    private void rdChung(){
        if (mCheckChung == false) {
            mCheckSieuToiUu = false;
            mCheckChung = true;
            mCheckGioDiNgu = false;
            mCheckTuyChinh = false;
            position = Constants.POSITION_CHUNG;
            rdToiUu.setChecked(false);
            rdChung.setChecked(true);
            rdTimeSleep.setChecked(false);
            rdTuyChinh.setChecked(false);

            llToiUu.setBackgroundResource(R.drawable.custom_layout);
            llChung.setBackgroundResource(R.drawable.custom_layout_click);
            llTimeSleep.setBackgroundResource(R.drawable.custom_layout);
            llTuyChinh.setBackgroundResource(R.drawable.custom_layout);

        }
        SharedPreferencesManager.setTitle_chuc_nang(this, getString(R.string.Chung));
        SharedPreferencesManager.setPosition_Che_Do(CheDoActivity.this, position);
        SharedPreferencesManager.setData_light(this, 60);
        SharedPreferencesManager.setRung(this, true);
        SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.fifteenSecond));
        SharedPreferencesManager.setPosion_Time(this, 1);
        SharedPreferencesManager.setBlueTooth(this, false);
        SharedPreferencesManager.setWifi(this, true);
        SharedPreferencesManager.setSound(this, false);
        SharedPreferencesManager.setUpload(this, false);
        Intent intent1 = new Intent(CheDoActivity.this, ChucNangActivity.class);
        startActivity(intent1);
    }
    private void rdToiUuCheDo(){
        if (mCheckSieuToiUu == false) {
            mCheckSieuToiUu = true;
            mCheckChung = false;
            mCheckGioDiNgu = false;
            mCheckTuyChinh = false;
            position = Constants.POSITION_SIEU_TOI_UU;
            rdToiUu.setChecked(true);
            rdChung.setChecked(false);
            rdTimeSleep.setChecked(false);
            rdTuyChinh.setChecked(false);

            llToiUu.setBackgroundResource(R.drawable.custom_layout_click);
            llChung.setBackgroundResource(R.drawable.custom_layout);
            llTimeSleep.setBackgroundResource(R.drawable.custom_layout);
            llTuyChinh.setBackgroundResource(R.drawable.custom_layout);

        }
        SharedPreferencesManager.setTitle_chuc_nang(this, getString(R.string.SieuToiUu));
        SharedPreferencesManager.setPosition_Che_Do(CheDoActivity.this, position);
        SharedPreferencesManager.setData_light(this, 40);
        SharedPreferencesManager.setRung(this, false);
        SharedPreferencesManager.setTurn_off_screen(this, getString(R.string.Five_Second));
        SharedPreferencesManager.setPosion_Time(this, 6);
        SharedPreferencesManager.setBlueTooth(this, false);
        SharedPreferencesManager.setWifi(this, true);
        SharedPreferencesManager.setSound(this, false);
        SharedPreferencesManager.setUpload(this, false);
        Intent intent = new Intent(CheDoActivity.this, ChucNangActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void readSharePreCheDo() {
        int pos = SharedPreferencesManager.getPosition_Che_Do(this);
        switch (pos) {
            case 1:
                mCheckSieuToiUu = true;
                rdToiUu.setChecked(true);
                llToiUu.setBackgroundResource(R.drawable.custom_layout_click);
                break;
            case 2:
                mCheckChung = true;
                rdChung.setChecked(true);
                llChung.setBackgroundResource(R.drawable.custom_layout_click);
                break;
            case 3:
                mCheckGioDiNgu = true;
                rdTimeSleep.setChecked(true);
                llTimeSleep.setBackgroundResource(R.drawable.custom_layout_click);
                break;
            case 4:
                mCheckTuyChinh = true;
                rdTuyChinh.setChecked(true);
                llTuyChinh.setBackgroundResource(R.drawable.custom_layout_click);
                break;
        }
    }

}
