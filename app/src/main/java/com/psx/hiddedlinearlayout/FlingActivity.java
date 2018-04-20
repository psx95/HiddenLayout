package com.psx.hiddedlinearlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;
import com.psx.hiddenlinearlayoutview.Interfaces.AnimationUpdateListeners;

public class FlingActivity extends AppCompatActivity {

    private HiddenLayoutView hiddenLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fling);
        hiddenLayoutView = findViewById(R.id.hidden_fling);
        setupHiddenFlingView();
    }

    private void setupHiddenFlingView() {
        hiddenLayoutView.setOverLayoutEventListener(view -> Toast.makeText(getApplicationContext(),"Pressed Revealed View "+view.getId(),Toast.LENGTH_SHORT).show());
        hiddenLayoutView.setUnderLayoutEventListener(new AnimationUpdateListeners.UnderLayoutEventListener() {
            @Override
            public void onUnderLayoutClickReceived(View view) {
                Toast.makeText(getApplicationContext(),"Pressed View hidden "+view.getId(),Toast.LENGTH_SHORT).show();
                hiddenLayoutView.closeRightHiddenView();
            }
        });
    }

    @Override
    protected void onDestroy() {
        getLifecycle().removeObserver(hiddenLayoutView);
        super.onDestroy();
    }
}
