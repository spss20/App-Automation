package com.ssoftwares.appmaker.listeners;

import android.graphics.Typeface;

/**
 * Created by mahmoud on 3/09/18.
 */

public interface AddTextFragmentListener {

    void OnAddTextButtonClick(Typeface typeface, String text, int color, String textStyle, String textalligment, float spacing, int textsize);
}
