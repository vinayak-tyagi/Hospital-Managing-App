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

public class OtherStockActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText name,location,quantity;
    TextView date,time;
    Button update_btn;
    MySharedPreferences sp;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_other_stock);

        name = findViewById(R.id.name_ostock);
        location = findViewById(R.id.location_ostock);
        quantity = findViewById(R.id.quantity_ostock);
        update_btn = findViewById(R.id.ostock_update_btn);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Other Stock");
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

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String  quantities =   quantity.getText().toString().trim();
             String locations =    location.getText().toString().trim();

             if (quantities.isEmpty()){ quantity.setError("Enter Quantity");}
             if (locations.isEmpty()){ location.setError("Enter Location"); }
             else {
                 onUpdate(quantities,locations);
             }

             }
        });

    }

    private void onUpdate(final String quantities, final String locations) {
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
                        Toast.makeText(OtherStockActivity.this, msg+"", Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();

                    Utils.handleSimpleVolleyRequestError(error, OtherStockActivity.this);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",sp.getID("uid"));
                    params.put("other_location",locations);
                    params.put("other_quantity",quantities);
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

                    Utils.handleSimpleVolleyRequestError(error, OtherStockActivity.this);

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
