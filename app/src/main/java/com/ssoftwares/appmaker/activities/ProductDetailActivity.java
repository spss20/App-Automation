package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.ProductBannerAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.custom.MarkdownView;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Product product;
    ApiService service;
    ProductBannerAdapter adapter;
    MarkdownView productDescription;
    BottomNavigationView productPageNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        product = (Product) getIntent().getSerializableExtra("data");
        service = ApiClient.create();

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        productDescription = findViewById(R.id.product_desc);

        productPageNav = findViewById(R.id.product_page_nav);
        productPageNav.setOnNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productDescription.setOpenUrlInBrowser(true);

        adapter = new ProductBannerAdapter(this, product != null ? product.getImages() : new ArrayList<>());
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(new MarginPageTransformer(20));

        CircleIndicator3 indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        if (product == null) {
            String id = getIntent().getStringExtra("product_id");
            String name = getIntent().getStringExtra("product_name");
//            id = "1";
//            name = "Web Application";
            getProduct(id);
            getSupportActionBar().setTitle(name);
        } else {
           init();
        }

    }

    private void init() {
        getSupportActionBar().setTitle(product.getName());
        updateMarkdown(product.getDescription());
        adapter.updateData(product.getImages());
        if (product.isAutomated())
            productPageNav.getMenu().findItem(R.id.build_apps).setVisible(true);

        if (product.getBrochure() != null)
            productPageNav.getMenu().findItem(R.id.download_brochure).setVisible(true);
    }

    private void updateMarkdown(String d) {
        String description = d.replaceAll("/uploads", ApiClient.BASE_URL + "/uploads");
        productDescription.setMarkDownText(description);
        productDescription.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("HTML", value);
                    }
                });
    }

    private void getProduct(String id) {
        service.getSingleProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {
                if (response.body() != null) {
                    product = response.body();
                    init();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Cannot fetch product details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {
                Toast.makeText(ProductDetailActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {

            case R.id.see_product_demo:
                intent = new Intent(ProductDetailActivity.this, ProductDemoActivity.class);
                intent.putExtra("product_id", product.getId());
                intent.putExtra("product_name", product.getName());
                startActivity(intent);
                break;
            case R.id.product_website:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(ApiClient.BASE_URL + "/products/description/" + product.getId()));
                startActivity(intent);
                break;
            case R.id.download_brochure:
                //download brochure
                break;
            case R.id.build_apps:
                if (new SessionManager(this).getToken() != null) {
                    intent = new Intent(ProductDetailActivity.this, SubProductsActivity.class);
                    intent.putExtra("product_id", product.getId());
                    intent.putExtra("product_name", product.getName());
                    startActivity(intent);
                } else {
                    intent = new Intent(ProductDetailActivity.this , LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }
}