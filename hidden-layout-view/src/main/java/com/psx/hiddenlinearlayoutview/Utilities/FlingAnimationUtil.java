package com.psx.hiddenlinearlayoutview.Utilities;

import android.app.Activity;
import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;

import java.util.Calendar;

import static com.psx.hiddenlinearlayoutview.Utilities.Constants.CLICK_DURATION_IN_MILLIS;
import static com.psx.hiddenlinearlayoutview.Utilities.Constants.MOVE_THRESHOLD_IN_DP;

public class FlingAnimationUtil {

    private static String TAG = FlingAnimationUtil.class.getSimpleName();
    private DisplayMetrics displayMetrics;
    private FlingAnimation flingAnimation, reverseFlingAnimation;
    private Context activityContext;
    private GestureDetector gestureDetector;
    private float pressedX, pressedY;
    private long clickStart = 0L;
    private View.OnTouchListener onTouchListener;
    private float minValue;
    private HiddenLayoutView hiddenLayoutView;
    private float reverseAnimationStartVelocity;
    private VelocityTracker velocityTracker;
    private View inflatedOverLayout;
    private float friction, frictionForReverseFling;

    public FlingAnimationUtil(Context context, View inflatedOverLayout, float revealViewPercentageRight, float friction, float frictionForReverseFling, HiddenLayoutView hiddenLayoutView) {
        displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.minValue = revealViewPercentageRight;
        this.hiddenLayoutView = hiddenLayoutView;
        this.activityContext = context;
        this.inflatedOverLayout = inflatedOverLayout;
        this.friction = friction;
        this.frictionForReverseFling = frictionForReverseFling;
        setSpeedForReverseAnimation();
        createFlingAnimationForHome(inflatedOverLayout);
    }

    private void setSpeedForReverseAnimation() {
        float pixelsRevealed = displayMetrics.widthPixels * minValue;
        float dpsRevealed = UtilityFunctions.convertPixelsToDp(pixelsRevealed, activityContext);
        reverseAnimationStartVelocity = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsRevealed, this.activityContext.getResources().getDisplayMetrics());
    }

    private void createFlingAnimationForHome(View inflatedOverLayout) {
        flingAnimation = new FlingAnimation(inflatedOverLayout, DynamicAnimation.TRANSLATION_X)
                .setFriction(friction)
                .setMinValue(-displayMetrics.widthPixels * minValue)
                .setMaxValue(0);
        reverseFlingAnimation = new FlingAnimation(inflatedOverLayout, DynamicAnimation.TRANSLATION_X)
                .setFriction(frictionForReverseFling)
                .setMinValue(-displayMetrics.widthPixels * minValue)
                .setMaxValue(0)
                .setStartVelocity(reverseAnimationStartVelocity);
        gestureDetector = new GestureDetector(activityContext, prepareGestureDetectorListener());
        initTouchListener();
        setTouchListenerOnView(inflatedOverLayout);
    }

    private void initTouchListener() {
        onTouchListener = (v, event) -> {
            boolean clickOccoured = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    clickStart = Calendar.getInstance().getTimeInMillis();
                    pressedX = event.getX();
                    pressedY = event.getY();
                    if (flingAnimation!=null)
                        flingAnimation.cancel();
                    else if (reverseFlingAnimation!=null)
                        reverseFlingAnimation.cancel();
                    velocityTracker = VelocityTracker.obtain();
                    velocityTracker.addMovement(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    if (velocityTracker == null)
                        break;
                    velocityTracker.addMovement(event);
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - clickStart;
                    if (clickDuration < CLICK_DURATION_IN_MILLIS && UtilityFunctions.distance(pressedX, pressedY, event.getX(), event.getY(), activityContext) < MOVE_THRESHOLD_IN_DP) {
                        clickOccoured = true;
                    }
                    velocityTracker.recycle();
                    velocityTracker = null;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (velocityTracker == null)
                        break;
                    velocityTracker.addMovement(event);
                    inflatedOverLayout.getParent().requestDisallowInterceptTouchEvent(true);
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (event.getActionIndex() <<
                                    MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    inflatedOverLayout.onTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (velocityTracker == null)
                        break;
                    velocityTracker.recycle();
                    velocityTracker = null;
                    long clickDuration1 = Calendar.getInstance().getTimeInMillis() - clickStart;
                    if (clickDuration1 < CLICK_DURATION_IN_MILLIS && UtilityFunctions.distance(pressedX, pressedY, event.getX(), event.getY(), activityContext) < MOVE_THRESHOLD_IN_DP) {
                        clickOccoured = true;
                    }
                    break;
            }
            if (clickOccoured) {
                v.performClick();
                hiddenLayoutView.overLayoutEventListener.onOverLayoutClickReceived(v);
            } else {
                gestureDetector.onTouchEvent(event);
            }
            return true;
        };
    }

    private void setTouchListenerOnView(View inflatedOverLayout) {
        if (inflatedOverLayout instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) inflatedOverLayout).getChildCount(); i++) {
                View childView = ((ViewGroup) inflatedOverLayout).getChildAt(i);
                childView.setOnTouchListener(onTouchListener);
            }
        } else {
            inflatedOverLayout.setOnTouchListener(onTouchListener);
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

    public FlingAnimation getReverseFlingAnimation() {
        return reverseFlingAnimation;
    }
}

