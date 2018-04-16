package com.psx.hiddenlinearlayoutview.Utilities;

import android.content.Context;
import android.util.Log;

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

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        Log.i("UTILITY","Converted "+Math.round((float) dp * density));
        return Math.round((float) dp * density);
    }
}
