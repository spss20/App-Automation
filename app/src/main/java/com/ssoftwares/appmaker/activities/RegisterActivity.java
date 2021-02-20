package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutUsername, textInputLayoutMobile;
    Button buttonSubmit;
    String username, email, mobile, password;
    ApiService service;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutUsername = findViewById(R.id.textInputUsername);
        textInputLayoutMobile = findViewById(R.id.textInputMobile);
        textInputLayoutPassword = findViewById(R.id.textInputPassword);
        buttonSubmit = findViewById(R.id.sign_up);
        loginTextView = findViewById(R.id.loginTextView);
        service = ApiClient.create();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    submit();
                }
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private boolean validation() {
        username = textInputLayoutUsername.getEditText().getText().toString();
        email = textInputLayoutEmail.getEditText().getText().toString();
        mobile = textInputLayoutMobile.getEditText().getText().toString();
        password = textInputLayoutPassword.getEditText().getText().toString();
        if (username.toString().isEmpty()) {
            textInputLayoutUsername.setError("Username can't be empty");
            return false;
        } else if (email.toString().isEmpty()) {
            textInputLayoutEmail.setError("Please enter Email");
            return false;
        } else if (mobile.toString().isEmpty()) {
            textInputLayoutMobile.setError("Please enter Mobile");
            return false;
        } else if (password.toString().isEmpty()) {
            textInputLayoutPassword.setError("Please enter Password");
            return false;
        }

        return true;
    }

    private void submit() {
        service.register(username, email, password, true, false, mobile)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("TAG", "onResponse: Signup" + response.toString());

                        if (response.body() != null) {
                            JsonObject user = response.body().get("user").getAsJsonObject();
                            if (user.get("confirmed").getAsBoolean()) {
                                new SessionManager(RegisterActivity.this).saveUser(response.body());
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Sorry! You are not confirmed by the admin. " +
                                        "Contact Administrator to confirm your account", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.code() == 400)
                                Toast.makeText(RegisterActivity.this, response.body()
                                        .getAsJsonObject("message")
                                        .getAsJsonObject("messages").get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(RegisterActivity.this, "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(RegisterActivity.this);
                    }
                });
    }
}