package com.ssoftwares.appmaker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.AllCategoryAdapter;
import com.ssoftwares.appmaker.adapters.CategoryAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.custom.SpaceItemDecoration;
import com.ssoftwares.appmaker.modals.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView categoryRecyler;
    ImageView imageViewBack;
    AllCategoryAdapter categoryAdapter;
    Context mContext;

    ApiService service;
    TextView category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        service = ApiClient.create();
        category_name = findViewById(R.id.category_name);
        category_name.setText("All Categories");

        categoryRecyler = findViewById(R.id.category_recycler);
        categoryRecyler.setLayoutManager(new StaggeredGridLayoutManager(2,
                LinearLayout.VERTICAL));
//        categoryRecyler.setLayoutManager(new GridLayoutManager(this,
//                2));
        categoryAdapter = new AllCategoryAdapter(this, new ArrayList<>());
        categoryRecyler.addItemDecoration(new SpaceItemDecoration(
                40));
        categoryRecyler.setAdapter(categoryAdapter);

        imageViewBack = findViewById(R.id.back_bt);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCategories();


    }

    private void getCategories() {
        service.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.body() != null) {
                    categoryAdapter.updateData(response.body());
                } else {
                    Toast.makeText(CategoryActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}