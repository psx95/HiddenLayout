package com.psx.hiddedlinearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;

public class MainActivity extends AppCompatActivity {

    HiddenLayoutView hiddenLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hiddenLayoutView = findViewById(R.id.hidden);
    }
}
