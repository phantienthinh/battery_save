package com.battery.saver.batterysaver.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.ulits.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ClearDoneActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitleDoneTv;
    private ImageView mBackDoneIv;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_done);
        initView();
        createAds();

    }

    private void createAds() {

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initView() {
        mTitleDoneTv = (TextView) findViewById(R.id.tv_title_done);
        mBackDoneIv = (ImageView) findViewById(R.id.iv_back_done);
        mBackDoneIv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_done:
                Intent intent  = new Intent(this,MainActivity.class);
                intent.putExtra(Constants.KEY_SHARE_INTENT,true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
