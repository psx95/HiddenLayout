package com.psx.hiddenlinearlayoutview;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

import com.psx.hiddenlinearlayoutview.Interfaces.AnimationUpdateListeners;
import com.psx.hiddenlinearlayoutview.Utilities.FlingAnimationUtil;
import com.psx.hiddenlinearlayoutview.Utilities.SpringAnimationUtil;

public class HiddenLayoutView extends LinearLayout implements LifecycleObserver{

    private static final String TAG = HiddenLayoutView.class.getSimpleName();
    private int layout_over;
    private int layout_under;
    private Context context;
    private View hiddenLayoutView;
    private View inflatedUnderLayout, inflatedOverLayout;
    private String animationType;
    private FlingAnimation flingReverseAnimation;
    private SpringAnimation springReverseAnim;
    private float revealViewPercentageRight;
    private DynamicAnimation animation, reverseAnimation;
    private boolean scaleHiddenView;
    public static AnimationUpdateListeners.OverLayoutEventListener overLayoutEventListener;
    public static AnimationUpdateListeners.UnderLayoutEventListener underLayoutEventListener;
    private float maxMovementFactor;

    public HiddenLayoutView(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public HiddenLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    private void initView(AttributeSet attributeSet) {
        loadPreferencesFromAttributes(attributeSet);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            Log.d(TAG, "Layout Inflater not null");
            hiddenLayoutView = layoutInflater.inflate(R.layout.hidded_layout, this);
            ViewStub under = hiddenLayoutView.findViewById(R.id.under_layout);
            ViewStub over = hiddenLayoutView.findViewById(R.id.over_layout);
            under.setLayoutResource(layout_under);
            over.setLayoutResource(layout_over);
            inflatedUnderLayout = under.inflate();
            inflatedOverLayout = over.inflate();
        } else {
            Log.d(TAG, "Layout inflater null");
        }
        initListeners();
        setupListeners();
        setupAnimations();
    }

    private void setupListeners() {
        inflatedUnderLayout.setOnClickListener(v -> underLayoutEventListener.onUnderLayoutClickRecieved(v));
    }

    private void initListeners() {
        overLayoutEventListener = (view) -> {};
    }

    private void setupAnimations() {
        int animType = Integer.parseInt(animationType);
        switch (animType) {
            case 1:
                Log.d(TAG,"Spring Animation");
                SpringAnimationUtil springAnimationUtil = new SpringAnimationUtil(context, inflatedOverLayout, revealViewPercentageRight, inflatedUnderLayout.findViewById(R.id.revealed_view_right), scaleHiddenView, maxMovementFactor);
                SpringAnimation springAnimation = springAnimationUtil.getxAnimation();
                if (springAnimation == null)
                    Log.wtf(TAG,"SPringanimation is null");
                springReverseAnim = springAnimationUtil.getReverseXAnim();
                animation = springAnimation;
                reverseAnimation = springReverseAnim;
                break;
            case 2:
                FlingAnimationUtil flingAnimationUtil = new FlingAnimationUtil(context, inflatedOverLayout, revealViewPercentageRight);
                FlingAnimation flingAnimation = flingAnimationUtil.getFlingAnimation();
                flingReverseAnimation = flingAnimation.setStartVelocity(1000);
                animation = flingAnimation;
                reverseAnimation = flingReverseAnimation;
                break;
            default:
                Log.e(TAG,"Unable to resolve Animation "+animation.getClass().getSimpleName());
        }
    }

    private void loadPreferencesFromAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            Log.d(TAG, "Attribute set not null");
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.HiddenLayoutView);
            try {
                layout_over = typedArray.getResourceId(R.styleable.HiddenLayoutView_layout_over, R.layout.over_layout_default);
                layout_under = typedArray.getResourceId(R.styleable.HiddenLayoutView_layout_under, R.layout.under_layout_default);
                revealViewPercentageRight = typedArray.getFloat(R.styleable.HiddenLayoutView_revealPercentageViewRight, 0.2f);
                scaleHiddenView = typedArray.getBoolean(R.styleable.HiddenLayoutView_scaleHiddenView, false);
                animationType = typedArray.getString(R.styleable.HiddenLayoutView_animationEffect);
                maxMovementFactor = typedArray.getFloat(R.styleable.HiddenLayoutView_maxMovementFactorForSpring,2);
                Log.d(TAG,"Animation typed Array " +animationType);
                if (animationType == null || animationType.equals(""))
                    animationType = "2";
            } finally {
                typedArray.recycle();
            }
        } else {
            Log.d(TAG, "Attribute set is null ");
        }
    }

    public void closeRightHiddenView() {
        if (animation instanceof FlingAnimation) {
            Log.d(TAG,"Animation was fling ");
            ((FlingAnimation)animation).setStartVelocity(1000f).start();
            Log.d(TAG,"Started ANimation");
        }
        else if (animation instanceof SpringAnimation) {
            Log.d(TAG,"Animation was spring");
            springReverseAnim.start();
            Log.d(TAG,"Started Animation");
        }
        else {
            Log.e(TAG,"Did not recognize animation "+animation.getClass().getSimpleName());
        }
    }

    public View getInflatedUnderLayout() {
        return inflatedUnderLayout;
    }

    public View getInflatedOverLayout() {
        return inflatedOverLayout;
    }

    public DynamicAnimation getAnimationInEffect() {
        return animation;
    }

    public DynamicAnimation getReverseAnimationInEffect () {
        return reverseAnimation;
    }

    public void setOverLayoutEventListener(AnimationUpdateListeners.OverLayoutEventListener overLayoutEventListener1) {
        overLayoutEventListener = overLayoutEventListener1;
    }

    public void setUnderLayoutEventListener(AnimationUpdateListeners.UnderLayoutEventListener underLayoutEventListener1) {
        underLayoutEventListener = underLayoutEventListener1;
    }

    public AnimationUpdateListeners.OverLayoutEventListener getOverLayoutEventListener() {
        return overLayoutEventListener;
    }

    public AnimationUpdateListeners.UnderLayoutEventListener getUnderLayoutEventListener() {
        return underLayoutEventListener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void closeOpenHiddenView (){
        Log.i(TAG,"Closing View");
        closeRightHiddenView();
    }
}
