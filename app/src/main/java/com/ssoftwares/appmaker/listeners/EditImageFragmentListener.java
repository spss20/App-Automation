package com.ssoftwares.appmaker.listeners;

/**
 * Created by mahmoud on 21/07/18.
 */

public interface EditImageFragmentListener {

    void onBrightnessChanged(int brightness);
    void onSaturationChanged(float saturation);
    void onConstrantChanged(float constrant);
    void onEditStarted();
    void onEditCompleted();


}
