package com.ssoftwares.appmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.BannerAdapter;
import com.ssoftwares.appmaker.adapters.CategoryAdapter;
import com.ssoftwares.appmaker.adapters.ProductAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.api.CommonApis;
import com.ssoftwares.appmaker.modals.Banner;
import com.ssoftwares.appmaker.modals.Category;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.User;
import com.ssoftwares.appmaker.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CommonApis.onSearchResult {

    CategoryAdapter categoryAdapter;
    ProductAdapter popularProductAdapter;
    ProductAdapter trendingProductAdapter;
    ProductAdapter newestProductAdapter;
    BannerAdapter bannerAdapter;

    ApiService service;
    DrawerLayout dl;
    ActionBarDrawerToggle tl;
    SessionManager sessionManager;
    NavigationView navigationView;
    TextView textViewAllPopular, textViewAllTrending, textViewAllNewest, textViewAllCatgeory;
    EditText editTextSearch;
    CommonApis commonApis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = ApiClient.create();
        sessionManager = new SessionManager(this);

        navigationView = findViewById(R.id.drawer_nav);
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        textViewAllPopular = findViewById(R.id.textAllPopular);
        textViewAllTrending = findViewById(R.id.textAllTrendingProducts);
        textViewAllNewest = findViewById(R.id.textAllNewestProducts);
        textViewAllCatgeory = findViewById(R.id.allCategory);
        editTextSearch = findViewById(R.id.editTextSearch);
        User user = sessionManager.getUser();
        if (user != null) {
            userName.setText(user.getUsername());
            navigationView.getMenu().findItem(R.id.logout).setTitle("Logout");
        } else {
            userName.setText("User not logged");
            navigationView.getMenu().findItem(R.id.logout).setTitle("Login");
            //set visibility to false giving to non authenticated user
            navigationView.getMenu().findItem(R.id.create_cpanel).setVisible(false);
            navigationView.getMenu().findItem(R.id.create_admin_panel).setVisible(false);
            navigationView.getMenu().findItem(R.id.my_cpanels).setVisible(false);

        }

        navigationView.setNavigationItemSelectedListener(this);
        String token = sessionManager.getToken();
        if (token != null)
            Log.v("User Token", token);


        Toolbar toolbar = findViewById(R.id.tool_bar);
        dl = findViewById(R.id.drawer_view);
        tl = new ActionBarDrawerToggle(this, dl, toolbar, R.string.open, R.string.close);
        dl.addDrawerListener(tl);
        tl.syncState();

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        bannerAdapter = new BannerAdapter(this, new ArrayList<>());
        viewPager.setAdapter(bannerAdapter);
//        viewPager.setClipToPadding(false);
//        viewPager.setPadding(40 , 0 , 40 , 0);
        viewPager.setPageTransformer(new MarginPageTransformer(20));
        getBanners();

        CircleIndicator3 indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        bannerAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());


        RecyclerView categoryRecyler = findViewById(R.id.category_recycler);
        categoryRecyler.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(this, new ArrayList<>());
        categoryRecyler.setAdapter(categoryAdapter);
        getCategories();

        RecyclerView popularProductRecycler = findViewById(R.id.popular_products_recycler);
        popularProductRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        popularProductAdapter = new ProductAdapter(this, new ArrayList<>());
        popularProductRecycler.setAdapter(popularProductAdapter);
        getProducts("popular");

        RecyclerView trendingProductRecycler = findViewById(R.id.trending_products_recyler);
        trendingProductRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        trendingProductAdapter = new ProductAdapter(this, new ArrayList<>());
        trendingProductRecycler.setAdapter(trendingProductAdapter);
        getProducts("trending");

        RecyclerView newestProductRecycler = findViewById(R.id.newest_products_recycler);
        newestProductRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        newestProductAdapter = new ProductAdapter(this, new ArrayList<>());
        newestProductRecycler.setAdapter(newestProductAdapter);
        getProducts("newest");


        textViewAllPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                intent.putExtra("tag", "popular");
                intent.putExtra("category_name", "Popular Products");
                startActivity(intent);

            }
        });
        textViewAllCatgeory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);

                startActivity(intent);
            }
        });
        textViewAllNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                intent.putExtra("tag", "newest");
                intent.putExtra("category_name", "Newest Products");
                startActivity(intent);

            }
        });
        textViewAllTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                intent.putExtra("tag", "trending");
                intent.putExtra("category_name", "Trending Products");
                startActivity(intent);

            }
        });
        commonApis = new CommonApis(service, this, this);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                commonApis.search(s.toString(), "contains", "name", "products");
            }
        });

    }


    private void getBanners() {
        service.getBanners().enqueue(new Callback<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                if (response.body() != null)
                    bannerAdapter.updateData(response.body());
                else
                    Toast.makeText(MainActivity.this, "Failed to fetch banner", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed : Check you internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProducts(String tag) {
        service.getProducts(tag, null).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null) {
                    if (tag.equals("popular"))
                        popularProductAdapter.updateData(response.body());
                    else if (tag.equals("trending"))
                        trendingProductAdapter.updateData(response.body());
                    else if (tag.equals("newest"))
                        newestProductAdapter.updateData(response.body());
                } else
                    Toast.makeText(MainActivity.this, "Failed to fetch popular products", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCategories() {
        service.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.body() != null) {
                    categoryAdapter.updateData(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.my_cpanels:
                intent = new Intent(this, MyCpanels.class);
                startActivity(intent);
                break;
            case R.id.create_cpanel:
                intent = new Intent(this, BuilderActivity.class);
                intent.putExtra("config_name", "cpanel");
                startActivity(intent);
                break;
            case R.id.my_apps:
                intent  = new Intent(this , MyApps.class);
                startActivity(intent);
                break;
            case R.id.create_admin_panel:
                intent = new Intent(this, BuilderActivity.class);
                intent.putExtra("config_name", "admin_panel");
                startActivity(intent);
                break;
            case R.id.clear_cache:
                sessionManager.clearCache();
                Toast.makeText(this, "Cache cleared successfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                if (sessionManager.getUser() != null) {
                    sessionManager.logout();
                    navigationView.getMenu().getItem(3).setTitle("Log In");
                    TextView userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
                    userName.setText("User not logged");
                    Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

    @Override
    public void onSuccess(JsonObject result) {
        Log.d("TAG", "onSuccess: " + result.getAsString());
    }

    @Override
    public void onFailed(String message) {

    }
}