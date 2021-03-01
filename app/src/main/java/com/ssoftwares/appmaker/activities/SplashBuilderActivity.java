package com.ssoftwares.appmaker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.demoadapter.CustomSpinnerAdapter;
import com.ssoftwares.appmaker.demoadapter.ViewPagerAdapter;
import com.ssoftwares.appmaker.demoutils.Bitmap_Utils;
import com.ssoftwares.appmaker.demoutils.FontItem;
import com.ssoftwares.appmaker.fragments.AddTextFragment;
import com.ssoftwares.appmaker.fragments.EditImageFragment;
import com.ssoftwares.appmaker.fragments.FilterListFragment;
import com.ssoftwares.appmaker.listeners.AddTextFragmentListener;
import com.ssoftwares.appmaker.listeners.EditImageFragmentListener;
import com.ssoftwares.appmaker.listeners.FiltersListFragmentListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;
import yuku.ambilwarna.AmbilWarnaDialog;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

public class SplashBuilderActivity extends AppCompatActivity implements FiltersListFragmentListener,
        EditImageFragmentListener, AddTextFragmentListener {
    public static final String picture_name = "flash.png";

    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_INSERT_IMAGE = 1001;
    private static final int MULTIPLE_PERMISSIONS = 5;

    PhotoEditorView phoroEditorView;

    CoordinatorLayout coordinatorLayout;
    Bitmap originalBitmap, filteredBitmap, finalBitmap;

    FilterListFragment filterListFragment;
    EditImageFragment editImageFragment;
    CardView btn_filter_list, bnt_edit, btn_brush, btn_emoji, btn_add_text, btn_add_image, btn_add_frame, btn_crop;


    PhotoEditor photoEditor;


    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float constrantFinal = 1.0f;

    Uri image_selected_uri;
    EditText myEditText;
    float dX, dY;


    //load native image filter lib

    static {
        System.loadLibrary("NativeImageProcessor");

    }

    RelativeLayout topbarEditor;
    ImageView open_Image, save_image, undo_Image;
    private ImageView button;
    AddTextFragmentListener listener;

    EditText edit_add_text;
    Button btn_done, saveToGalleryButton;
    RelativeLayout viewLaySplash;
    Spinner fontStyleSpinner;
    ImageView colorImageView;
    RelativeLayout textEditLayout;
    Typeface typefaceSelected = Typeface.DEFAULT;
    int mDefaultColor, defaultBackColor;
    ArrayList<FontItem> fontItems;
    ImageView closeAddText;


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
    private String textAligment = "Center";
    private String textTyle = "Bold";
    int textStyle;
    private float textSpacing = 1;
    private int textSize = 20;

    RelativeLayout addTextLayout;
    HorizontalScrollView bottomToolScrollView;
    private LayoutInflater factory;
    private View myView;
    private TextView textView;
    int textBold = 0, textItalic = 0, textUnderLine = 0;
    ImageView imageViewPreview, imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_builder);
        topbarEditor = (RelativeLayout) findViewById(R.id.topbar_editor);
        open_Image = (ImageView) findViewById(R.id.imageViewopen);
        save_image = (ImageView) findViewById(R.id.imageViewsave);
        undo_Image = (ImageView) findViewById(R.id.imageViewundo);
        addTextLayout = (RelativeLayout) findViewById(R.id.addtextLayout);
        imageViewPreview = findViewById(R.id.previewImage);
        saveToGalleryButton = findViewById(R.id.saveToGallery);
        viewLaySplash = findViewById(R.id.viewLaySplash);
        viewLaySplash.setVisibility(View.GONE);
        addTextLayout.setVisibility(View.GONE);
        bottomToolScrollView = (HorizontalScrollView) findViewById(R.id.scrollingLayout);
        bottomToolScrollView.setVisibility(View.VISIBLE);
        imageViewBack = findViewById(R.id.imageView3);

        //view
        phoroEditorView = (PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, phoroEditorView)
                .setPinchTextScalable(true)
                .build();
        phoroEditorView.getSource().setBackgroundColor(getColor(R.color.black));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
//        bnt_edit = (CardView) findViewById(R.id.btn_edit);
        btn_filter_list = (CardView) findViewById(R.id.btn_filters_list);
        // btn_brush = (CardView) findViewById(R.id.btn_brunsh);

        // btn_emoji = (CardView) findViewById(R.id.btn_emoji);
        btn_add_text = (CardView) findViewById(R.id.btn_add_text);
        btn_add_image = (CardView) findViewById(R.id.btn_add_image);

        //btn_add_frame = (CardView) findViewById(R.id.btn_add_frame);

        // btn_crop = (CardView) findViewById(R.id.btn_crop);
        open_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFromGallery();
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                saveImageFromGallery();
            }
        });
        undo_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });


