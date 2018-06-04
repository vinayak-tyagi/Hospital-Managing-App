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
import android.widget.Toast;


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

public class LeaveActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText name,till,from,reason;
    TextView date,time;
    Button submit_btn;
    MySharedPreferences sp;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_leave);

        name = findViewById(R.id.name_leave);
        till = findViewById(R.id.till_leave);
        from = findViewById(R.id.from_leave);
        reason = findViewById(R.id.leave_reason);
        submit_btn = findViewById(R.id.leave_submit_btn);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Leave Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        date.setText(Utils.getDate());
        time.setText(Utils.getTime());

        sp = MySharedPreferences.getInstance(this);
        if(sp.getKey("uid") != null){
            String key = sp.getKey("uid");
            Log.e("VIN: Key: ",key+"");
            onSearch(key);
        }


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             String l_till =   till.getText().toString().trim();
             String l_reason =   reason.getText().toString().trim();
             String l_from =   from.getText().toString().trim();

             if (l_till.isEmpty()){ till.setError("Enter Date");}
             if (l_reason.isEmpty()){ reason.setError("Enter Reason");}
             if(l_from.isEmpty()){ from.setError("Enter Date");}
             else{  onUpdate(l_till,l_from,l_reason);  }


            }
        });

    }

    private void onUpdate(final String l_till, final String l_from, final String l_reason) {
        if (Utils.isNetworkAvailable()) {
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.UPDATE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    Log.e("VIN:", response + "");


                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        String msg =  jsonArray.getJSONObject(0).getString("msg");
                        Toast.makeText(LeaveActivity.this, msg+"", Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();

                    Utils.handleSimpleVolleyRequestError(error, LeaveActivity.this);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",sp.getID("uid"));
                    params.put("leave_from",l_from );
                    params.put("leave_till",l_till);
                    params.put("leave_reason",l_reason);
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

                    Utils.handleSimpleVolleyRequestError(error, LeaveActivity.this);

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
        name.setText(names);
    }
}
