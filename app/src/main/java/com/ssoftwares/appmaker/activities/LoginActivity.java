package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.modals.errormodels.BaseError;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;
import com.ssoftwares.appmaker.utils.SnackUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputEditText password;
    Button signIn;
    ApiService service;
    TextView createAccountTextView;
    SnackUtils snackUtils;
    View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        service = ApiClient.create();

        username = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        mainView = findViewById(R.id.mainLayLogin);

        createAccountTextView = findViewById(R.id.createAccountTextView);
        snackUtils = new SnackUtils(this);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    submit();
                }
            }
        });

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    private boolean validation() {
        if (username.getText().toString().isEmpty()) {
            username.setError("Username/Email can't be empty");
            return false;
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Please enter password");
            return false;
        }
        return true;
    }


    private void submit() {
        service.login(username.getText().toString(), password.getText().toString())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.body() != null) {
                            JsonObject user = response.body().get("user").getAsJsonObject();
                            if (user.get("confirmed").getAsBoolean()) {
                                snackUtils.showMessageSnackBar("Login Successful",
                                        mainView, 0);
                                new SessionManager(LoginActivity.this).saveUser(response.body());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                snackUtils.showMessageSnackBar("Sorry! You are not confirmed by the admin. " +
                                                "Contact Administrator to confirm your account",
                                        mainView, 0);
                            }
                        } else {
                            //BaseError baseError = new Gson().fromJson(response.toString(), BaseError.class);
//                            showErrorTextView("Invalid username/email or password",
//                                    3000);

                            if (response.code() == 400) {
                                snackUtils.showMessageSnackBar("Invalid username/email or password",
                                        mainView, 0);
                            } else {
                                snackUtils.showMessageSnackBar("Unknown Error Occurred",
                                        mainView, 0);
                            }


                            //Toast.makeText(LoginActivity.this, "Invalid username/email or password", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(LoginActivity.this);
                    }
                });
    }
}