package com.vinayak.app_1.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.vinayak.app_1.Activity.HomeActivity;
import com.vinayak.app_1.Infrastuture.AppController;
import com.vinayak.app_1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) AppController.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showNoInternetToast(){
        Toast.makeText(AppController.getContext(), "No Internet available", Toast.LENGTH_SHORT).show();
    }

    public static String getDeviceId(){
        return Settings.Secure.getString(AppController.getContext().getContentResolver(),Settings.Secure.ANDROID_ID);
    }

    public static void goToHomeActivity(Activity mythis){
        Intent i = new Intent(mythis, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mythis.startActivity(i);
    }


    public static void handleSimpleVolleyRequestError(VolleyError error, Context context) {
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            try {
                String body = new String(error.networkResponse.data, "UTF-8");
                if (statusCode == 400) {
                    //server error
                    String errorMsg = Utils.simpleJsonParser(body);
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                }else if (statusCode == 401){
                    Toast.makeText(context, "You are unauthorized to view this request. Please try  again", Toast.LENGTH_SHORT).show();
                }else if (statusCode == 422){
                    JSONObject json = new JSONObject(body);
                    JSONArray namearray = json.names();

                    if (namearray.length() > 0){
                        String key = namearray.get(0).toString();
                        JSONArray ja = json.getJSONArray(key);
                        Toast.makeText(context, ja.get(0).toString(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    String errorString = AppController.handleVolleyError(error);
                    Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
                }
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
                Utils.showParsingErrorAlert(context);
            }
        } else {
            String errorString = AppController.handleVolleyError(error);
            Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
        }
    }

    //Parse Simple JSON Parse
    public static String simpleJsonParser(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        return obj.getString("msg");
    }

    public static void showParsingErrorAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.oops))
                .setMessage(context.getString(R.string.dont_worry_engineers_r_working))
                .setNegativeButton(context.getString(R.string.report_issue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO:: take user to report issue area
                    }
                })
                .setPositiveButton(context.getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String thisDate = simpleDateFormat.format(date);
        return  thisDate;
    }

    public static String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        String thistime = simpleDateFormat.format(date);
        return  thistime;
    }



}
