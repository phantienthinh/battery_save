package com.battery.saver.batterysaver.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {

    public static void setPosition_Che_Do(Context context, int is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("KEY_Position_Che_Do", is).apply();
    }

    public static int getPosition_Che_Do(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("KEY_Position_Che_Do",1);
    }

    public static void setString_Che_Do(Context context, String is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("KEY_String_Che_Do", is).apply();
    }

    public static String getString_Che_Do(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("KEY_String_Che_Do","SIÊU TỐI ƯU");
    }

    public static void setTitle_chuc_nang(Context context, String is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("KEY_Title_chuc_nang", is).apply();
    }

    public static String getTitle_chuc_nang(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("KEY_Title_chuc_nang","");
    }

    //ánh sáng
    public static void setData_light(Context context, int is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("KEY_Data_light", is).apply();
    }

    public static int getData_light(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("KEY_Data_light",40);
    }

    public static void setRung(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Rung", is).apply();
    }

    public static boolean getRung(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Rung",false);
    }

    public static void setTurn_off_screen(Context context, String is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("KEY_Turn_off_screen", is).apply();
    }

    public static String getTurn_off_screen(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("KEY_Turn_off_screen","15 giây");
    }

    public static void setBlueTooth(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_BlueTooth", is).apply();
    }

    public static boolean getBlueTooth(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_BlueTooth",false);
    }

    public static void setWifi(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Wifi", is).apply();
    }

    public static boolean getWifi(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Wifi",true);
    }


    public static void setSound(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Sound", is).apply();
    }

    public static boolean getSound(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Sound",false);
    }

    public static void setPosion_Time(Context context, int is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("KEY_Posion_Time", is).apply();
    }

    public static int getPosion_Time(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("KEY_Posion_Time",0);
    }

    public static void setUpload(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Upload", is).apply();
    }

    public static boolean getUpload(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Upload",false);
    }

    public static void setCheck_Noti(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Check_Noti", is).apply();
    }

    public static boolean getCheck_Noti(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Check_Noti",true);
    }

    public static void setToi_Uu(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Toi_Uu", is).apply();
    }

    public static boolean getToi_Uu(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Toi_Uu",true);
    }

    public static void setTiet_Kiem(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Tiet_Kiem", is).apply();
    }

    public static boolean getTiet_Kiem(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Tiet_Kiem",true);
    }

    public static void setDanger(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_Danger", is).apply();
    }

    public static boolean getDanger(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_Danger",true);
    }

    public static void setNhietDo(Context context, String is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("KEY_NhietDo", is).apply();
    }

    public static String getNhietDo(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("KEY_NhietDo","");
    }

    public static void setTime(Context context, String is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("KEY_Time", is).apply();
    }

    public static String getTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("KEY_Time","");
    }

    public static void setTimeAds(Context context, long is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong("KEY_TimeAds", is).apply();
    }

    public static long getTimeAds(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong("KEY_TimeAds",0);
    }

    public static void setFristTime(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_FristTime", is).apply();
    }

    public static boolean getFristTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_FristTime",false);
    }

    public static void setShowDialogMain(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_ShowDialogMain", is).apply();
    }

    public static boolean getShowDialogMain(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_ShowDialogMain",false);
    }

    public static void setShowDialogCheDo(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("KEY_ShowDialogCheDo", is).apply();
    }

    public static boolean getShowDialogCheDo(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("KEY_ShowDialogCheDo",false);
    }

    public static void setKeyTimeToiUu(Context context, long is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong("KeyTimeToiUu", is).apply();
    }

    public static long getKeyTimeToiUu(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong("KeyTimeToiUu",0);
    }

    public static void setOne(Context context, boolean is) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("Key_One", is).apply();
    }

    public static boolean getOne(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Key_One",false);
    }
}
