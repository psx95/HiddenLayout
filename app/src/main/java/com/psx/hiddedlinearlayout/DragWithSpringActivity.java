package com.psx.hiddedlinearlayout;

import android.os.Bundle;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;

public class DragWithSpringActivity extends AppCompatActivity {

    private HiddenLayoutView hiddenLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_with_spring);
        hiddenLayoutView = findViewById(R.id.hidden_spring);
        setupListnersOnViews();
        setupHiddenSpring();
        getLifecycle().addObserver(hiddenLayoutView);
    }

    private void setupHiddenSpring() {
        hiddenLayoutView.setDampingAndStiffnessForDragWithSpringForward(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY,SpringForce.STIFFNESS_LOW);
        hiddenLayoutView.setAnimationUpdateListeners(() -> Toast.makeText(getApplicationContext(),"PULLED!!",Toast.LENGTH_SHORT).show());
        hiddenLayoutView.getInflatedOverLayout()
                .findViewById(R.id.sender_name)
                .setOnClickListener(v -> {
                    // for detecting clicks on individual items on the Over Layout.
                    Log.d("test","Pressed on "+v.getId());
                    Toast.makeText(v.getContext(),"Hello "+v.getId(),Toast.LENGTH_SHORT).show();
                });
    }

    private void setupListnersOnViews() {
        hiddenLayoutView.setOverLayoutEventListener(view -> {
            // detects clicks on the overall layout
            Toast.makeText(getApplicationContext(),"Pressed Revealed View ",Toast.LENGTH_SHORT).show();
        });
        hiddenLayoutView.setUnderLayoutEventListener(view -> {
            Toast.makeText(getApplicationContext(),"Pressed View hidden "+view.getId(),Toast.LENGTH_SHORT).show();
            hiddenLayoutView.closeRightHiddenView();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(hiddenLayoutView);
    }
}
