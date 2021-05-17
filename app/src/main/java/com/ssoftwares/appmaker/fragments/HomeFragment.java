package com.ssoftwares.appmaker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.CategoryActivity;
import com.ssoftwares.appmaker.activities.MainActivity;
import com.ssoftwares.appmaker.activities.ProductsActivity;
import com.ssoftwares.appmaker.adapters.BannerAdapter;
import com.ssoftwares.appmaker.adapters.CategoryAdapter;
import com.ssoftwares.appmaker.adapters.ProductAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.modals.Banner;
import com.ssoftwares.appmaker.modals.Category;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    CategoryAdapter categoryAdapter;
    ProductAdapter popularProductAdapter;
    ProductAdapter trendingProductAdapter;
    ProductAdapter newestProductAdapter;
    BannerAdapter bannerAdapter;
    ApiService service;
    SessionManager sessionManager;
    
    TextView textViewAllPopular, textViewAllTrending, textViewAllNewest, textViewAllCatgeory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_home , container , false);

        service = ApiClient.create();
        sessionManager = new SessionManager(getContext());

        textViewAllPopular = view.findViewById(R.id.textAllPopular);
        textViewAllTrending = view.findViewById(R.id.textAllTrendingProducts);
        textViewAllNewest = view.findViewById(R.id.textAllNewestProducts);
        textViewAllCatgeory = view.findViewById(R.id.allCategory);

        ViewPager2 viewPager = view.findViewById(R.id.viewpager);
        bannerAdapter = new BannerAdapter(getActivity(), new ArrayList<>());
        viewPager.setAdapter(bannerAdapter);
        viewPager.setPageTransformer(new MarginPageTransformer(20));

        getBanners();

        CircleIndicator3 indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        bannerAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());


        RecyclerView categoryRecycler = view.findViewById(R.id.category_recycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>());
        categoryRecycler.setAdapter(categoryAdapter);
        getCategories();

        RecyclerView popularProductRecycler = view.findViewById(R.id.popular_products_recycler);
        popularProductRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        popularProductAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        popularProductRecycler.setAdapter(popularProductAdapter);
        getProducts("popular");

        RecyclerView trendingProductRecycler = view.findViewById(R.id.trending_products_recyler);
        trendingProductRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        trendingProductAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        trendingProductRecycler.setAdapter(trendingProductAdapter);
        getProducts("trending");

        RecyclerView newestProductRecycler = view.findViewById(R.id.newest_products_recycler);
        newestProductRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        newestProductAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        newestProductRecycler.setAdapter(newestProductAdapter);
        getProducts("newest");


        textViewAllPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("tag", "popular");
                intent.putExtra("category_name", "Popular Products");
                startActivity(intent);

            }
        });
        textViewAllCatgeory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CategoryActivity.class);

                startActivity(intent);
            }
        });
        textViewAllNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("tag", "newest");
                intent.putExtra("category_name", "Newest Products");
                startActivity(intent);

            }
        });
        textViewAllTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("tag", "trending");
                intent.putExtra("category_name", "Trending Products");
                startActivity(intent);

            }
        });
        
        return view;
    }


    private void getBanners() {
        service.getBanners().enqueue(new Callback<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                if (response.body() != null)
                    bannerAdapter.updateData(response.body());
                else
                    Toast.makeText(getContext(), "Failed to fetch banner", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed : Check you internet connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Failed to fetch popular products", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
