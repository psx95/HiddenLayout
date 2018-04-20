package com.psx.hiddenlinearlayoutview.Utilities;

import android.app.Activity;
import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Toast;

import com.psx.hiddenlinearlayoutview.HiddenLayoutView;

public class SpringAnimationUtil {

    private View animatedView;
    private static final String TAG = SpringAnimationUtil.class.getSimpleName();

    private SpringAnimation xAnimation;
    private SpringAnimation reverseXAnim;

    private float dX;
    private static float finalPosDiff;
    private Context context;

    private boolean hiddenViewRevealed = false;
    private boolean scalehiddenView, maxReached = false;
    private View underLayout;
    float maxMovement;
    private VelocityTracker velocityTracker;
    private HiddenLayoutView hiddenLayoutView;
    //private float dY;

    public SpringAnimationUtil(Context context, View animatedView, float revealViewPercentageRight,
                               View underLayout, boolean scaleHiddenView, float maxMovementFactor, HiddenLayoutView hiddenLayoutView) {
        if (animatedView != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)animatedView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            finalPosDiff = displayMetrics.widthPixels * revealViewPercentageRight;
            maxMovement = maxMovementFactor*finalPosDiff;
            Log.i(TAG,"FInal pos Diff "+finalPosDiff);
            Log.d(TAG,"MaX Movement allowed "+maxMovement);
            this.hiddenLayoutView = hiddenLayoutView;
            this.animatedView = animatedView;
            this.underLayout = underLayout;
            this.scalehiddenView = scaleHiddenView;
            createAnimations();
            this.animatedView.setOnTouchListener(touchListener);
            this.context = context;
        }
    }

    private void createAnimations() {
        xAnimation = createSpringAnimation(animatedView, SpringAnimation.X, animatedView.getX()-finalPosDiff,
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        reverseXAnim = createSpringAnimation(animatedView, SpringAnimation.X, animatedView.getX(),
                SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_NO_BOUNCY);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    // capture the difference between view's top left corner and touch point
                    dX = v.getX() - event.getRawX();
                    reverseXAnim.cancel();
                    xAnimation.cancel();
                    maxReached = false;
                    Log.d(TAG,"MOTION ACTION DOWN");
                    velocityTracker = VelocityTracker.obtain();
                    velocityTracker.addMovement(event);
                    return false;
                case MotionEvent.ACTION_MOVE:
                    if (velocityTracker == null)
                        break;
                    velocityTracker.addMovement(event);
                    float movement = event.getRawX() + dX;
                    Log.d(TAG,"Movement Action move"+movement);
                    float scaleFactor = movement > 0 ? -0.04f : 0.04f;
                    if (!scalehiddenView)
                        scaleFactor = 0;
                    animatedView.getParent().requestDisallowInterceptTouchEvent(true);
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (event.getActionIndex() <<
                                    MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    animatedView.onTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                    if (!hiddenViewRevealed && movement < 0) {
                        moveAndScale(movement, scaleFactor);
                    }  else if (hiddenViewRevealed) {
                        moveAndScale(movement, scaleFactor);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (velocityTracker == null)
                        break;
                    velocityTracker.addMovement(event);
                    Log.d(TAG,"Motion UP ");
                    if ((event.getRawX() + dX) < -finalPosDiff/2) {
                        Log.d(TAG,"Anim "+(event.getRawX() + dX));
                        xAnimation.start();
                        hiddenViewRevealed = true;
                    } else if ((event.getRawX() + dX) < 0 && (event.getRawX() + dX) > -finalPosDiff/2){
                       // Log.d(TAG,"Reverse Anim "+(event.getRawX() + dX));
                        reverseXAnim.start();
                        hiddenViewRevealed = false;
                    }
                    else if ((event.getRawX() + dX) > 0){
                        Toast.makeText(context,"Reverse Animation",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"reverse Anim "+(event.getRawX() + dX));
                        reverseXAnim.start();
                        hiddenViewRevealed = false;
                    }
                    underLayout.animate().scaleX(1f).setDuration(0).start();
                    if (Math.abs(event.getRawX() + dX) >= maxMovement) {
                        hiddenLayoutView.animationUpdateListeners.onMaxSpringPull();
                    } else {
                        Log.i(TAG,"Not a max pull event max Movement "+maxMovement +" actual move "+(event.getRawX()+dX));
                    }
                    Log.d(TAG,"Motion UP ");
                    velocityTracker.recycle();
                    velocityTracker = null;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d(TAG,"ACTION CANCEL");
                    if (velocityTracker == null)
                        break;
                    velocityTracker.recycle();
                    velocityTracker = null;
                    if ((event.getRawX() + dX) < -finalPosDiff/2) {
                        Log.d(TAG,"Anim "+(event.getRawX() + dX));
                        xAnimation.start();
                        hiddenViewRevealed = true;
                    } else if ((event.getRawX() + dX) < 0 && (event.getRawX() + dX) > -finalPosDiff/2){
                        reverseXAnim.start();
                        hiddenViewRevealed = false;
                    }
                    else if ((event.getRawX() + dX) > 0){
                        Log.d(TAG,"reverse Anim "+(event.getRawX() + dX));
                        reverseXAnim.start();
                        hiddenViewRevealed = false;
                    }
                    underLayout.animate().scaleX(1f).setDuration(0).start();
                    if (Math.abs(event.getRawX() + dX) >= maxMovement) {
                        hiddenLayoutView.animationUpdateListeners.onMaxSpringPull();
                    } else {
                        Log.i(TAG,"Not a max pull event max Movement "+maxMovement +" actual move "+(event.getRawX()+dX));
                    }
                    break;
            }
            return true;
        }
    };

    private void moveAndScale(float movement, float scaleFactor) {
        if (movement<=-5) {
            if (Math.abs(movement) <= finalPosDiff)
                animatedView.animate().x(movement / 1.5f).setDuration(0).start();
            else if (Math.abs(movement) <= maxMovement) {
                underLayout.animate().scaleXBy(scaleFactor).setDuration(0).start();
                animatedView.animate().x(movement / 1.5f).setDuration(0).start();
            }
        }
    }

    private static SpringAnimation createSpringAnimation(View view,
                                                         DynamicAnimation.ViewProperty property,
                                                         float finalPosition,
                                                         float stiffness,
                                                         float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new  SpringForce(finalPosition);
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }

    public SpringAnimation getxAnimation () {
        return xAnimation;
    }

    public SpringAnimation getReverseXAnim () {
        return reverseXAnim;
    }
}
