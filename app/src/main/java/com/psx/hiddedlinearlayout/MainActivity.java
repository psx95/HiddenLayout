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
    }
}
