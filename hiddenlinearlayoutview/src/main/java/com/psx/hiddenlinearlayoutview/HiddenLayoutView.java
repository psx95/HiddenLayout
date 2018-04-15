package com.psx.hiddenlinearlayoutview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.animation.FlingAnimation;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

import com.psx.hiddenlinearlayoutview.Interfaces.AnimationUpdateListeners;
import com.psx.hiddenlinearlayoutview.Utilities.FlingAnimationUtil;

public class HiddenLayoutView extends LinearLayout{

    private static final String TAG = HiddenLayoutView.class.getSimpleName();
    private int layout_over;
    private int layout_under;
    private Context context;
    private View hiddenLayoutView;
    private ViewStub under, over;
    private View inflatedUnderLayout, inflatedOverLayout;
    private FlingAnimation flingAnimation;
    private int nestedTouchLevel;
    private float revealViewPercentageRight;
    public static AnimationUpdateListeners.OverLayoutEventListener overLayoutEventListener;
    public static AnimationUpdateListeners.UnderLayoutEventListener underLayoutEventListener;

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

    private void initView (AttributeSet attributeSet) {
        loadPreferencesFromAttributes(attributeSet);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater!=null) {
            Log.d(TAG,"Layout Inflater not null");
            hiddenLayoutView = layoutInflater.inflate(R.layout.hidded_layout,this);
            under = hiddenLayoutView.findViewById(R.id.under_layout);
            over = hiddenLayoutView.findViewById(R.id.over_layout);
            under.setLayoutResource(layout_under);
            over.setLayoutResource(layout_over);
            inflatedUnderLayout = under.inflate();
            inflatedOverLayout = over.inflate();
        } else {
            Log.d(TAG,"Layout inflater null");
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
        FlingAnimationUtil flingAnimationUtil = new FlingAnimationUtil(context, inflatedOverLayout, revealViewPercentageRight);
        flingAnimation = flingAnimationUtil.getFlingAnimation();
    }

    private void loadPreferencesFromAttributes(AttributeSet attributeSet) {
        if (attributeSet!=null) {
            Log.d(TAG,"Attribute set not null");
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.HiddenLayoutView);
            try {
                layout_over = typedArray.getResourceId(R.styleable.HiddenLayoutView_layout_over,R.layout.over_layout_default);
                layout_under = typedArray.getResourceId(R.styleable.HiddenLayoutView_layout_under,R.layout.under_layout_default);
                nestedTouchLevel = typedArray.getInt(R.styleable.HiddenLayoutView_nestedTouchDetectionLevel,1);
                revealViewPercentageRight = typedArray.getFloat(R.styleable.HiddenLayoutView_revealPercentageViewRight,0.2f);
            } finally {
                typedArray.recycle();
            }
        } else {
            Log.d(TAG,"Attribute set is null ");
        }
    }

    public void closeRightHiddenView() {
        flingAnimation.setStartVelocity(1000).start();
    }

    public View getInflatedUnderLayout() {
        return inflatedUnderLayout;
    }

    public View getInflatedOverLayout() {
        return inflatedOverLayout;
    }

    public FlingAnimation getFlingAnimation() {
        return flingAnimation;
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
}
