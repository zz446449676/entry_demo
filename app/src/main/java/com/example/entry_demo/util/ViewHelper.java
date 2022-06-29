package com.example.entry_demo.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;


/**
 * Helper used for handling view
 */
public class ViewHelper {

    private ViewHelper() {

    }

    public static void setViewHidden(View view, boolean hidden) {
        if (view == null)  return;
        view.setVisibility(hidden ? View.GONE : View.VISIBLE);
    }

    public static void setViewInvisible(View view, boolean invisible) {
        if (view == null) return;
        view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) getRawSize(context, TypedValue.COMPLEX_UNIT_DIP, dpValue);
    }

    public static int sp2px(Context context, float dpValue) {
        return (int) getRawSize(context, TypedValue.COMPLEX_UNIT_SP, dpValue);
    }



    /**
     * Gets the raw basic pixel size for current resolution
     * @param context
     * @param unit
     * @param size
     * @return
     */
    public static float getRawSize(Context context, int unit, float size) {
        return TypedValue.applyDimension(unit, size, context.getResources().getDisplayMetrics());
    }

}
