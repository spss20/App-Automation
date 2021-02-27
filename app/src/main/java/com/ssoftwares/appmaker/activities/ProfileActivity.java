package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.BuildConfig;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.modals.User;
import com.ssoftwares.appmaker.utils.SessionManager;
import com.ssoftwares.appmaker.utils.SnackUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout myappsLy;
    LinearLayout checkforUpdateLay, logoutLayout;
    ApiService service;
    int versionName;
    BottomSheetDialog bottomSheetDialog;

    Dialog dialogMaintains;
    SnackUtils snackUtils;
    RelativeLayout mainSplash;
    SessionManager sessionManager;
    private static final String TAG = "ProfileActivity";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.profileToolbar);
        myappsLy = findViewById(R.id.myappsLy);
        logoutLayout = findViewById(R.id.layLogout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking for new update...");
        checkforUpdateLay = findViewById(R.id.checkforUpdateLay);
        checkforUpdateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSettings();
            }
        });
        sessionManager = new SessionManager(this);
        service = ApiClient.create();
        myappsLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MyApps.class);
                startActivity(intent);

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void showUpdateDialog() {
        bottomSheetDialog =
                new BottomSheetDialog(ProfileActivity.this,
                        R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.layout_update_dialog);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        //  bottomSheetDialog.setFeatureDrawable(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme);
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

    public void logout() {
        bottomSheetDialog =
                new BottomSheetDialog(ProfileActivity.this,
                        R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.layout_logout_dialog);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        //  bottomSheetDialog.setFeatureDrawable(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        MaterialButton buttonSubmit = (MaterialButton)
                bottomSheetDialog.findViewById(R.id.yesButton);
        MaterialButton imageView = bottomSheetDialog.findViewById(R.id.noButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getUser() != null) {
                    sessionManager.logout();
                    Toast.makeText(ProfileActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProfileActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        bottomSheetDialog.show();
        // Save verification ID and resending token so we can use them later

    }


    public void showMaintanceDialog(Context mContext) {
        dialogMaintains = new Dialog(mContext, R.style.AppBottomSheetDialogTheme);

        dialogMaintains.setContentView(R.layout.layout_maintance_dialog);

        dialogMaintains.setCancelable(false);
        dialogMaintains.setCanceledOnTouchOutside(false);
        dialogMaintains.getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Window window = dialogMaintains.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        //  bottomSheetDialog.set(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme);
        dialogMaintains.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imageView = dialogMaintains.findViewById(R.id.closeButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMaintains.cancel();

            }
        });

        dialogMaintains.show();
        // Save verification ID and resending token so we can use them later

    }

    public void getSettings() {
        progressDialog.show();
        service.getSettings().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObject = response.body();
                versionName = BuildConfig.VERSION_CODE;
                Log.d(TAG, "onResponse: Current version  " + versionName + "" + response.body());
//                if (jsonObject.has("maintenance")) {
//                    if (jsonObject.get("maintenance").getAsBoolean()) {
//                        Log.d(TAG, "onResponse: Under Maintenance");
//                        showMaintanceDialog(ProfileActivity.this);
//                        return;
//                    } else {
//                        // showMaintanceDialog(SplashActivity.this);
//                    }
//
//                }

                if (jsonObject.has("app_version")) {
                    if (versionName != jsonObject.get("app_version").getAsInt()) {
                        Log.d(TAG, "onResponse: New Update Available");
                        progressDialog.dismiss();
                        showUpdateDialog();
                        return;
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "You are already using latest version", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    progressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}