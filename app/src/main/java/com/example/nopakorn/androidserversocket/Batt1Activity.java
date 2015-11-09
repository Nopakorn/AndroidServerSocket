package com.example.nopakorn.androidserversocket;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Batt1Activity extends AppCompatActivity {

    private LinearLayout mBatteryGreenLine1;
    private LinearLayout mBatteryGreenLine2;
    private LinearLayout mBatteryGreenLine3;
    private LinearLayout mBatteryYellowLine1;
    private LinearLayout mBatteryYellowLine2;
    private LinearLayout mBatteryYellowLine3;
    private TextView mMessage;
    private int stateBattery;
    private int stateBattery2;

    private Handler handler = new Handler();

    private Runnable runCommon = new Runnable() {
        @Override
        public void run() {
            callCommon();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batt1);
        mMessage = (TextView) findViewById(R.id.message);
        mMessage.setText("OK");

        handler.postDelayed(runCommon,3000);

    }

    private void callCommon(){
//        Intent intent = new Intent(Batt1Activity.this, MainActivity.class);
//        startActivity(intent);
        finish();
    }

    protected void onPause() {
        handler.removeCallbacks(runCommon);
        finish();
        super.onPause();
    }
}
