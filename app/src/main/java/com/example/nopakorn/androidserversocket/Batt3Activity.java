package com.example.nopakorn.androidserversocket;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Batt3Activity extends AppCompatActivity {

    private LinearLayout mBatteryGreenLine1;
    private LinearLayout mBatteryGreenLine2;
    private LinearLayout mBatteryGreenLine3;
    private LinearLayout mBatteryYellowLine1;
    private LinearLayout mBatteryYellowLine2;
    private LinearLayout mBatteryYellowLine3;
    private TextView mMessage;
    private int stateBattery;

    private Handler handler = new Handler();

    private Runnable runBattery = new Runnable() {
        @Override
        public void run() {
            if(stateBattery < 3){
                updateBattery();
                handler.postDelayed(runBattery, 1000);
            }else{
                callCommon();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batt3);
        mMessage = (TextView) findViewById(R.id.message);
        mMessage.setText("Charging");

        mBatteryGreenLine1 = (LinearLayout) findViewById(R.id.battery_green1);
        mBatteryGreenLine2 = (LinearLayout) findViewById(R.id.battery_green2);
        mBatteryGreenLine3 = (LinearLayout) findViewById(R.id.battery_green3);
        mBatteryYellowLine1 = (LinearLayout) findViewById(R.id.battery_yellow1);
        mBatteryYellowLine2 = (LinearLayout) findViewById(R.id.battery_yellow2);
        mBatteryYellowLine3 = (LinearLayout) findViewById(R.id.battery_yellow3);

        mBatteryGreenLine1.setVisibility(LinearLayout.INVISIBLE);
        mBatteryGreenLine2.setVisibility(LinearLayout.INVISIBLE);
        mBatteryGreenLine3.setVisibility(LinearLayout.INVISIBLE);
        mBatteryYellowLine1.setVisibility(LinearLayout.INVISIBLE);
        mBatteryYellowLine2.setVisibility(LinearLayout.INVISIBLE);
        mBatteryYellowLine3.setVisibility(LinearLayout.INVISIBLE);
        stateBattery = 0;
        handler.post(runBattery);


    }
    private void updateBattery(){
        if(stateBattery == 0) {
            mBatteryYellowLine3.setVisibility(LinearLayout.VISIBLE);
        }else if(stateBattery == 1){
            mBatteryYellowLine2.setVisibility(LinearLayout.VISIBLE);
        }else if(stateBattery == 2) {
            mBatteryYellowLine1.setVisibility(LinearLayout.VISIBLE);
        }
        stateBattery++;
    }

    private void callCommon(){
        stateBattery = 0;
        Intent intent = new Intent(Batt3Activity.this, Batt3CommonActivity.class);
        startActivity(intent);
    }
    protected void onPause() {
        handler.removeCallbacks(runBattery);
        finish();
        super.onPause();
    }
}
