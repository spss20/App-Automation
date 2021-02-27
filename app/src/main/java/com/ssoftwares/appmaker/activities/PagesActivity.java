package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagesActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webpage;
    ApiService service;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);
        toolbar = findViewById(R.id.toolbar);
        webpage = findViewById(R.id.webpage);
        service = ApiClient.create();
        webpage.loadData("", "text/html", "UTF-8");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String page_type = intent.getStringExtra("page_name");
        String page_tittle = intent.getStringExtra("tittle");
        sessionManager = new SessionManager(PagesActivity.this);
        if (page_type == null) {
            return;
        }
        toolbar.setTitle("" + page_tittle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPages(page_type);


    }

    public void getPages(String type) {
        service.getStatic_Pages().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        if (jsonObject.has(type)) {
                            if (jsonObject.get(type) != null) {
                                if (jsonObject.get(type).toString() != null) {
                                    webpage.loadData(jsonObject.get(type).toString(),
                                            "text/html", "UTF-8");
                                }

                            }

                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}