package com.ssoftwares.appmaker.fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.demoadapter.CustomSpinnerAdapter;
import com.ssoftwares.appmaker.demoutils.FontItem;
import com.ssoftwares.appmaker.interfaces.AddTextFragmentListener;

import java.util.ArrayList;
import java.util.List;


import yuku.ambilwarna.AmbilWarnaDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTextFragment extends BottomSheetDialogFragment {

    int colorSelected = Color.parseColor("#000000");

    AddTextFragmentListener listener;

    EditText edit_add_text;
    Button btn_done;
    Spinner fontStyleSpinner;
    ImageView colorImageView;
    RelativeLayout textEditLayout;
    Typeface typefaceSelected = Typeface.DEFAULT;
    int mDefaultColor;
    List<FontItem> fontItems;


    static AddTextFragment instance;
    private SeekBar spacingSeekbar;
    private SeekBar textsizeSeekbar;
    private TextView textViewTextSizeNumberView;
    private TextView textViewTextSize;
    private TextView textViewSpacingNumberView;
    private ImageView textAligmentImage;
    private ImageView textBoldImage;
    private ImageView textItalicImage;
    private ImageView textCapsImage;
    private RelativeLayout seekbarSpacingLayout;
    private RelativeLayout seekbarTextSizelayout;
    private int textAlligmentCount;
    private String textAligment="Center";
    private String textTyle="Bold";
    int textStyle;
    private float textSpacing=1;
    private int textSize=20;

    public static AddTextFragment getInstance() {
        if (instance == null)
            instance = new AddTextFragment();
        return instance;
    }

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    public AddTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview = inflater.inflate(R.layout.fragment_add_text, container, false);
        textEditLayout = (RelativeLayout) itemview.findViewById(R.id.textEditingLayout);
        textEditLayout.setVisibility(View.VISIBLE);
        edit_add_text = (EditText) itemview.findViewById(R.id.edt_add_text);
        btn_done = (Button) itemview.findViewById(R.id.buttonaddtext);
        spacingSeekbar = (SeekBar) itemview.findViewById(R.id.seekBarspacing);
        textsizeSeekbar = (SeekBar) itemview.findViewById(R.id.seekBartextsize);
//        textViewTextSize = (TextView) itemview.findViewById(R.id.fontsize);
        textViewSpacingNumberView = (TextView) itemview.findViewById(R.id.textViewspacingnumber);
        textViewTextSizeNumberView = (TextView) itemview.findViewById(R.id.textViewtextsizenumber);
        textAligmentImage = (ImageView) itemview.findViewById(R.id.textaligment);
        textBoldImage = (ImageView) itemview.findViewById(R.id.textbold);
        textItalicImage = (ImageView) itemview.findViewById(R.id.textitalic);
        textCapsImage = (ImageView) itemview.findViewById(R.id.textcaps);
        colorImageView = (ImageView) itemview.findViewById(R.id.colorpallete);

        seekbarSpacingLayout = (RelativeLayout) itemview.findViewById(R.id.spacingSeekbarLayout);
        seekbarTextSizelayout = (RelativeLayout) itemview.findViewById(R.id.textsizelayout);

        fontStyleSpinner = (Spinner) itemview.findViewById(R.id.spinnerfonttype);
        final String fontStyle[] = {"Select Font Style", "Abel-Regular", "Acme-Regular", "AlfaSlabOne-Regular", "Anton-Regular", "BlackHanSans-Regular"};
        fontItems = new ArrayList<FontItem>();
        for (int i = 0; i < fontStyle.length; i++) {

            FontItem item = new FontItem(fontStyle[i]);
            fontItems.add(item);
        }
        CustomSpinnerAdapter spinnerArrayAdapter = new CustomSpinnerAdapter(getContext(),
                R.layout.spinneritem, R.id.spinnerTextview, fontItems);// The drop down view
        fontStyleSpinner.setAdapter(spinnerArrayAdapter);
        fontStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    String selctedFont = fontStyle[position];///FontStyle
                    typefaceSelected = Typeface.createFromAsset(getContext().getAssets(), new StringBuilder("fonts/")
                            .append(selctedFont + ".ttf").toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        textViewTextSize.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                seekbarTextSizelayout.setVisibility(View.VISIBLE);
//
//            }
//        });
        textsizeSeekbar.setMax(100);
        textsizeSeekbar.setProgress(14);
        textsizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewTextSizeNumberView.setText(String.valueOf(progress));
                // textViewTextSize.setText(String.valueOf(progress));
               textSize = progress;//////Size of text

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // seekbarTextSizelayout.setVisibility(View.GONE);
            }
        });
        spacingSeekbar.setMax(100);
        spacingSeekbar.setProgress(14);
        spacingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSpacingNumberView.setText(String.valueOf(progress));
                // textViewSpacing.setText(String.valueOf(progress));
                textSpacing = progress;//////Text Spacing

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //seekbarSpacingLayout.setVisibility(View.GONE);

            }
        });
        mDefaultColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        colorImageView.setBackgroundColor(mDefaultColor);
        colorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultColor = color;
                        colorImageView.setBackgroundColor(mDefaultColor);

                    }
                });
                colorPicker.show();


                //// oolor Pallete dalni hai


            }
        });
        if (textAlligmentCount == 0) {
            //textAligmentImage.setImageResource("");/////RightSideAligmentImage
            // textAlligmentCount = 1;
        } else if (textAlligmentCount == 1) {
            //////leftSideAligmentImage
            // textAlligmentCount = 2;
        } else if (textAlligmentCount == 2) {
            /////textAlligmentCenterImage
            // textAlligmentCount = 0;
        }
        textAligmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textAlligmentCount == 0) {
                    ///setImageofLeftAligment
                    textAlligmentCount = 1;
                    textAligment = "Left";
                } else if (textAlligmentCount == 1) {
                    textAlligmentCount = 2;
                    textAligment = "Center";
                } else if (textAlligmentCount == 2) {
                    textAlligmentCount = 0;
                    textAligment = "Right";
                }


            }
        });
        textBoldImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               textTyle="Bold";
              // textItalicImage.setImageResource();


            }
        });
        textItalicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTyle="Italic";

            }
        });
        textCapsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTyle="Caps";

            }
        });
       // listener.OnAddTextButtonClick(typefaceSelected, edit_add_text.getText().toString(), mDefaultColor, "Bold", Gravity.CENTER, 5, 20);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                listener.OnAddTextButtonClick(typefaceSelected, edit_add_text.getText().toString(), mDefaultColor,textTyle,textAligment,textSpacing,textSize);
            }
        });


        return itemview;
    }

    private void openColorDialog() {

    }

}
