package com.ssoftwares.appmaker.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.BuildConfig;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.BannerAdapter;
import com.ssoftwares.appmaker.adapters.CategoryAdapter;
import com.ssoftwares.appmaker.adapters.ProductAdapter;
import com.ssoftwares.appmaker.adapters.ProductBottomSheetAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.api.CommonApis;
import com.ssoftwares.appmaker.fragments.HomeFragment;
import com.ssoftwares.appmaker.interfaces.ProductSelectedListener;
import com.ssoftwares.appmaker.modals.Banner;
import com.ssoftwares.appmaker.modals.Category;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.User;
import com.ssoftwares.appmaker.utils.SessionManager;
import com.ssoftwares.appmaker.utils.SnackUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    ApiService service;
    DrawerLayout dl;
    ActionBarDrawerToggle tl;
    SessionManager sessionManager;
    NavigationView navigationView;
    String versionName;
    BottomSheetDialog bottomSheetDialog;
    Dialog dialogMaintains;
    CardView cardViewSearch;
    ImageView imageViewProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = ApiClient.create();
        sessionManager = new SessionManager(this);

        navigationView = findViewById(R.id.drawer_nav);
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);

        cardViewSearch = findViewById(R.id.searchCard);

        User user = sessionManager.getUser();
        if (user != null) {
            userName.setText(user.getUsername());
            navigationView.getMenu().findItem(R.id.logout).setTitle("Logout");
//            textViewName.setText("Hey " + user.getUsername() + ",");
        } else {
            userName.setText("User not logged");
            navigationView.getMenu().findItem(R.id.logout).setTitle("Login");
            //set visibility to false giving to non authenticated user
            navigationView.getMenu().findItem(R.id.create_cpanel).setVisible(false);
            navigationView.getMenu().findItem(R.id.my_apps).setVisible(false);
            navigationView.getMenu().findItem(R.id.create_admin_panel).setVisible(false);
            navigationView.getMenu().findItem(R.id.my_cpanels).setVisible(false);
            navigationView.getMenu().findItem(R.id.feature_request).setVisible(false);

        }

        navigationView.setNavigationItemSelectedListener(this);
        String token = sessionManager.getToken();
        if (token != null)
            Log.v("User Token", token);


        Toolbar toolbar = findViewById(R.id.tool_bar);
        dl = findViewById(R.id.drawer_view);
        tl = new ActionBarDrawerToggle(this, dl, toolbar, R.string.open, R.string.close);
        dl.addDrawerListener(tl);
        tl.syncState();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container , new HomeFragment())
                .commit();
    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container , fragment);
        transaction.commit();
    }

    public void showUpdateDialog() {
        bottomSheetDialog =
                new BottomSheetDialog(MainActivity.this,
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

    public void showMaintenanceDialog(Context mContext) {
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
        service.getSettings().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObject = response.body();
                versionName = BuildConfig.VERSION_NAME;
                Log.d(TAG, "onResponse: Current version  " + versionName + "" + response.body());
                if (jsonObject.has("maintenance")) {
                    if (jsonObject.get("maintenance").getAsBoolean()) {
                        Log.d(TAG, "onResponse: Under Maintenance");
                        showMaintenanceDialog(MainActivity.this);
                        return;
                    } else {
                        // showMaintanceDialog(SplashActivity.this);
                    }

                }

                if (jsonObject.has("app_version")) {
                    if (!versionName.equals(jsonObject.get("app_version"))) {
                        Log.d(TAG, "onResponse: New Update Available");
                        showUpdateDialog();
                        return;
                    } else {
                        Intent intent = new Intent(MainActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                    }

                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.my_cpanels:
                intent = new Intent(this, MyCpanels.class);
                startActivity(intent);
                break;
            case R.id.create_cpanel:
                intent = new Intent(this, BuilderActivity.class);
                intent.putExtra("config_name", "cpanel");
                startActivity(intent);
                break;
            case R.id.my_apps:
                intent = new Intent(this, MyApps.class);
                startActivity(intent);
                break;
            case R.id.create_admin_panel:
                intent = new Intent(this, BuilderActivity.class);
                intent.putExtra("config_name", "admin_panel");
                startActivity(intent);
                break;
            case R.id.clear_cache:
                sessionManager.clearCache();
                Toast.makeText(this, "Cache cleared successfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.feature_request:
                intent = new Intent(this , FeatureRequestActivity.class);
                startActivity(intent);
                break;
            case R.id.privacy_policy:
                intent = new Intent(this, PagesActivity.class);
                intent.putExtra("page_name", "privacy_policy");
                intent.putExtra("tittle", "Privacy Policy");
                startActivity(intent);
                break;
            case R.id.terms_condition:
                intent = new Intent(this, PagesActivity.class);
                intent.putExtra("page_name", "terms_condition");
                intent.putExtra("tittle", "Terms & Conditions");
                startActivity(intent);
                break;
            case R.id.about_us:
                intent = new Intent(this, SplashBuilderActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                if (sessionManager.getUser() != null) {
                    sessionManager.logout();
                    navigationView.getMenu().getItem(3).setTitle("Log In");
                    TextView userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
                    userName.setText("User not logged");
                    Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

}