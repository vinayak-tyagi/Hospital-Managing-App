package com.vinayak.app_1.Infrastuture;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;


public class AppController extends Application {


    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mContext = this;
    }

    public static synchronized Context getContext() {
        return mContext;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static String handleVolleyError(VolleyError error) {
        String message = null;
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            message = "Bad network Connection";
        } else if (error instanceof AuthFailureError) {
            message = "Failed to perform a request";
        } else if (error instanceof ServerError) {
            message = "Server error";
        } else if (error instanceof NetworkError) {
            message = "Network error while performing a request";
        } else if (error instanceof ParseError) {
            message = "Server response could not be parsed";
        }
        return message;
    }

}
