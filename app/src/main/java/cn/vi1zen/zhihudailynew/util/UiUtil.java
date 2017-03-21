package cn.vi1zen.zhihudailynew.util;


import cn.vi1zen.zhihudailynew.App;


public class UiUtil {

    /** dip转换px */
    public static int dp2px(int dp) {
        final float scale = App.app.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    /** px转换dip */
    public static int px2dp(int px) {
        final float scale = App.app.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
