package com.psx.hiddenlinearlayoutview.Interfaces;

import android.support.animation.FlingAnimation;
import android.view.View;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;

public interface AnimationUpdateListeners {

    interface OverLayoutEventListener {
        void onOverLayoutClickReceived(View view);
    }

    interface UnderLayoutEventListener {
        void onUnderLayoutClickReceived(View view);
    }

    void onMaxSpringPull();
}
