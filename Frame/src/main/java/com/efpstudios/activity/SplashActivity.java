package com.efpstudios.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.efpstudios.R;
import com.efpstudios.constant.Applications;
import com.efpstudios.constant.Constant;
import com.google.android.gms.ads.AdListener;

public class SplashActivity extends AppCompatActivity {

    private boolean statusIklan = true;
    int hitung = 0;
    int loadIklanBerapaKali = 5;
    private Applications applications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        applications = (Applications) getApplication();
        applications.initInterstitial();
        applications.loadIntersTitial();
        applications.getInterstitial().setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                hitung++;
                //Log.d("iklan", "gagal "+ String.valueOf(hitung));
                if(hitung<loadIklanBerapaKali){
                    if(statusIklan) {
                        applications.loadIntersTitial();
                    }
                }
                if(hitung == loadIklanBerapaKali){
                    if(statusIklan) {
                        statusIklan = false;
                        applications.setStatusIklan(Constant.gagalLoadIklan);
                        bukaActivity();
                    }
                }
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                if(statusIklan) {
                   // Log.d("iklan", "berhasil");
                    statusIklan = false;
                    applications.setStatusIklan(Constant.berhasilLoadIklan);
                    bukaActivity();
                }
                super.onAdLoaded();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(statusIklan) {
                    statusIklan = false;
                    applications.setStatusIklan(Constant.gagalLoadIklan);
                    bukaActivity();
                }
            }
        }, 15000);
    }

    private void bukaActivity(){
        Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
