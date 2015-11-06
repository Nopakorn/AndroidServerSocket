package com.example.nopakorn.androidserversocket;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ViewAActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_a);
        
    }

    @Override
    protected void onDestroy() {
        Log.d("Server","destroy view a");
        super.onDestroy();
    }
}
