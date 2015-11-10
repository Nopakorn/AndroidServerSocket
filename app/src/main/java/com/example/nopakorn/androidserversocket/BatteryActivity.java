package com.example.nopakorn.androidserversocket;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BatteryActivity extends AppCompatActivity {

    private LinearLayout mBatteryGreenLine1;
    private LinearLayout mBatteryGreenLine2;
    private LinearLayout mBatteryGreenLine3;
    private LinearLayout mBatteryYellowLine1;
    private LinearLayout mBatteryYellowLine2;
    private LinearLayout mBatteryYellowLine3;
    private TextView mMessage;
    private int stateBattery;
    private int stateBattery2;

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
//        if(savedInstanceState != null){
//
//            stateBattery = savedInstanceState.getInt("myStateBattery");
//            stateBattery2 = savedInstanceState.getInt("myStateBattery2");
//            Log.d("Server","state not null : "+stateBattery);
//        }

        mMessage = (TextView) findViewById(R.id.message);

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

    }

    @Override
    protected void onResume() {

        handleB.post(runB);
        super.onResume();
    }

    private Handler handleB = new Handler();

    private Runnable runB = new Runnable() {
        @Override
        public void run() {
            if (stateBattery > 6) {
                if (stateBattery2 == 4) {
                    stateBattery2 = 0;
                }
                updateBattery();
                handleB.postDelayed(runB, 1000);
            } else {
                initiateBattery();
                handleB.postDelayed(runB, 300);
            }
        }
    };

    public void initiateBattery() {
        mMessage.setText("initiate battery");

        if (stateBattery == 1) {
            mBatteryYellowLine3.setVisibility(LinearLayout.VISIBLE);
        } else if (stateBattery == 2) {
            mBatteryYellowLine2.setVisibility(LinearLayout.VISIBLE);
        } else if (stateBattery == 3) {
            mBatteryYellowLine1.setVisibility(LinearLayout.VISIBLE);
        } else if (stateBattery == 4) {
            mBatteryGreenLine3.setVisibility(LinearLayout.VISIBLE);
        } else if (stateBattery == 5) {
            mBatteryGreenLine2.setVisibility(LinearLayout.VISIBLE);
        } else if (stateBattery == 6) {
            mMessage.setText("OK");
            mBatteryGreenLine1.setVisibility(LinearLayout.VISIBLE);
        }

        stateBattery++;

    }

    private void updateBattery() {
        if (stateBattery == 8) {
            mBatteryGreenLine1.setVisibility(LinearLayout.INVISIBLE);
        } else if (stateBattery == 9) {
            mBatteryGreenLine2.setVisibility(LinearLayout.INVISIBLE);
        } else if (stateBattery == 10) {
            mBatteryGreenLine3.setVisibility(LinearLayout.INVISIBLE);
            mMessage.setText("Charge");
        } else {
            if (stateBattery2 == 0 && stateBattery > 10) {
                mMessage.setText("Charge");
                mBatteryYellowLine1.setVisibility(LinearLayout.INVISIBLE);
            } else if (stateBattery2 == 1) {
                mBatteryYellowLine1.setVisibility(LinearLayout.VISIBLE);
            } else if (stateBattery2 == 2) {
                mBatteryGreenLine3.setVisibility(LinearLayout.VISIBLE);
                mMessage.setText("OK");
            } else if (stateBattery2 == 3) {
                mMessage.setText("Charge");
                mBatteryGreenLine3.setVisibility(LinearLayout.INVISIBLE);
            }
        }
        stateBattery2++;
        stateBattery++;
    }

    @Override
    protected void onPause() {
        handleB.removeCallbacks(runB);
        finish();
        super.onPause();
    }
}