//        btn_crop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startCrop(image_selected_uri);
//            }
//        });

        btn_filter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (filterListFragment != null) {
//
//                    filterListFragment.show(getSupportFragmentManager(),
//                            filterListFragment.getTag());
//                } else {
//                    FilterListFragment filterListFragment = (FilterListFragment)
//                            FilterListFragment.getInstance(null);
//                    filterListFragment.setListener(SplashBuilderActivity.this);
//                    filterListFragment.show(getSupportFragmentManager(), filterListFragment.getTag());
//                }
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(SplashBuilderActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        defaultBackColor = color;
                        phoroEditorView.setBackgroundColor(defaultBackColor);
                        phoroEditorView.getSource().setBackgroundColor(defaultBackColor);


                    }
                });
                colorPicker.show();

            }
        });

//        bnt_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditImageFragment editImageFragment = EditImageFragment.getInstance();
//                editImageFragment.setListener(SplashBuilderActivity.this);
//                editImageFragment.show(getSupportFragmentManager(), editImageFragment.getTag());
//            }
//        });
        imageViewPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveToGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SplashBuilderActivity.this, "Save Image to gallery", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(SplashBuilderActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                java.io.File file = new java.io.File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator
                        + System.currentTimeMillis() + ".png");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)

                        .setTransparencyEnabled(true)
                        .build();
                photoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        Uri mSaveImageUri = Uri.fromFile(new File(imagePath));
                        //phoroEditorView.getSource().setImageURI(mSaveImageUri);
                        Toast.makeText(SplashBuilderActivity.this, "Saved Successfully to " + imagePath, Toast.LENGTH_SHORT).show();

                        //  Toast.makeText(SplashBuilderActivity.this, "" + imagePath, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SplashBuilderActivity.this, "" + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


//        btn_brush.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                photoEditor.setBrushDrawingMode(true);
//                BrushFragment brushFragment = BrushFragment.getInstance();
//                brushFragment.setListener(EditingActivity.this);
//                brushFragment.show(getSupportFragmentManager(), brushFragment.getTag());
//            }
//        });

//        btn_emoji.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EmojiFragment emojiFragment = EmojiFragment.getInstance();
//                emojiFragment.setListener(EditingActivity.this);
//                emojiFragment.show(getSupportFragmentManager(), emojiFragment.getTag());
//            }
//        });
        factory = LayoutInflater.from(SplashBuilderActivity.this);
        myView = factory.inflate(R.layout.checkforaddtext, null);
        textView = (TextView) myView.findViewById(R.id.textView9);
        button = (ImageView) myView.findViewById(R.id.button);

        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTextLayout.setVisibility(View.VISIBLE);
                bottomToolScrollView.setVisibility(View.GONE);
                phoroEditorView.removeView(myView);
                phoroEditorView.addView(myView);


            }

        });


        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageToPicture();
            }
        });

//        btn_add_frame.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FrameFragment frameFragment = FrameFragment.getInstance();
//                frameFragment.setListener(EditingActivity.this);
//                frameFragment.show(getSupportFragmentManager(), frameFragment.getTag());
//            }
//        });


//        loadImage();
        loadImage(R.drawable.splash_demo_image);

        textEditLayout = (RelativeLayout) findViewById(R.id.textEditingLayout);
        closeAddText = (ImageView) findViewById(R.id.closeButton);
        closeAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextLayout.setVisibility(View.GONE);
                bottomToolScrollView.setVisibility(View.VISIBLE);
                // phoroEditorView.removeView(myView);
            }
        });

        textEditLayout.setVisibility(View.VISIBLE);
        edit_add_text = (EditText) findViewById(R.id.edt_add_text);
        btn_done = (Button) findViewById(R.id.buttonaddtext);
        spacingSeekbar = (SeekBar) findViewById(R.id.seekBarspacing);
        textsizeSeekbar = (SeekBar) findViewById(R.id.seekBartextsize);
