package com.qixiang.codetoy.Util;

import android.content.Context;

public class DpUtils {

    /**
     * dp转换为px
     *
     * @param context
     * @param value 单位dp
     * @return
     */
    public static int dp2px(Context context, int value) {
        float v = context.getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    /**
     * sp转换为px
     *
     * @param context
     * @param value 单位sp
     * @return
     */
    public static int sp2px(Context context, int value) {
        float v = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

    /**
     * px转换为dp
     *
     * @param context
     * @param value
     * @return
     */
    public static int px2dp(Context context, int value) {
        float v = context.getResources().getDisplayMetrics().density;
        return (int) (value / v + 0.5f);
    }

    /**
     * px转换为sp
     *
     * @param context
     * @param value
     * @return
     */
    public static int px2sp(Context context, int value) {
        float v = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / v + 0.5f);
    }
}
