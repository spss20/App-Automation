package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.OrdersAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.interfaces.OnClickInterface;
import com.ssoftwares.appmaker.modals.Order;
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

public class MyApps extends AppCompatActivity {

    private SessionManager sessionManager;
    private ApiService service;
    private OrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Apps");

        sessionManager = new SessionManager(this);
        service = ApiClient.create();

        RecyclerView ordersRecycler = findViewById(R.id.orders_recycler);
        adapter = new OrdersAdapter(this, new ArrayList<>() , onOrderClick ,
                onShowOutputClick);
        ordersRecycler.setLayoutManager(new LinearLayoutManager(this));
        ordersRecycler.setAdapter(adapter);

        fetchOrders();
    }

    private void fetchOrders() {
        service.getOrders(sessionManager.getToken())
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        if (response.isSuccessful()) {
                            adapter.updateData(response.body());
                        } else
                            Toast.makeText(MyApps.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(MyApps.this);
                    }
                });
    }

    private final OnClickInterface onOrderClick = new OnClickInterface() {
        @Override
        public void onClick(Object object) {
            Order order = (Order) object;
            service.fetchJson(order.getConfig().getImageUrl())
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()){
                                try {
                                    Intent intent = new Intent(MyApps.this, BuilderActivity.class);
                                    intent.putExtra("config", response.body().toString());
                                    intent.putExtra("isCachingEnabled" , false);
                                    startActivity(intent);
                                } catch (RuntimeException e){
                                    e.printStackTrace();
                                    Toast.makeText(MyApps.this, "An unknown error occurred, One of the reason might be huge image size", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(MyApps.this, "Cannot fetch json file", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            AppUtils.handleNoInternetConnection(MyApps.this);
                        }
                    });

        }
    };

    private final OnClickInterface onShowOutputClick = new OnClickInterface() {
        @Override
        public void onClick(Object object) {
            Order order = (Order) object;
            service.fetchJson(order.getConfig().getImageUrl())
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()){
                                try {
                                    JSONObject rootJson = new JSONObject(response.body().toString());
                                    if (rootJson.has("steps")) {
                                        JSONArray stepsSchema = rootJson.getJSONArray("steps");
                                        List<Step> stepList = new ArrayList<>();
                                        for (int j = 0; j < stepsSchema.length(); j++) {
                                            JSONObject element = stepsSchema.getJSONObject(j);
                                            Step step = new Step();
                                            step.setOrder(element.getInt("order"));
                                            step.setStepName(element.getString("name"));
                                            step.setStepSlug(element.getString("slug"));
                                            step.setStepMessage(element.getString("message"));
                                            stepList.add(step);
                                        }
                                        if (stepList.size() != 0)
                                            AppUtils.showResultDialog(MyApps.this, order.getOutputUrl(), stepList);
                                    } else
                                        AppUtils.showResultDialog(MyApps.this, order.getOutputUrl());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else
                                Toast.makeText(MyApps.this, "Cannot fetch json file", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            AppUtils.handleNoInternetConnection(MyApps.this);
                        }
                    });
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}