//        textViewTextSize = (TextView) itemview.findViewById(R.id.fontsize);
        textViewSpacingNumberView = (TextView) findViewById(R.id.textViewspacingnumber);
        textViewTextSizeNumberView = (TextView) findViewById(R.id.textViewtextsizenumber);
        textAligmentImage = (ImageView) findViewById(R.id.textaligment);
        textBoldImage = (ImageView) findViewById(R.id.textbold);
        textItalicImage = (ImageView) findViewById(R.id.textitalic);
        textCapsImage = (ImageView) findViewById(R.id.textcaps);
        colorImageView = (ImageView) findViewById(R.id.colorpallete);

        seekbarSpacingLayout = (RelativeLayout) findViewById(R.id.spacingSeekbarLayout);
        seekbarTextSizelayout = (RelativeLayout) findViewById(R.id.textsizelayout);

        fontStyleSpinner = (Spinner) findViewById(R.id.spinnerfonttype);
        edit_add_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView.setText(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final String fontStyle[] = {"Select Font Style", "Abel-Regular", "Acme-Regular", "AlfaSlabOne-Regular", "Anton-Regular", "BlackHanSans-Regular"};
        fontItems = new ArrayList<FontItem>();
        for (int i = 0; i < fontStyle.length; i++) {

            FontItem item = new FontItem(fontStyle[i]);
            fontItems.add(item);
        }
        CustomSpinnerAdapter spinnerArrayAdapter = new CustomSpinnerAdapter(
                SplashBuilderActivity.this, R.layout.spinneritem, R.id.spinnerTextview, fontItems);// The drop down view
        fontStyleSpinner.setAdapter(spinnerArrayAdapter);
        fontStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    String selctedFont = fontStyle[position];///FontStyle
                    typefaceSelected = Typeface.createFromAsset(getAssets(), new StringBuilder("fonts/")
                            .append(selctedFont + ".ttf").toString());
                    textView.setTypeface(typefaceSelected);
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
                textView.setTextSize(textSize);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // seekbarTextSizelayout.setVisibility(View.GONE);
            }
        });
        spacingSeekbar.setMax(10);
        spacingSeekbar.setProgress(0);
        spacingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSpacingNumberView.setText(String.valueOf(progress));
                // textViewSpacing.setText(String.valueOf(progress));
                textSpacing = progress;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textView.setLetterSpacing(textSpacing);/////Text Spacing
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //seekbarSpacingLayout.setVisibility(View.GONE);

            }
        });
        mDefaultColor = ContextCompat.getColor(SplashBuilderActivity.this, R.color.colorPrimary);
        colorImageView.setBackgroundColor(mDefaultColor);
        colorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(SplashBuilderActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultColor = color;
                        colorImageView.setBackgroundColor(mDefaultColor);

                        textView.setTextColor(mDefaultColor);

                    }
                });
                colorPicker.show();


                //// oolor Pallete dalni hai


            }
        });
        if (textAlligmentCount == 0) {
            textAligmentImage.setImageResource(R.drawable.ic_baseline_format_align_left_24);/////RightSideAligmentImage
            //textAlligmentCount = 1;
        } else if (textAlligmentCount == 1) {
            textAligmentImage.setImageResource(R.drawable.ic_baseline_format_align_right_24);/////RightSideAligmentImage

            //////leftSideAligmentImage
            // textAlligmentCount = 2;
        } else if (textAlligmentCount == 2) {
            /////textAlligmentCenterImage
            // textAlligmentCount = 0;
            textAligmentImage.setImageResource(R.drawable.ic_baseline_format_align_center_24);/////RightSideAligmentImage

        }
        textAligmentImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                if (textAlligmentCount == 0) {
                    ///setImageofLeftAligment
                    textAlligmentCount = 1;
                    textAligment = "Right";
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    textAligmentImage.setImageResource(R.drawable.ic_baseline_format_align_right_24);


                } else if (textAlligmentCount == 1) {
                    textAlligmentCount = 2;
                    textAligment = "Center";
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textAligmentImage.setImageResource(R.drawable.ic_baseline_format_align_center_24);


                } else if (textAlligmentCount == 2) {
                    textAlligmentCount = 0;
                    textAligment = "Left";
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    textAligmentImage.setImageResource(R.drawable.ic_baseline_format_align_left_24);


                }


            }
        });

        textBoldImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textBold == 0) {
                    textTyle = "Bold";
                    textView.setTypeface(typefaceSelected, Typeface.BOLD);
                    textBold = 1;
                    textBoldImage.setColorFilter(ContextCompat.getColor(SplashBuilderActivity.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (textBold == 1) {
                    textBold = 0;
                    textView.setTypeface(typefaceSelected, Typeface.NORMAL);
                    textBoldImage.setColorFilter(ContextCompat.getColor(SplashBuilderActivity.this, R.color.selectedfilter), android.graphics.PorterDuff.Mode.SRC_IN);


                }//textBoldImage.setImageResource(R.drawable.ic_emoji_symbols_light);
                // textItalicImage.setImageResource();


            }
        });
        textItalicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textItalic == 0) {
                    textTyle = "Italic";
                    textView.setTypeface(typefaceSelected, Typeface.ITALIC);
                    textItalic = 1;
                    textItalicImage.setColorFilter(ContextCompat.getColor(SplashBuilderActivity.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (textItalic == 1) {
                    textItalic = 0;
                    textView.setTypeface(typefaceSelected, Typeface.NORMAL);
                    textItalicImage.setColorFilter(ContextCompat.getColor(SplashBuilderActivity.this, R.color.selectedfilter), android.graphics.PorterDuff.Mode.SRC_IN);


                }


            }
        });
        textCapsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textUnderLine == 0) {
                    textTyle = "Caps";
                    textUnderLine = 1;
                    textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    textCapsImage.setColorFilter(ContextCompat.getColor(SplashBuilderActivity.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (textUnderLine == 1) {
                    textView.setPaintFlags(0);
                    textUnderLine = 0;
                    textCapsImage.setColorFilter(ContextCompat.getColor(SplashBuilderActivity.this, R.color.selectedfilter), android.graphics.PorterDuff.Mode.SRC_IN);


                }


            }
        });


