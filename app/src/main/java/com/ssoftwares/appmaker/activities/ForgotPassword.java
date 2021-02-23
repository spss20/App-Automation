package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.ssoftwares.appmaker.R;

public class ForgotPassword extends AppCompatActivity {
    Button buttonSubmitEmail;
    TextInputLayout textInputLayoutEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        buttonSubmitEmail = findViewById(R.id.forgotPswd);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        buttonSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}