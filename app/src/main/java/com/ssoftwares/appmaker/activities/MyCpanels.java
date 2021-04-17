package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.CpanelAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.interfaces.CpanelSelectedListener;
import com.ssoftwares.appmaker.modals.Cpanel;
import com.ssoftwares.appmaker.modals.Step;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCpanels extends AppCompatActivity {

    ApiService service;
    CpanelAdapter adapter;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpanel);

        getSupportActionBar().setTitle("My Cpanels");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        service = ApiClient.create();
        sessionManager = new SessionManager(this);

        if (sessionManager.getToken() == null){
            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
            return;
        }
        //setAdapter
        RecyclerView cpanelRecycler = findViewById(R.id.cpanel_recycler);
        adapter = new CpanelAdapter(this, new ArrayList<>(), new CpanelSelectedListener() {
            @Override
            public void onSelected(Cpanel cpanel) {
                Toast.makeText(MyCpanels.this, "Cpanel selected", Toast.LENGTH_SHORT).show();

            }
        });

        cpanelRecycler.setLayoutManager(new LinearLayoutManager(this));
        cpanelRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        cpanelRecycler.setAdapter(adapter);

        getCpanels();
    }

    private CpanelSelectedListener selectedListener = new CpanelSelectedListener() {
        @Override
        public void onSelected(Cpanel cpanel) {
            service.getConfig(name)
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if (response.body() != null) {
                                try {
                                    String data = response.body().getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject().toString();
                                    configHash = AppUtils.getMd5(data);
                                    //Check if a cache of same config file already exists
                                    if (sessionManager.getConfigHash() != null) {
                                        if (sessionManager.getConfigHash().equals(AppUtils.getMd5(data))) {
                                            data = sessionManager.getConfig();
                                        }
                                    }
                                    rootJson = new JSONObject(data);
                                    if (rootJson.has("title"))
                                        getSupportActionBar().setTitle(rootJson.getString("title"));
                                    else
                                        getSupportActionBar().setTitle(getIntent().getStringExtra("subproduct_name"));
                                    JSONArray schema = rootJson.getJSONArray("schema");
                                    for (int i = 0; i < schema.length(); i++) {
                                        JSONObject element = schema.getJSONObject(i);
                                        inflateView(element.getString("type"), element);
                                    }
                                    //Get build steps array to show in dialog
                                    if (rootJson.has("steps")) {
                                        JSONArray stepsSchema = rootJson.getJSONArray("steps");
                                        for (int j = 0; j < stepsSchema.length(); j++) {
                                            JSONObject element = stepsSchema.getJSONObject(j);
                                            Step step = new Step();
                                            step.setOrder(element.getInt("order"));
                                            step.setStepName(element.getString("name"));
                                            step.setStepSlug(element.getString("slug"));
                                            step.setStepMessage(element.getString("message"));
                                            stepList.add(step);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            AppUtils.handleNoInternetConnection(BuilderActivity.this);
                        }
                    });
        }
    }
    private void getCpanels() {
        service.getCpanels(sessionManager.getToken(), sessionManager.getUserId())
                .enqueue(new Callback<List<Cpanel>>() {
                    @Override
                    public void onResponse(Call<List<Cpanel>> call, Response<List<Cpanel>> response) {
                        if (response.body() != null) {
                            adapter.updateData(response.body());
                        } else {
                            if (response.code() == 401)
                                Toast.makeText(MyCpanels.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MyCpanels.this, "Failed to fetch cpanels list", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Cpanel>> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(MyCpanels.this);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}