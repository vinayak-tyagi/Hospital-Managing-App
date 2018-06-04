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

public class ProcedureActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText name, age , contact, charges, procedure;
    Button  save;
    TextView date, time;
    MySharedPreferences sp;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_procedure);


        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Procedure");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = findViewById(R.id.procedure_name_id);
        age = findViewById(R.id.procedure_age_id);
        contact = findViewById(R.id.procedure_contact_id);
        charges = findViewById(R.id.procedure_charge_id);
        procedure = findViewById(R.id.procedure_id);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        save = findViewById(R.id.procedure_update_btn);

        pd =  new ProgressDialog(this);
        pd.setMessage("Loading...");

        time.setText(Utils.getTime());
        date.setText(Utils.getDate());

        sp = MySharedPreferences.getInstance(this);
        if(sp.getKey("uid") != null){
           String key = sp.getKey("uid");
           Log.e("VIN: Key: ",key+"");
           onSearch(key);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String procedures = procedure.getText().toString().trim();
                if (procedures.isEmpty()){procedure.setError("Enter Procedure");}
                else {
                      onUpdate();
                }
            }
        });

    }

    private void onUpdate() {

        if (Utils.isNetworkAvailable()) {

         final String prod =   procedure.getText().toString().trim();
         final String charge = charges.getText().toString().trim();

            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.UPDATE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    Log.e("VIN:", response + "");


                    try {
                        JSONArray jsonArray = new JSONArray(response);
                      String msg =  jsonArray.getJSONObject(0).getString("msg");
                       Toast.makeText(ProcedureActivity.this, msg+"", Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();

                    Utils.handleSimpleVolleyRequestError(error, ProcedureActivity.this);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",sp.getID("uid"));
                    params.put("procedures", prod);
                    params.put("charges",charge);
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
                       JSONArray   jsonArray = new JSONArray(response);
                        JSONObject innerObj = jsonArray.getJSONObject(0);
                      String names =  innerObj.getString("username");
                      String contacts =  innerObj.getString("contact");
                      String ages =  innerObj.getString("age");

                      onUiUpdate(names, contacts , ages);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();

                    Utils.handleSimpleVolleyRequestError(error, ProcedureActivity.this);

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

    private void onUiUpdate(String names, String contacts, String ages) {
        name.setText(names);
        age.setText(ages);
        contact.setText(contacts);
    }


}
