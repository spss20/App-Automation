package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputEditText password;
    Button signIn;
    ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        service = ApiClient.create();

        username = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    submit();
                }
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
                            if (user.get("confirmed").getAsBoolean()){
                                new SessionManager(LoginActivity.this).saveUser(response.body());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Sorry! You are not confirmed by the admin. " +
                                        "Contact Administrator to confirm your account", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.code() == 400)
                                Toast.makeText(LoginActivity.this, "Invalid username/email or password", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(LoginActivity.this, "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(LoginActivity.this);
                    }
                });
    }
}