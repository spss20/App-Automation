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
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;

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
        adapter = new OrdersAdapter(this, new ArrayList<>() , onClickInterface);
        ordersRecycler.setLayoutManager(new LinearLayoutManager(this));
        ordersRecycler.setAdapter(adapter);

        fetchOrders();
    }

    private void fetchOrders() {
        service.getOrders(sessionManager.getToken() , "id:DESC")
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

    private OnClickInterface onClickInterface = new OnClickInterface() {
        @Override
        public void onClick(Object object) {
            Order order = (Order) object;
            String url = order.getConfig().getAttachmentUrl().replace("%2f" , "/");
            service.fetchJson(order.getConfig().getImageUrl())
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()){
                                Intent intent = new Intent(MyApps.this , BuilderActivity.class);
                                intent.putExtra("config" , response.body().toString());
                                startActivity(intent);
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