package com.ssoftwares.appmaker.utils;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.ssoftwares.appmaker.R;

public class SnackUtils {

    Context mContext;

    public SnackUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showMessageSnackBar(String message,
                                    View contextView,
                                    int errorType) {
        if (errorType == 0) {
            Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).setBackgroundTint(
                    mContext.getColor(R.color.colorPrimary)).setTextColor(mContext.getColor(R.color.white))
                    .show();
        } else {
            Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).setBackgroundTint(
                    mContext.getColor(android.R.color.holo_green_dark)).setTextColor(mContext.getColor(R.color.white))
                    .show();
        }

    }
}
