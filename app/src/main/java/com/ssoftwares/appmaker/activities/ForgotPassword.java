package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;

public class ForgotPassword extends AppCompatActivity {
    Button buttonSubmitEmail;
    EditText textInputLayoutEmail;
    ApiService service;
    TextView backLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        buttonSubmitEmail = findViewById(R.id.forgotPswd);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        backLogin = findViewById(R.id.backLogin);
        service = ApiClient.create();
        buttonSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendResetLink() {
        // service
    }
}