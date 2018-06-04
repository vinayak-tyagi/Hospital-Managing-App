package com.vinayak.app_1.Activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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

public class IPDRegisterActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView date, time;
    EditText name,age,mobile,address,consultant,ward,charge,id;
    Button register;
    ProgressDialog pd;
    MySharedPreferences sp;

    private static String UID = "KHRCIPD ";
    private static String TAG = IPDRegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ipd_registration);

        sp = MySharedPreferences.getInstance(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("IPD Registration");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        id = findViewById(R.id.unique_id);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        name = findViewById(R.id.username_id);
        age = findViewById(R.id.age_id);
        mobile = findViewById(R.id.mobile_id);
        address =findViewById(R.id.address_id);
        consultant = findViewById(R.id.consultant_id);
        ward = findViewById(R.id.ward_id);
        charge = findViewById(R.id.charge_id);
        register = findViewById(R.id.ipd_register_btn);

        time.setText(Utils.getTime());
        date.setText(Utils.getDate());

        Log.e("VIN: ",sp.getKey("uid")+"");
        String key = sp.getKey("uid")+"" ;
        if (!key.equals("null")){
              id.setText(key);
        }else{
               id.setText("Register First to Generate");
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().isEmpty()){ name.setError("Enter Name");}
                else if (mobile.getText().toString().isEmpty() ){ mobile.setError("Enter Contact Number");}
                else if (age.getText().toString().isEmpty()){age.setError("Enter Age");}
                else if (address.getText().toString().isEmpty()){ address.setError("Enter Address"); }
                else if (consultant.getText().toString().isEmpty()){ consultant.setError("Enter Address"); }
                else{ onSuccess(); }

            }
        });


    }

    private void onSuccess() {
        if (Utils.isNetworkAvailable()){

            final String username = name.getText().toString().trim();
            final String contact = mobile.getText().toString().trim();
            final String age = mobile.getText().toString().trim();
            final String addres = address.getText().toString().trim();
            final String consul = consultant.getText().toString().trim();
            final String wards = ward.getText().toString().trim();
            final String charges = charge.getText().toString().trim();
            String device_id = Utils.getDeviceId();
            final String timing = time.getText().toString().trim();
            final String dating = date.getText().toString().trim();


            // volly request
            pd.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    Log.e("VIN: ",response);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        String id =   jsonArray.getJSONObject(0).getString("id");
                        Log.e("VIN: ", id+"");

                        createUniqueID(id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Utils.handleSimpleVolleyRequestError(error,IPDRegisterActivity.this);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("name",username);
                    params.put("age",age);
                    params.put("date",dating);
                    params.put("time",timing);
                    params.put("address",addres);
                    params.put("consultant",consul);
                    params.put("charges",charges);
                    params.put("ward",wards);
                    params.put("mobile",contact);

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

            AppController.getInstance().addToRequestQueue(stringRequest,TAG);


        }else {
            Utils.showNoInternetToast();
        }
    }

    private void createUniqueID(final String id) {
        if (Utils.isNetworkAvailable()){
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.USERID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    Log.e("VIN: ",response);

                    try {
                       JSONArray jsonArray = new JSONArray(response);
                        String unique_id  = jsonArray.getJSONObject(0).getString("User_id");
                        sp.setKey("uid",unique_id);
                        alertDialog(unique_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Utils.handleSimpleVolleyRequestError(error,IPDRegisterActivity.this);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("id",id);
                    params.put("user_id",UID+id);

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

            AppController.getInstance().addToRequestQueue(stringRequest,TAG);


        }else {
            Utils.showNoInternetToast();
        }

    }

    private void alertDialog(String unique_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Registration Successful")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Your Unique Id is "+unique_id)
                .setPositiveButton("OK",null);
        builder.create().show();

    }
}