//        textView.setTextSize(textSize);
//        if (textAligment.equals("Center")){
//            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
//
//        }
//        else if (textAligment.equals("Left")){
//            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
//        }
//        else if (textAligment.equals("Right")){
//            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
//        }
//
//
//        if (textTyle.equals("Bold")){
//            textView.setTypeface(typefaceSelected,Typeface.BOLD);
//
//        }
//        else if(textTyle.equals("Caps")){
//            String capsText=edit_add_text.getText().toString().toUpperCase();
//            textView.setTypeface(typefaceSelected);
//            textView.setText(capsText);
//
//        }
//
//        else if(textTyle.equals("Italic")){
//            textView.setTypeface(typefaceSelected,Typeface.ITALIC);
//
//        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            textView.setLetterSpacing(textSpacing);
//        }

//        myEditText = new EditText(EditingActivity.this);
//        RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        myEditText.setLayoutParams(mRparams);
//        phoroEditorView.addView(myEditText);


        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                button.setVisibility(View.VISIBLE);
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

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoroEditorView.removeView(myView);
                bottomToolScrollView.setVisibility(View.VISIBLE);
                addTextLayout.setVisibility(View.GONE);
            }
        });


//        listener.OnAddTextButtonClick(typefaceSelected, edit_add_text.getText().toString(), mDefaultColor,textTyle,textAligment,textSpacing,textSize);
//
//         listener.OnAddTextButtonClick(typefaceSelected, edit_add_text.getText().toString(), mDefaultColor, "Bold", Gravity.CENTER, 5, 20);

//        btn_done.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onClick(View v) {
//                listener.OnAddTextButtonClick(typefaceSelected, edit_add_text.getText().toString(), mDefaultColor,textTyle,textAligment,textSpacing,textSize);
//            }
//        });


    }

    @Override
    public void OnAddTextButtonClick(Typeface typeface, String text, int color, String textStyle, String textalligment, float spacing, int textsize) {

        //photoEditor.addText(typeface, text, color);
        LayoutInflater factory = LayoutInflater.from(this);
        final View myView = factory.inflate(R.layout.checkforaddtext, null);
        TextView textView = (TextView) myView.findViewById(R.id.textView9);
        button = (ImageView) myView.findViewById(R.id.button);

        textView.setTextSize(textsize);
        if (textalligment.equals("Center")) {
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);

        } else if (textalligment.equals("Left")) {
            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        } else if (textalligment.equals("Right")) {
            textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        }
        textView.setText(text);
        textView.setTextColor(color);


        if (textStyle.equals("Bold")) {
            textView.setTypeface(typeface, Typeface.BOLD);


        } else if (textStyle.equals("Caps")) {
            String capsText = text.toUpperCase();
            textView.setTypeface(typeface);
            textView.setText(capsText);

        } else if (textStyle.equals("Italic")) {
            textView.setTypeface(typeface, Typeface.ITALIC);

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setLetterSpacing(spacing);
        }
        phoroEditorView.addView(myView);

