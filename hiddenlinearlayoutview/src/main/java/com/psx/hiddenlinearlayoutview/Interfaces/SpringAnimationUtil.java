package com.psx.hiddenlinearlayoutview.Interfaces;

import android.app.Activity;
import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

public class SpringAnimationUtil {

    private View animatedView;

    private SpringAnimation xAnimation;
    private SpringAnimation reverseXAnim;

    private float dX;
    private static float finalPosDiff;
    private Context context;

    private boolean hiddenViewRevealed = false;
    private View underLayout;
    //private float dY;

    public SpringAnimationUtil(Context context, View animatedView, View underLayout) {
        if (animatedView != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)animatedView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            finalPosDiff = displayMetrics.widthPixels/4;
            this.animatedView = animatedView;
            this.underLayout = underLayout;
            animatedView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
            this.animatedView.setOnTouchListener(touchListener);
            this.context = context;
        }
    }

    // create X and Y animations for view's initial position once it's known
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            xAnimation = createSpringAnimation(animatedView, SpringAnimation.X, animatedView.getX()-finalPosDiff,
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
            reverseXAnim = createSpringAnimation(animatedView, SpringAnimation.X, animatedView.getX(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
            Log.d("ANIMATION","POSITION IS "+animatedView.getX()+ " MY POS "+(animatedView.getX()-finalPosDiff));
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    // capture the difference between view's top left corner and touch point
                    dX = v.getX() - event.getRawX();
                    reverseXAnim.cancel();
                    xAnimation.cancel();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float movement = event.getRawX() + dX;
                    if (!hiddenViewRevealed && movement < 0) {
                        if (Math.abs(movement) <= finalPosDiff)
                            animatedView.animate().x(movement).setDuration(0).start();
                        else {
                            animatedView.animate().x(movement).setDuration(0).start();
                            //underLayout.animate().scaleXBy(movement).setDuration(0).start();
                        }
                    }  else if (hiddenViewRevealed) {
                        if (Math.abs(movement) <= finalPosDiff)
                            animatedView.animate().x(movement).setDuration(0).start();
                        else {
                            animatedView.animate().x(movement).setDuration(0).start();
                            //underLayout.animate().scaleXBy(movement).setDuration(0).start();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if ((event.getRawX() + dX) < -10) {
                        Toast.makeText(context,"X Animation", Toast.LENGTH_SHORT).show();
                        xAnimation.start();
                        hiddenViewRevealed = true;
                    }
                    else if ((event.getRawX() + dX) > 10){
                        Toast.makeText(context,"Reverse Animation",Toast.LENGTH_SHORT).show();
                        reverseXAnim.start();
                        hiddenViewRevealed = false;
                    }
                    break;
            }
            return true;
        }
    };

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
}
