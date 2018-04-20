package com.psx.hiddenlinearlayoutview;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
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

public class HiddenLayoutView extends LinearLayout implements LifecycleObserver {

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
    public static final String ANIMATION_FLING = "2";
    public static final String ANIMATION_DRAG_SPRING = "1";
    public AnimationUpdateListeners.OverLayoutEventListener overLayoutEventListener;
    public AnimationUpdateListeners.UnderLayoutEventListener underLayoutEventListener;
    public AnimationUpdateListeners animationUpdateListeners;
    private float maxMovementFactor;
    private float flingFriction, flingFrictionReverse;

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
        setupAnimations(this.animationType);
    }

    private void setupListeners() {
        underLayoutEventListener = view -> {
        };
        overLayoutEventListener = view -> {
        };
        inflatedUnderLayout.setOnClickListener(v -> underLayoutEventListener.onUnderLayoutClickRecieved(v));
        inflatedOverLayout.setOnClickListener(v -> overLayoutEventListener.onOverLayoutClickRecieved(v));
    }

    private void initListeners() {
        overLayoutEventListener = (view) -> {
        };
        animationUpdateListeners = () -> {
            Log.d(TAG, "Max Pulled");
        };
    }

    private void setupAnimations(String animationType) {
        int animType = Integer.parseInt(animationType);
        switch (animType) {
            case 1:
                Log.d(TAG, "Spring Animation");
                SpringAnimationUtil springAnimationUtil = new SpringAnimationUtil(context, inflatedOverLayout,
                        revealViewPercentageRight, inflatedUnderLayout.findViewById(R.id.revealed_view_right),
                        scaleHiddenView, maxMovementFactor, this);
                SpringAnimation springAnimation = springAnimationUtil.getxAnimation();
                if (springAnimation == null)
                    Log.wtf(TAG, "SPringanimation is null");
                springReverseAnim = springAnimationUtil.getReverseXAnim();
                animation = springAnimation;
                reverseAnimation = springReverseAnim;
                break;
            case 2:
                FlingAnimationUtil flingAnimationUtil = new FlingAnimationUtil(context, inflatedOverLayout, revealViewPercentageRight, flingFriction, flingFrictionReverse, this);
                FlingAnimation flingAnimation = flingAnimationUtil.getFlingAnimation();
                flingReverseAnimation = flingAnimationUtil.getReverseFlingAnimation();
                animation = flingAnimation;
                reverseAnimation = flingReverseAnimation;
                break;
            default:
                Log.e(TAG, "Unable to resolve Animation " + animation.getClass().getSimpleName());
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
                maxMovementFactor = typedArray.getFloat(R.styleable.HiddenLayoutView_maxMovementFactorForSpring, 2);
                flingFriction = typedArray.getFloat(R.styleable.HiddenLayoutView_flingFriction, 0.8f);
                flingFrictionReverse = typedArray.getFloat(R.styleable.HiddenLayoutView_flingFrictionReverse, 0.001f);
                Log.d(TAG, "Animation typed Array " + animationType);
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
        if (reverseAnimation != null) {
            reverseAnimation.start();
        } else {
            Log.e(TAG, "Did not recognize animation " + animation.getClass().getSimpleName());
        }
    }

    public void changeAnimation(String animationType) {
        this.animationType = animationType;
        setupAnimations(animationType);
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

    public DynamicAnimation getReverseAnimationInEffect() {
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

    public void setAnimationUpdateListeners(AnimationUpdateListeners animationUpdateListeners) {
        this.animationUpdateListeners = animationUpdateListeners;
    }

    public void setDampingAndStiffnessForDragWithSpringForward(float damping, float stiffness) {
        setDampingAndStiffness(animation, damping, stiffness);
    }

    public void setDampingAndStiffnessForDragWithSpringReverse(float damping, float stiffness) {
        setDampingAndStiffness(reverseAnimation, damping, stiffness);
    }

    public void setFlingFriction(float friction) {
        if (animation instanceof FlingAnimation)
            ((FlingAnimation) animation).setFriction(friction);
    }

    public void setFlingReverseFriction(float friction) {
        if (reverseAnimation instanceof FlingAnimation)
            ((FlingAnimation) reverseAnimation).setFriction(friction);
    }

    private void setDampingAndStiffness(DynamicAnimation animation, float damping, float stiffness) {
        if (animation instanceof SpringAnimation) {
            ((SpringAnimation) animation).getSpring().setDampingRatio(damping);
            ((SpringAnimation) animation).getSpring().setStiffness(stiffness);
        }
    }

    public float getFlingFriction() {
        return flingFriction;
    }

    public float getFlingFrictionReverse() {
        return flingFrictionReverse;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void closeOpenHiddenView() {
        Log.i(TAG, "Closing View");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeRightHiddenView();
            }
        }, 200);
    }
}
