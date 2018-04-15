package com.psx.hiddedlinearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;
import com.psx.hiddenlinearlayoutview.Interfaces.AnimationUpdateListeners;

public class MainActivity extends AppCompatActivity {

    HiddenLayoutView hiddenLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hiddenLayoutView = findViewById(R.id.hidden);
        hiddenLayoutView.setOverLayoutEventListener(view -> Toast.makeText(getApplicationContext(),"Pressed View "+view.getId(),Toast.LENGTH_SHORT).show());
        hiddenLayoutView.setUnderLayoutEventListener(new AnimationUpdateListeners.UnderLayoutEventListener() {
            @Override
            public void onUnderLayoutClickRecieved(View view) {
                Toast.makeText(getApplicationContext(),"Pressed View "+view.getId(),Toast.LENGTH_SHORT).show();
                hiddenLayoutView.closeRightHiddenView();
            }
        });
        hiddenLayoutView.getInflatedUnderLayout().findViewById(R.id.refresh_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenLayoutView.closeRightHiddenView();
                Toast.makeText(getApplicationContext(),"PRESSED HOME REFRESH",Toast.LENGTH_SHORT).show();
            }
        });
        hiddenLayoutView.getInflatedOverLayout().findViewById(R.id.current_time_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"PRESSED TIME",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