//        myEditText = new EditText(EditingActivity.this);
//        RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        myEditText.setLayoutParams(mRparams);
//        phoroEditorView.addView(myEditText);

        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                button.setVisibility(View.VISIBLE);
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

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoroEditorView.removeView(myView);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void startCrop(Uri uri) {
        UCrop.Options options = new UCrop.Options();
//        options.setCropFrameColor(getResources().getColor(R.color.colorAccent));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        //options.setToolbarWidgetColor(getResources().getColor(R.color.colorgreen));
        options.setActiveControlsWidgetColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setRootViewBackgroundColor(getResources().getColor(R.color.selectedfilter));

//        options.setFreeStyleCropEnabled(true);
//        options.setToolbarCancelDrawable(R.drawable.aboutus);
//        options.setToolbarCropDrawable(R.drawable.ic_template);
        //options.setCropGridColor(getResources().getColor(R.color.colorPrimary));
        // options.setDimmedLayerColor(getResources().getColor(R.color.colorPrimary));

//        String destinationFileName = new StringBuilder(UUID.randomUUID().toString())
//                .append(".jpg").toString();
//        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(),
//                destinationFileName)));
//        uCrop.withOptions(options);
//        uCrop.start(SplashBuilderActivity.this);

        //  uCrop= new UCrop.Options().setStatusBarColor(getResources().getColor(R.color.colorPrimary));


    }

    private void addImageToPicture() {
        if (ContextCompat.checkSelfPermission(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(SplashBuilderActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(SplashBuilderActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MULTIPLE_PERMISSIONS);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MULTIPLE_PERMISSIONS);

            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PERMISSION_INSERT_IMAGE);


        }

//        Dexter.withActivity(this)
//                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if(report.areAllPermissionsGranted()){
//                            Intent intent=new Intent(Intent.ACTION_PICK);
//                            intent.setType("image/*");
//                            startActivityForResult(intent,PERMISSION_INSERT_IMAGE);
//                        }
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
//                        Toast.makeText(EditingActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }).check();
    }

    private void loadImage(int image) {

        originalBitmap = Bitmap_Utils.decodeSampledBitmapFromResource(getResources(), image,
                800, 1700);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

        phoroEditorView.getSource().setImageBitmap(originalBitmap);
        phoroEditorView.setBackgroundColor(getColor(R.color.black));
        phoroEditorView.getSource().setBackgroundColor(getColor(R.color.black));


    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        filterListFragment = new FilterListFragment();
        filterListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);


        adapter.addFragment(filterListFragment, "FILTERS");
        adapter.addFragment(editImageFragment, "EDIT");

        viewPager.setAdapter(adapter);

    }


    @Override
    public void onBrightnessChanged(int brightness) {

        brightnessFinal = brightness;
        Filter myfilter = new Filter();
        myfilter.addSubFilter(new BrightnessSubFilter(brightness));
        phoroEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));

    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myfilter = new Filter();
        myfilter.addSubFilter(new SaturationSubfilter(saturation));
        phoroEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));

    }

    @Override
    public void onConstrantChanged(float constrant) {
        constrantFinal = constrant;
        Filter myfilter = new Filter();
        myfilter.addSubFilter(new ContrastSubFilter(constrant));
        phoroEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));

    }

    @Override
    public void onEditStarted() {


    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Filter myFilter = new Filter();

        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));

        finalBitmap = myFilter.processFilter(bitmap);

    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        phoroEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);


    }

    private void resetControl() {
        if (editImageFragment != null) {
            editImageFragment.resetControls();
        }
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        constrantFinal = 1.0f;


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return true;
//    }

    //
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_open) {
//            openImageFromGallery();
//            return true;
//        }
//
//        if (id == R.id.action_save) {
//            saveImageFromGallery();
//            return true;
//        }
//        if (id==R.id.action_undo){
//            undo();
//            resetControl();
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
    public void undo() {
        photoEditor.undo();
    }

    private void saveImageFromGallery() {
        if (ContextCompat.checkSelfPermission(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(SplashBuilderActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(SplashBuilderActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MULTIPLE_PERMISSIONS);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MULTIPLE_PERMISSIONS);

            }
        } else {

            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(Bitmap saveBitmap) {

                    Log.d("TAG", "onBitmapReady: " + saveBitmap);
                    imageViewPreview.setImageBitmap(saveBitmap);
                    undo_Image.setVisibility(View.GONE);
                    save_image.setVisibility(View.GONE);
                    viewLaySplash.setVisibility(View.VISIBLE);
                    imageViewPreview.setVisibility(View.VISIBLE);
                    bottomToolScrollView.setVisibility(View.GONE);
                    imageViewBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            undo_Image.setVisibility(View.VISIBLE);
                            save_image.setVisibility(View.VISIBLE);
                            viewLaySplash.setVisibility(View.GONE);
                            imageViewPreview.setVisibility(View.GONE);
                            photoEditor.undo();
                            bottomToolScrollView.setVisibility(View.VISIBLE);
                            phoroEditorView.removeAllViews();
                            imageViewBack.setOnClickListener(new
                                                                     View.OnClickListener() {
                                                                         @Override
                                                                         public void onClick(View v) {
                                                                             finish();
                                                                         }
                                                                     });

                        }
                    });
//                        final String path =
//                                Bitmap_Utils.insertImage(getContentResolver(), saveBitmap
//                                , System.currentTimeMillis() + "_profile.jpg", null);
//                        if (!TextUtils.isEmpty(path)) {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Image Save to gallery",
//                                    Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    openImage(path);
//                                    phoroEditorView.removeView(myView);
//
//                                }
//                            });
//                            snackbar.show();
//
//                        } else {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Unable to save Image",
//                                    Snackbar.LENGTH_LONG);
//                            snackbar.show();
//
//                        }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });


        }


