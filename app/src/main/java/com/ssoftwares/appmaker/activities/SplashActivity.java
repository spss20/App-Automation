package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.mukesh.OtpView;
import com.ssoftwares.appmaker.BuildConfig;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.utils.SessionManager;
import com.ssoftwares.appmaker.utils.SnackUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ApiService service;
    String versionName;
    BottomSheetDialog bottomSheetDialog;

    Dialog dialogMaintains;
    SnackUtils snackUtils;
    RelativeLayout mainSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        service = ApiClient.create();
        mainSplash = findViewById(R.id.mainSplash);

        getSettings();

    }

    public void showUpdateDialog() {
        bottomSheetDialog =
                new BottomSheetDialog(SplashActivity.this,
                        R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.layout_update_dialog);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        //  bottomSheetDialog.set(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button buttonSubmit = (Button)
                bottomSheetDialog.findViewById(R.id.submitOTP);
        ImageView imageView = bottomSheetDialog.findViewById(R.id.closeButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomSheetDialog.show();
        // Save verification ID and resending token so we can use them later

    }


    public void showMaintanceDialog(Context mContext) {
        dialogMaintains = new Dialog(mContext);
        dialogMaintains =
                new Dialog(SplashActivity.this,
                        R.style.AppBottomSheetDialogTheme);
        dialogMaintains.setContentView(R.layout.layout_maintance_dialog);
        dialogMaintains.setCancelable(false);
        dialogMaintains.setCanceledOnTouchOutside(false);
        dialogMaintains.getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        //  bottomSheetDialog.set(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme);
        dialogMaintains.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button buttonSubmit = (Button)
                dialogMaintains.findViewById(R.id.submitOTP);
        ImageView imageView = dialogMaintains.findViewById(R.id.closeButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomSheetDialog.show();
        // Save verification ID and resending token so we can use them later

    }

    private static final String TAG = "SplashActivity";

    public void getSettings() {
        service.getSettings().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObject = response.body();
                versionName = BuildConfig.VERSION_NAME;
                Log.d(TAG, "onResponse: Current version  " + versionName + "" + response.body());
                if (jsonObject.has("maintenance")) {
                    if (jsonObject.get("maintenance").getAsBoolean()) {
                        Log.d(TAG, "onResponse: Under Maintenance");
                    } else {
                        showMaintanceDialog(SplashActivity.this);
                    }
                }

                if (jsonObject.has("app_version")) {
                    if (!versionName.equals(jsonObject.get("app_version"))) {
                        Log.d(TAG, "onResponse: New Update Available");
                        showUpdateDialog();
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}