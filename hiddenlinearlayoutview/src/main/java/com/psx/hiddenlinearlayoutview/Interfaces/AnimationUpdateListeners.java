package com.psx.hiddenlinearlayoutview.Interfaces;

import android.support.animation.FlingAnimation;
import android.view.View;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;

public interface AnimationUpdateListeners {

    interface OverLayoutEventListener {
        void onOverLayoutClickRecieved(View view);
    }

    interface UnderLayoutEventListener {
        void onUnderLayoutClickRecieved(View view);
    }

    void onMaxSpringPull();
}
