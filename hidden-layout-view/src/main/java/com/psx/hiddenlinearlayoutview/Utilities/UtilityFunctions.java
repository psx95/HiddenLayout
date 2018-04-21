package com.psx.hiddenlinearlayoutview.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class UtilityFunctions {
    public static float distance(float x1, float y1, float x2, float y2, Context context) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(distanceInPx, context);
    }

    private static float pxToDp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