//        Dexter.withActivity(this)
//                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if(report.areAllPermissionsGranted()){
//                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
//                                @Override
//                                public void onBitmapReady(Bitmap saveBitmap) {
//                                    try {
//
//                                        phoroEditorView.getSource().setImageBitmap(saveBitmap);
//                                        final String path=Bitmap_Utils.insertImage(getContentResolver(),saveBitmap
//                                                ,System.currentTimeMillis()+"_profile.jpg",null);
//                                        if (!TextUtils.isEmpty(path)) {
//                                            Snackbar snackbar=Snackbar.make(coordinatorLayout,"Image Save to gallery",
//                                                    Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    openImage(path);
//
//                                                }
//                                            });
//                                            snackbar.show();
//
//                                        }
//                                        else {
//                                            Snackbar snackbar=Snackbar.make(coordinatorLayout,"Unable to save Image",
//                                                    Snackbar.LENGTH_LONG);
//                                            snackbar.show();
//
//                                        }
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Exception e) {
//
//                                }
//                            });
//                        }else {
//                            Toast.makeText(EditingActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();


    }

    private void openImage(String path) {
        if (ContextCompat.checkSelfPermission(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(SplashBuilderActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(SplashBuilderActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MULTIPLE_PERMISSIONS);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MULTIPLE_PERMISSIONS);

            }
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "image/*");
            startActivity(intent);


        }


    }

    private void openImageFromGallery() {

        if (ContextCompat.checkSelfPermission(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(SplashBuilderActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SplashBuilderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(SplashBuilderActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MULTIPLE_PERMISSIONS);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MULTIPLE_PERMISSIONS);

            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PERMISSION_PICK_IMAGE);


        }

//        Dexter.withActivity(this)
//                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if(report.areAllPermissionsGranted()){
//
//                        }else {
//                            Toast.makeText(EditingActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writePermission && readExternalFile) {
                        // write your logic here
                    } else {
                        Snackbar.make(SplashBuilderActivity.this.findViewById(android.R.id.content),
                                "Please Grant Permissions",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(SplashBuilderActivity.this,
                                                new String[]{Manifest.permission
                                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                                MULTIPLE_PERMISSIONS);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE) {

                Bitmap bitmap = Bitmap_Utils.getBitmapFromGallery(this, data.getData(), 800, 800);

                image_selected_uri = data.getData();

                //clear bitmap memory
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();
                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                phoroEditorView.getSource().setImageBitmap(originalBitmap);

                bitmap.recycle();

                //fix crush

                filterListFragment = (FilterListFragment) FilterListFragment.getInstance(originalBitmap);
                filterListFragment.setListener(this);

            } else if (requestCode == PERMISSION_INSERT_IMAGE) {
                Bitmap bitmap = Bitmap_Utils.getBitmapFromGallery(this, data.getData(), 300, 300);
                photoEditor.addImage(bitmap);

            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (requestCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            }
        }
    }

    private void handleCropError(Intent data) {
        final Throwable cropError = UCrop.getError(data);
        if (cropError != null) {
            Toast.makeText(this, "" + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri = UCrop.getOutput(data);
        if (resultUri != null) {
            phoroEditorView.getSource().setImageURI(resultUri);

        } else {
            Toast.makeText(this, "Cant retrive crop image", Toast.LENGTH_SHORT).show();
        }

    }

}