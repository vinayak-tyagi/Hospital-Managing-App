package com.vinayak.app_1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    //instance field
    private static SharedPreferences mSharedPreference;
    private static MySharedPreferences mInstance = null;
    private static Context mContext;

    //Shared Preference key
    private String KEY_PREFERENCE_NAME = "App_1";


    //private keyS
    public String KEY_DEFAULT = null;

    public MySharedPreferences() {
        mSharedPreference = mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static MySharedPreferences getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new MySharedPreferences();
        }
        return mInstance;
    }

    //Method to set boolean for (AppIntro)
    public void setBooleanKey(String keyname) {
        mSharedPreference.edit().putBoolean(keyname, true).apply();
    }

    public void setBooleanKey(String keyname, boolean state) {
        mSharedPreference.edit().putBoolean(keyname, state).apply();
    }

    /*
     * Method to get boolan key
     * true = means set
     * false = not set (show app intro)
     * */
    public boolean getBooleanKey(String keyname) {
        return mSharedPreference.getBoolean(keyname, false);
    }


    //Method to store user Mobile number
    public boolean setKey(String keyname, String mobile) {
        mSharedPreference.edit().putString(keyname, mobile).apply();
        return false;
    }

    //Method to get User mobile number
    public String getKey(String keyname) {
        return mSharedPreference.getString(keyname, KEY_DEFAULT);
    }

    public boolean setID(String keyname, String id) {
        mSharedPreference.edit().putString(keyname, id).apply();
        return false;
    }

    //Method to get User mobile number
    public String getID(String keyname) {
        return mSharedPreference.getString(keyname, KEY_DEFAULT);
    }

    public void setInt(String key, int value) {
        mSharedPreference.edit().putInt(key, value).apply();

    }

    public int getInt(String key) {
        return mSharedPreference.getInt(key, 0);
    }

    /*
     * Return false when not loggedin
     * Return true when loggedin
     * */
    public boolean isUserLoggedIn() {
        String r = mSharedPreference.getString("apikey", KEY_DEFAULT);
        return !(r == null || r.equals(KEY_DEFAULT));
    }

    public boolean setClear() {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.clear();
        editor.apply();
        return true;

    }

}
