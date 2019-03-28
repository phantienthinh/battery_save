package com.battery.saver.batterysaver.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.models.SharedPreferencesManager;
import com.battery.saver.batterysaver.services.CleanerService;
import com.battery.saver.batterysaver.services.CoreService;
import com.battery.saver.batterysaver.services.Service;
import com.battery.saver.batterysaver.ulits.Constants;
import com.battery.saver.batterysaver.widgets.CustomTypefaceSpan;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final String TAG = "debug";
    public static int POSITION_INTENT = 0;
    private Dialog DialogInfo, DialogClearDone;
    private Button btnDialogOk, btnSetting, btnCloseClear;
    private ImageView ivShowNav, ivRunning, ivTick;
    private TextView tvPin, tvTime, tvNhietDo, tvDienap, tvCheDo, tvCheDo1, tvPinTypeface, tvNhietDoTypeface, tvDienapTypeface, tvTimeTypeface, tvTitle;
    private TextView tvTitleDialog, tvAuthor, tvHdp, tvPricacy, tvLink, tvEmail, tvLinkEmail, tvTitleClear, tvContentClear;
    private DrawerLayout drawer;
    private CircularProgressIndicator ProgressPin, progressTime, progressNhietDo, progressDienap;
    private Context context;
    private LinearLayout llRegime;
    private Intent intent;
    private ImageView mMainDoneIv;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    //  private Booster booster;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);

            float batteryPct = level / (float) scale;
            int a = (int) (batteryPct * 100);
            ProgressPin.setCurrentProgress(a);
            tvPin.setText(a + "%");

            int second = a * 432;
            int hour = second / 3600;
            int minute = (second % 3600) / 60;

            tvTime.setText(hour + "h" + minute + "m");
            SharedPreferencesManager.setTime(arg0, tvTime.getText().toString().trim());
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        }
    };


    //lấy về giá trị nhiệt đọ pin
    public static int batteryTemperature(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        float temp = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
        return (int) temp;
    }

    public static String getApplicationLabel(Context context, String packageName) {

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        String label = null;

        for (int i = 0; i < packages.size(); i++) {

            ApplicationInfo temp = packages.get(i);

            if (temp.packageName.equals(packageName))
                label = packageManager.getApplicationLabel(temp).toString();
        }

        return label;
    }

    public static final void ratingApp(Context context) {
        Uri uri = Uri.parse(Constants.PLAY_MARKET + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.PLAYSTORE_LINK + context.getPackageName())));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        eventClick();
        setDataCircul();
        context = this;
        createNavigationView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!SharedPreferencesManager.getShowDialogMain(this)) {
                createDialog();
            }
        }
        // checkSystemWritePermission();
        this.registerReceiver(this.mBatInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        intent = this.getIntent();
        Boolean aBoolean = intent.getBooleanExtra(Constants.KEY_SHARE_INTENT, false);
        if (aBoolean) {
            ivRunning.setVisibility(View.GONE);
            mMainDoneIv.setVisibility(View.VISIBLE);
            ivTick.setVisibility(View.VISIBLE);
        }
        createAds();

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            if (!SharedPreferencesManager.getOne(this)) {
                SharedPreferencesManager.setOne(this,true);
                createShortCut_tiet_kiem_pin();
                createShortCut_Toi_Uu();
                runService();
                startService(new Intent(this, CoreService.class));
                startService(new Intent(this, CleanerService.class));
            }
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.quyen_noi_dung))
                .setTitle(getString(R.string.cap_quyen_cho_ung_dung))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkSystemWritePermission();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void checkappADL() {
        String topPackageName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            @SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);

                }
                if (!mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    Log.e(TAG, topPackageName);
                    Toast.makeText(context, "vao", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void createAds() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("D6A8A5C1E97B95861A9A098D58D1D2CC")
                .build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                switch (POSITION_INTENT) {
                    case 1:
                        //checkSystemWritePermission();
                        Intent intent1 = new Intent(MainActivity.this, CheDoActivity.class);
                        intent1.putExtra("KEY_SHOW",true);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, TuyChonNangLuong.class));
                        break;
                }
                loadInterstitialAd();
            }
        });

        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        if (mInterstitialAd != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            mInterstitialAd.loadAd(adRequest);
        }

    }

    private void createNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setDataCircul() {

        //setdata %
        ProgressPin.setMaxProgress(100);
        int a = (int) getBatteryLevel();
        // ProgressPin.setProgressTextAdapter(new PatternProgressTextAdapter(a+"\u0055"));
        ProgressPin.setStartAngle(270);
        ProgressPin.setCurrentProgress(a);
        tvPin.setText(a + "%");

        //set data nhiệt độ
        progressNhietDo.setAnimationEnabled(false);
        progressNhietDo.setStartAngle(0);
        progressNhietDo.setProgress(360, 360);
        String s = batteryTemperature(this) + "ᵒC";
        tvNhietDo.setText(s);
        SharedPreferencesManager.setNhietDo(this, s);

        //set data điện áp

        progressDienap.setAnimationEnabled(false);
        //progressDienap.setMaxProgress(100);
        progressDienap.setStartAngle(130);
        progressDienap.setProgress(270, 360);
        tvDienap.setText(getVoltage());

        int second = a * 432;
        int hour = second / 3600;
        int minute = (second % 3600) / 60;

        tvTime.setText(hour + "h" + minute + "m");
        SharedPreferencesManager.setTime(this, tvTime.getText().toString().trim());


    }

    private void eventClick() {
        llRegime.setOnClickListener(this);
        ivShowNav.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        ivRunning.setOnClickListener(this);
        mMainDoneIv.setOnClickListener(this);
    }

    private void initView() {
        ivTick = findViewById(R.id.iv_tick_da_toi_uu);
        tvTitle = findViewById(R.id.title);
        tvDienapTypeface = findViewById(R.id.dienap_typeface);
        tvNhietDoTypeface = findViewById(R.id.nhietDo_typeface);
        tvPinTypeface = findViewById(R.id.pin_typeface);
        tvTimeTypeface = findViewById(R.id.time_typeface);

        tvCheDo1 = findViewById(R.id.tv_che_do_1);
        ivRunning = findViewById(R.id.iv_running);
        tvCheDo = findViewById(R.id.tv_che_do);
        tvTime = findViewById(R.id.tv_time);
        tvNhietDo = findViewById(R.id.tv_nhietDo);
        tvDienap = findViewById(R.id.tv_dienap);
        tvPin = findViewById(R.id.tv_pin);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivShowNav = findViewById(R.id.iv_show_nav);
        ProgressPin = findViewById(R.id.circular_progress_Pin);
        progressTime = findViewById(R.id.circular_progress_Time);
        progressDienap = findViewById(R.id.circular_progress_dien_ap);
        progressNhietDo = findViewById(R.id.circular_progress_nhietdo);
        llRegime = findViewById(R.id.ll_regime);
        btnSetting = findViewById(R.id.btn_setting);


        Font.QUICKSAND_BOLD.apply(this, tvCheDo);
        Font.QUICKSAND_BOLD.apply(this, tvCheDo1);
        Font.QUICKSAND_LIGHT.apply(this, tvTime);
        Font.QUICKSAND_LIGHT.apply(this, tvNhietDo);
        Font.QUICKSAND_LIGHT.apply(this, tvDienap);
        Font.QUICKSAND_LIGHT.apply(this, tvPin);

        Font.QUICKSAND_MEDIUM.apply(this, tvDienapTypeface);
        Font.QUICKSAND_MEDIUM.apply(this, tvNhietDoTypeface);
        Font.QUICKSAND_MEDIUM.apply(this, tvTimeTypeface);
        Font.QUICKSAND_MEDIUM.apply(this, tvPinTypeface);
        Font.QUICKSAND_MEDIUM.apply(this, tvPinTypeface);
        Font.QUICKSAND_MEDIUM.apply(this, btnSetting);
        Font.QUICKSAND_MEDIUM.apply(this, tvTitle);
        mMainDoneIv = (ImageView) findViewById(R.id.iv_main_done);
        mMainDoneIv.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("WrongConstant")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            String linkApp = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent intent3 = new Intent("android.intent.action.SEND");
            intent3.setType("text/plain");
            intent3.addFlags(52428);
            intent3.putExtra("android.intent.extra.SUBJECT", getString(R.string.new_app_name));
            intent3.putExtra("android.intent.extra.TEXT", linkApp);
            MainActivity.this.startActivity(Intent.createChooser(intent3, getResources().getString(R.string.Share)));
            //MainActivity.this.drawerLayout.closeDrawers();d
            drawer.closeDrawer(GravityCompat.START);
            // Handle the camera action
        } else if (id == R.id.nav_info) {

            DialogInfo = new Dialog(MainActivity.this);
            DialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogInfo.setContentView(R.layout.custom_dialog_info);
            DialogInfo.setCancelable(true);
            DialogInfo.show();
            initViewDialog();
            btnDialogOk.setOnClickListener(this);

        } else if (id == R.id.nav_slideshow) {
            ratingApp(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViewDialog() {
        tvTitleDialog = DialogInfo.findViewById(R.id.title_dialog);
        tvAuthor = DialogInfo.findViewById(R.id.author_dialog);
        tvHdp = DialogInfo.findViewById(R.id.hdp_dialog);
        tvPricacy = DialogInfo.findViewById(R.id.pricacy_dialog);
        tvLink = DialogInfo.findViewById(R.id.link_dialog);
        tvLink.setOnClickListener(this);
        tvEmail = DialogInfo.findViewById(R.id.emali_dialog);
        tvLinkEmail = DialogInfo.findViewById(R.id.mail_link_dialog);
        tvLinkEmail.setOnClickListener(this);
        btnDialogOk = DialogInfo.findViewById(R.id.btn_dialog_ok);

        Font.QUICKSAND_MEDIUM.apply(this, tvTitleDialog);
        Font.QUICKSAND_MEDIUM.apply(this, tvAuthor);
        Font.QUICKSAND_MEDIUM.apply(this, tvHdp);
        Font.QUICKSAND_MEDIUM.apply(this, tvPricacy);
        Font.QUICKSAND_MEDIUM.apply(this, tvLink);
        Font.QUICKSAND_MEDIUM.apply(this, tvEmail);
        Font.QUICKSAND_MEDIUM.apply(this, tvLinkEmail);
        Font.QUICKSAND_MEDIUM.apply(this, btnDialogOk);

        tvLink.setPaintFlags(tvLink.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvLinkEmail.setPaintFlags(tvEmail.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

    }

    // lấy về giá trị phần trăm pin hiện tại
    public float getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

    //lấy về điện áp của pin
    public String getVoltage() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent b = this.registerReceiver(null, ifilter);
        String a = String.valueOf(b.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0));
        String a1 = a.substring(1, 3);
        String a2 = a.substring(0, 1);
        String a3 = a2 + "," + a1 + "V";
        return a3;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_dialog_ok:
                DialogInfo.dismiss();
                break;
            case R.id.iv_show_nav:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_regime:
                POSITION_INTENT = 1;
                Intent intent1 = new Intent(MainActivity.this, CheDoActivity.class);
                intent1.putExtra("KEY_SHOW",true);
                showInterstitial(intent1);
//                NotificationManager notificationManager =
//                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                        && !notificationManager.isNotificationPolicyAccessGranted()) {
//                    showdialog();
//                }
                break;
            case R.id.btn_setting:
                POSITION_INTENT = 2;
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                showInterstitial(intent);
                break;
            case R.id.iv_running:
                POSITION_INTENT = 3;
                Intent intent2 = new Intent(MainActivity.this, TuyChonNangLuong.class);
                showInterstitial(intent2);
                break;
            case R.id.iv_main_done:
                DialogClearDone = new Dialog(this);
                DialogClearDone.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogClearDone.setCancelable(true);
                DialogClearDone.setContentView(R.layout.custom_dialog_clear_done);
                DialogClearDone.show();
                initViewDialogClear();
                break;
            case R.id.btnCloseDialog:
                DialogClearDone.cancel();
                break;
            case R.id.mail_link_dialog:
                sendEmail(this);
                break;
            case  R.id.link_dialog:
                Uri uri = Uri.parse(getResources().getString(R.string.link_bao_mat));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    context.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getResources().getString(R.string.link_bao_mat))));
                }
                break;
        }
    }
    public static final void sendEmail(Context context){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{context.getString(R.string.email_hdp)});
        i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.send_email));
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.send_email )));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, context.getString(R.string.no_client_email), Toast.LENGTH_SHORT).show();
        }
    }
    private void initViewDialogClear() {
        tvContentClear = DialogClearDone.findViewById(R.id.tvNoiDungDialog);
        tvTitleClear = DialogClearDone.findViewById(R.id.tvTitleClear);
        btnCloseClear = DialogClearDone.findViewById(R.id.btnCloseDialog);
        btnCloseClear.setOnClickListener(this);

        Font.QUICKSAND_LIGHT.apply(this, tvContentClear);
        Font.QUICKSAND_MEDIUM.apply(this, tvTitleClear);
        Font.QUICKSAND_MEDIUM.apply(this, btnCloseClear);

    }

    private void createPer() {
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readSharePreMain();
    }

    private void runService() {
        Intent intent = new Intent(this, Service.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
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
            Intent shortcutintentToiUu = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
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
            Intent shortcutintentTKP = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            Intent demo = new Intent(this, TietKiemPinMotCham.class);
            shortcutintentTKP.putExtra("duplicate", false);
            shortcutintentTKP.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.che_do_TKP));
            Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.chedo_tietkiempin);
            shortcutintentTKP.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            shortcutintentTKP.putExtra(Intent.EXTRA_SHORTCUT_INTENT, demo);
            sendBroadcast(shortcutintentTKP);
        }
    }

    private void readSharePreMain() {
        String a = SharedPreferencesManager.getString_Che_Do(this);
        tvCheDo.setText(a);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Quicksand-Medium.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.mBatInfoReceiver);

    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context)) {

                return true;
            } else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
