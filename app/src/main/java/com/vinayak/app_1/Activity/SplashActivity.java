package com.vinayak.app_1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.vinayak.app_1.R;


public class SplashActivity extends AppCompatActivity {
    private  static int SPLASH_TIME_OUT = 5000;
    TextView txt;
    Animation fromtop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        txt = findViewById(R.id.txt);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
                }
        },SPLASH_TIME_OUT);

        fromtop = AnimationUtils.loadAnimation(this,R.anim.from_top);
        txt.setAnimation(fromtop);

    }
}
