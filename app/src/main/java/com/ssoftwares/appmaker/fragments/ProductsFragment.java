package com.ssoftwares.appmaker.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.ProductsActivity;
import com.ssoftwares.appmaker.adapters.ProductVerticalAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.modals.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {

    ProductVerticalAdapter adapter;
    ApiService service;
    String categoryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_products , container , false);

        service = ApiClient.create();
        Bundle bundle = getArguments();
        categoryId = bundle.getString("category_id" , null);
        if (categoryId == null) {
            getFragmentManager().popBackStackImmediate();
            return null;
        }

        RecyclerView productRecycler = view.findViewById(R.id.products_recycler);
        productRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductVerticalAdapter(getContext(), new ArrayList<>());
        productRecycler.setAdapter(adapter);
        getProducts(categoryId);

        return view;
    }

    private void getProducts(String category_id) {
        service.getProducts(null, category_id)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.body() != null) {
                            adapter.updateData(response.body());
                        } else
                            Toast.makeText(getContext(), "Failed to fetch products by category", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.v("Error", t.getLocalizedMessage());
                        Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
