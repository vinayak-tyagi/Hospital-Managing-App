package com.vinayak.app_1.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vinayak.app_1.Infrastuture.AppController;
import com.vinayak.app_1.R;
import com.vinayak.app_1.Utils.Endpoints;
import com.vinayak.app_1.Utils.MySharedPreferences;
import com.vinayak.app_1.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText uid;
    TextView date,time,summary;
    Button button;
    MySharedPreferences sp;
    ProgressDialog pd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);

       uid = findViewById(R.id.uid_search);
       button = findViewById(R.id.seach_submit_btn);
       summary = findViewById(R.id.summary);

        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Search");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        sp = MySharedPreferences.getInstance(this);
        if(sp.getKey("uid") != null){
            String key = sp.getKey("uid");
            uid.setText(key);
            Log.e("VIN: Key: ",key+"");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.getText().toString().trim().isEmpty()){ uid.setError("Please Enter unique ID");}
                else{ onSearch(uid.getText().toString().trim()); }
            }
        });

    }

    private void onSearch(final String key) {
        if (Utils.isNetworkAvailable()) {

            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.SEARCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    Log.e("VIN:", response + "");


                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject innerObj = jsonArray.getJSONObject(0);
                        String names =  innerObj.getString("username");


                        onUiUpdate(names);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();

                    Utils.handleSimpleVolleyRequestError(error, SearchActivity.this);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", key);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("Accept", "application/json");
                    header.put("Content-Type", "application/x-www-form-urlencoded");
                    return header;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest, AppController.TAG);

        }else{ Utils.showNoInternetToast(); }



    }

    private void onUiUpdate(String names) {
        summary.setText("User is "+ names);
    }
}
