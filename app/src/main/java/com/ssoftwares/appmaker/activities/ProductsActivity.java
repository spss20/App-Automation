package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.ProductVerticalAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.modals.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    ProductVerticalAdapter adapter;
    ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        service = ApiClient.create();

        String id = getIntent().getStringExtra("category_id");
        String category = getIntent().getStringExtra("category_name");

        TextView categoryName = findViewById(R.id.category_name);
        ImageView backBtn = findViewById(R.id.back_bt);
        categoryName.setText(category);

        if (id == null){
            finish();
            return;
        }
        RecyclerView productRecycler = findViewById(R.id.products_recycler);
        productRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductVerticalAdapter(this , new ArrayList<>());
        productRecycler.setAdapter(adapter);

        getProducts(id);

        backBtn.setOnClickListener(v -> {finish();});

    }

    private void getProducts(String category_id) {
        service.getProducts(null , category_id)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.body() != null) {
                            adapter.updateData(response.body());
                        }
                        else
                            Toast.makeText(ProductsActivity.this, "Failed to fetch products by category", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.v("Error" , t.getLocalizedMessage());
                        Toast.makeText(ProductsActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}