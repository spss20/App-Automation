package com.ssoftwares.appmaker.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeatureRequestActivity extends AppCompatActivity {

    SessionManager sessionManager;
    ApiService service;
    EditText featureEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feature Request");

        sessionManager = new SessionManager(this);
        service = ApiClient.create();

        featureEt = findViewById(R.id.feature_et);
        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishFeatureRequest();
            }
        });
    }

    private void publishFeatureRequest() {
        String feature = featureEt.getText().toString();
        if (feature.isEmpty()) {
            Toast.makeText(this, "Please enter something in feature box", Toast.LENGTH_SHORT).show();
            return;
        }
        AppUtils.showLoadingBar(this);
        service.createFeatureRequest(sessionManager.getToken(), feature)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        AppUtils.dissmissLoadingBar();
                        if (response.isSuccessful()) {
                            Toast.makeText(FeatureRequestActivity.this, "Thanks for submitting feature request. People like you help us to progress better", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FeatureRequestActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(FeatureRequestActivity.this);
                        AppUtils.dissmissLoadingBar();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}