//            context.sta(intent);
            startActivityForResult(intent, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (Settings.System.canWrite(this)) {
                SharedPreferencesManager.setShowDialogMain(this, true);
                createShortCut_tiet_kiem_pin();
                createShortCut_Toi_Uu();
                runService();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.can_cap_quyen))
                        .setTitle(getString(R.string.cap_quyen_cho_ung_dung))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        }).show();
//                AlertDialog alert = builder.create();
////                alert.show();
            }
        }
    }

    private void showInterstitial(Intent intent) {
        long a = SharedPreferencesManager.getTimeAds(this);
        long calenda = System.currentTimeMillis();
        if (a == 0) {
            mInterstitialAd.show();
            SharedPreferencesManager.setTimeAds(this, calenda + Constants.THREE_MINUTE);
        } else if (a < calenda) {
            mInterstitialAd.show();
            SharedPreferencesManager.setTimeAds(this, calenda + Constants.THREE_MINUTE);
        } else if (a > calenda) {
            startActivity(intent);
        }
    }


    public static class Font {
        public static final Font QUICKSAND_BOLD = new Font("Quicksand-Bold.ttf");
        public static final Font QUICKSAND_LIGHT = new Font("Quicksand-Light.ttf");
        public static final Font QUICKSAND_MEDIUM = new Font("Quicksand-Medium.ttf");
        public static final Font QUICKSAND_REGULAR = new Font("Quicksand-Regular.ttf");
        private final String assetName;
        private volatile Typeface typeface;

        private Font(String assetName) {
            this.assetName = assetName;
        }

        public void apply(Context context, TextView textView) {
            if (typeface == null) {
                synchronized (this) {
                    if (typeface == null) {
                        typeface = Typeface.createFromAsset(context.getAssets(), assetName);
                    }
                }
            }
            textView.setTypeface(typeface);
        }
    }
}
