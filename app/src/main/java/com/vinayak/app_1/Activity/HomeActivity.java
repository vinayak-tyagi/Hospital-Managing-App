package com.vinayak.app_1.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.vinayak.app_1.R;
import com.vinayak.app_1.Utils.Utils;

public class HomeActivity extends AppCompatActivity {

    Button opd , ipd, feedback , leave ,ot,search ,procedure, otherStock, medicineStock;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        if(!Utils.isNetworkAvailable()){
            Utils.showNoInternetToast();
        }

        opd = findViewById(R.id.opd_btn);
        ipd = findViewById(R.id.ipd_register_btn);
        feedback = findViewById(R.id.feedback_btn);
        leave = findViewById(R.id.leave_btn);
        ot = findViewById(R.id.ot_btn);
        search = findViewById(R.id.search_btn);
        procedure = findViewById(R.id.procedure_btn);
        otherStock = findViewById(R.id.oth_stock_btn);
        medicineStock = findViewById(R.id.medical_stock_btn);


        opd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,OPDRegisterActivity.class));
            }
        });
        ipd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,IPDRegisterActivity.class));
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,FeedbackActivity.class));
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,LeaveActivity.class));
            }
        });
        ot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,OtActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SearchActivity.class));
            }
        });
        procedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ProcedureActivity.class));
            }
        });
        otherStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,OtherStockActivity.class));
            }
        });
        medicineStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(HomeActivity.this,MedicalStockActivity.class));
            }
        });




    }
}
