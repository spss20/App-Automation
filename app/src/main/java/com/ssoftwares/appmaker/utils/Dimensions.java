package com.ssoftwares.appmaker.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Dimensions {

    private static DisplayMetrics r;

    public static int get15dp(){
        init();
        int px15 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15,
                r
        );
        return px15;
    }

    private static void init() {
        if (r == null)
            r = Resources.getSystem().getDisplayMetrics();
    }

    public static int get70dp(){
        init();
        int px70 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                70,
               r
        );
        return px70;
    }

    public static int get20dp(){
        init();
        int px20 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                r
        );
        return px20;
    }
}
