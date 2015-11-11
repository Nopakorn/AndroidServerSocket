package com.example.nopakorn.androidserversocket;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Batt3CommonActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    private Runnable runBattery = new Runnable() {
        @Override
        public void run() {
            callCommon();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batt3_common);
        handler.postDelayed(runBattery, 2000);
    }

    private void callCommon(){
        Intent intent = new Intent(Batt3CommonActivity.this, Batt3Activity.class);
        startActivity(intent);

        //finish();
    }


    @Override
    protected void onPause() {
        handler.removeCallbacks(runBattery);
        finish();
        super.onPause();
    }
}
