package com.psx.hiddenlinearlayoutview.Utilities;

import android.app.Activity;
import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;

import java.util.Calendar;

public class FlingAnimationUtil {

    private static String TAG = FlingAnimationUtil.class.getSimpleName();
    private static int CLICK_DURATION_IN_MILLIS = 500;
    private static int MOVE_THRESHOLD_IN_DP = 10;
    private DisplayMetrics displayMetrics;
    private FlingAnimation flingAnimation;
    private Context activityContext;
    private GestureDetector gestureDetector;
    private float pressedX, pressedY;
    private long clickStart = 0L;
    private View.OnTouchListener onTouchListener;

    public FlingAnimationUtil(Context context, View view) {
        displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        createFlingAnimationForHome(view);
        this.activityContext = context;
    }

    private void createFlingAnimationForHome(View v) {
        flingAnimation = new FlingAnimation(v, DynamicAnimation.TRANSLATION_X)
                .setFriction(0.8f)
                .setMinValue(-displayMetrics.widthPixels / 4)
                .setMaxValue(0);
        gestureDetector = new GestureDetector(activityContext, prepareGestureDetectorListener());
        initTouchListener();
        setTouchListenerOnView(v);
    }

    private void initTouchListener() {
        onTouchListener = (v, event) -> {
            boolean clickOccoured = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    clickStart = Calendar.getInstance().getTimeInMillis();
                    pressedX = event.getX();
                    pressedY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - clickStart;
                    if (clickDuration < CLICK_DURATION_IN_MILLIS && UtilityFunctions.distance(pressedX, pressedY, event.getX(), event.getY(), activityContext) < MOVE_THRESHOLD_IN_DP) {
                        Log.d(TAG, " Click duration " + clickDuration + " Distance " + UtilityFunctions.distance(pressedX, pressedY, event.getX(), event.getY(), activityContext));
                        clickOccoured = true;
                    }
                    break;
            }
            if (clickOccoured) {
                HiddenLayoutView.overLayoutEventListener.onOverLayoutClickRecieved(v);
            } else {
                gestureDetector.onTouchEvent(event);
            }
            return true;
        };
    }

    private void setTouchListenerOnView(View v) {
        if (v instanceof ViewGroup) {
            Log.d(TAG,"FOUND multiple children inside view");
            for (int i = 0; i < ((ViewGroup)v).getChildCount(); i++){
                View childView = ((ViewGroup)v).getChildAt(i);
                childView.setOnTouchListener(onTouchListener);
                Log.d(TAG," ID "+childView.getId() + " found at pos "+i);
            }
        } else {
            Log.d(TAG,"No Child views");
            v.setOnTouchListener(onTouchListener);
        }
    }

    private GestureDetector.OnGestureListener prepareGestureDetectorListener() {
        return new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (distanceX > 0) {
                    flingAnimation.setStartVelocity(1000);
                    flingAnimation.start();
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int helperVelocity = velocityX < 0 ? -200 : 200;
                flingAnimation.setStartVelocity(velocityX + helperVelocity);
                flingAnimation.start();
                return true;
            }
        };
    }

    public FlingAnimation getFlingAnimation() {
        return flingAnimation;
    }
}

