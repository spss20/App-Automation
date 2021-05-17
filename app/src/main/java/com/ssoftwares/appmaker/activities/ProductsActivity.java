package com.ssoftwares.appmaker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.ProductVerticalAdapter;
import com.ssoftwares.appmaker.adapters.SpinnerAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.TagType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ProductVerticalAdapter adapter;
    ApiService service;
//    Spinner typeSpinner;
//    SpinnerAdapter spinnerAdapter;
//    ArrayList<TagType> listType = new ArrayList<>();

//    public void setListData() {
//
//        listType.add(new TagType("1", "All", null));
//        listType.add(new TagType("1", "Popular", "popular"));
//        listType.add(new TagType("1", "Newest", "newest"));
//        listType.add(new TagType("1", "Trending", "trending"));
//
//        // Now i have taken static values by loop.
//        // For further inhancement we can take data by webservice / json / xml;
//
//
//    }

    String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        categoryId = null;
        String tag = null;
        service = ApiClient.create();
        if (getIntent().hasExtra("category_id")) {
            categoryId = getIntent().getStringExtra("category_id");
        }
        if (getIntent().hasExtra("tag")) {
            tag = getIntent().getStringExtra("tag");

        }

//        String category = getIntent().getStringExtra("category_name");
//        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
//        typeSpinner.setOnItemSelectedListener(this);
//        setListData();
//
//        spinnerAdapter = new SpinnerAdapter(this, listType);
//        typeSpinner.setAdapter(spinnerAdapter);

//        typeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getProducts(categoryId, listType.get(position).getTag());
//            }
//        });


//        TextView categoryName = findViewById(R.id.category_name);
//        ImageView backBtn = findViewById(R.id.back_bt);
//        categoryName.setText(category);

        if (categoryId == null && tag == null) {
            finish();
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("category_name"));

        RecyclerView productRecycler = findViewById(R.id.products_recycler);
        productRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductVerticalAdapter(this, new ArrayList<>());
        productRecycler.setAdapter(adapter);
//        Log.d("TAG", "onCreate: Category Id"+category+"Tag-"+tag);
        getProducts(categoryId, tag);
//        for (int i = 0; i < listType.size(); i++) {
//            if (tag != null) {
//                if (tag.equals(listType.get(i).getTag())) {
//                    typeSpinner.setSelection(i);
//                }
//            }
//
//        }

//        backBtn.setOnClickListener(v -> {
//            finish();
//        });

    }

    private void getProducts(String category_id, String tag) {
        service.getProducts(tag, category_id)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.body() != null) {
                            adapter.updateData(response.body());
                        } else
                            Toast.makeText(ProductsActivity.this, "Failed to fetch products by category", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.v("Error", t.getLocalizedMessage());
                        Toast.makeText(ProductsActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "" + listType.get(position).getName(), Toast.LENGTH_SHORT).show();
//        getProducts(categoryId,
//                listType.get(position).getTag());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}