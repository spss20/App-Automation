package com.ssoftwares.appmaker.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ssoftwares.appmaker.R;

public class GalleryFragment extends Fragment {
    float dX, dY;
    TextView text;
    Button addtext;
    RelativeLayout mRlayout;
    EditText myEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mRlayout = (RelativeLayout) view.findViewById(R.id.myGalleryLayout);
        addtext = (Button) view.findViewById(R.id.addtext);
        RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myEditText = new EditText(getContext());
        myEditText.setLayoutParams(mRparams);

        addtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRlayout.addView(myEditText);

            }
        });

        myEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true; // Don't miss to return as true
            }
        });

        return view;
    }
}
