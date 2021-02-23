package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.modals.Step;
import com.ssoftwares.appmaker.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    List<Step> stepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        stepList.add(new Step(0 , "Preparing" , "PREPARING_THINGS" , "Initialising and Validating fields"));
        stepList.add(new Step(1 , "Database" , "CREATING_DATABASE" , "Establishing database connections"));
        stepList.add(new Step(2 , "Email" , "CREATING_EMAIL" , "Establishing email connections"));
        stepList.add(new Step(3 , "Successful" , "SITE_IS_LIVE" , "Site creation successful"));

        findViewById(R.id.test_open).setOnClickListener(v -> {
            AppUtils.showResultDialog(TestActivity.this , "http://web2app.in/test.html" , stepList);
        });
    }
}