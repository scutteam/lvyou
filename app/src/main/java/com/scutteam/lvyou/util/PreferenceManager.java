package com.scutteam.lvyou.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;

/**
 * Created by sam on 11/5/14.
 */
public class PreferenceManager {

    private static SharedPreferences getPreferences() {
        return LvYouApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static String getStringData(String key){
        return getPreferences().getString(key,"");
    }
    
    public static int getIntData(String key){
        return getPreferences().getInt(key,0);
    }

    public static long getLongData(String key){
        return getPreferences().getLong(key,0);
    }

    public static Float getFloatData(String key){
        return getPreferences().getFloat(key, 0);
    }

    public static boolean getBooleanData(String key){
        return getPreferences().getBoolean(key, false);
    }
    
    public static void setStringData(String key,String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key,value);
        editor.commit();
    }
    
    public static void setFloatData(String key,Float value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key,value);
        editor.commit();
    }
    
    public static void setIntData(String key,int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key,value);
        editor.commit();
    }
    
    public static void setLongData(String key,long value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key,value);
        editor.commit();
    }
    
    public static void setBooleanData(String key,Boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static void cleanPreference() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear();
        editor.commit();
    }

}
