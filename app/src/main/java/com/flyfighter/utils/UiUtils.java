package com.flyfighter.utils;

import android.content.Context;
import android.util.TypedValue;

public class UiUtils {

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, int dp) {
        float v = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * dp + 0.5f);
    }
}
