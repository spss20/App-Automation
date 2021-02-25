package com.ssoftwares.appmaker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.SubProductAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.modals.SubProduct;
import com.ssoftwares.appmaker.utils.SpaceItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubProductsActivity extends AppCompatActivity {

    SubProductAdapter adapter;
    ApiService service;
    ArrayList<SubProduct> subProductArrayList = new ArrayList<>();
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_products);

        String productId = getIntent().getStringExtra("product_id");
        name = getIntent().getStringExtra("product_name");
        boolean isAdminPanel = getIntent().getBooleanExtra("is_AdminPanel", false);
        if (productId == null) finish();
        service = ApiClient.create();

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(name);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Build " + name);

        RecyclerView subProductRecycler = findViewById(R.id.subproduct_recycler);
        subProductRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        int px15 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15,
                getResources().getDisplayMetrics()
        );
        subProductRecycler.addItemDecoration(new SpaceItemDecoration(2, px15, true));

        adapter = new SubProductAdapter(this, new ArrayList<>());
        subProductRecycler.setAdapter(adapter);

        getSubProducts(productId, isAdminPanel);
    }

    private void getSubProducts(String productId, boolean isAdminPanel) {
        service.getSubProducts(productId)
                .enqueue(new Callback<List<SubProduct>>() {
                    @Override
                    public void onResponse(Call<List<SubProduct>> call,
                                           Response<List<SubProduct>> response) {
                        if (response.body() != null) {
                            subProductArrayList.clear();
                            if (isAdminPanel) {
                                SubProduct subProduct = new SubProduct();
                                subProduct.setId(productId);
                                subProduct.setName("Admin Panel " + name);
                                subProduct.setDescription("admin");
                                subProductArrayList.add(subProduct);

                            }
                            subProductArrayList.addAll(response.body());
                            adapter.updateData(subProductArrayList);
                        } else
                            Toast.makeText(SubProductsActivity.this, "Failed to fetch subcategories", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<SubProduct>> call, Throwable t) {
                        Toast.makeText(SubProductsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}