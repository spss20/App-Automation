package com.ssoftwares.appmaker.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.interfaces.EditImageFragmentListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    SeekBar seekBar_brightness, seekBar_constrant, seekBar_saturation;

    static EditImageFragment instance;

    public static EditImageFragment getInstance() {
        if (instance == null) {
            instance = new EditImageFragment();
        }
        return instance;
    }

    public EditImageFragment() {
        // Required empty public constructor
    }

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview = inflater.inflate(R.layout.fragment_editimage, container, false);
        seekBar_brightness = (SeekBar) itemview.findViewById(R.id.seekbar_brighness);
        seekBar_constrant = (SeekBar) itemview.findViewById(R.id.seekbar_contrast);
        seekBar_saturation = (SeekBar) itemview.findViewById(R.id.seekbar_saturation);
        seekBar_brightness.setMax(200);
        seekBar_brightness.setProgress(100);

        seekBar_constrant.setMax(20);
        seekBar_constrant.setProgress(0);

        seekBar_saturation.setMax(30);
        seekBar_saturation.setProgress(10);

        seekBar_saturation.setOnSeekBarChangeListener(this);
        seekBar_constrant.setOnSeekBarChangeListener(this);
        seekBar_brightness.setOnSeekBarChangeListener(this);

        return itemview;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {
            if (seekBar.getId() == R.id.seekbar_brighness) {
                listener.onBrightnessChanged(progress - 100);
            } else if (seekBar.getId() == R.id.seekbar_contrast) {
                progress += 10;
                float value = .10f * progress;
                listener.onConstrantChanged(value);

            } else if (seekBar.getId() == R.id.seekbar_saturation) {
                float value = .10f * progress;
                listener.onSaturationChanged(value);
            }
        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener != null) {
            listener.onEditStarted();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null)
            listener.onEditCompleted();
    }

    public void resetControls() {
        seekBar_brightness.setProgress(100);
        seekBar_constrant.setProgress(0);
        seekBar_saturation.setProgress(10);
    }
}
