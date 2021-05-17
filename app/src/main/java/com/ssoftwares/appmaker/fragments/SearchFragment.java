package com.ssoftwares.appmaker.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.MainActivity;
import com.ssoftwares.appmaker.adapters.ProductBottomSheetAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.api.CommonApis;
import com.ssoftwares.appmaker.interfaces.ProductSelectedListener;
import com.ssoftwares.appmaker.modals.Product;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements CommonApis.onSearchResult, ProductSelectedListener {

    ApiService service;
    EditText editTextSearch;
    CommonApis commonApis;
    RecyclerView recyclerViewSearch;
    ProductBottomSheetAdapter productBottomSheetAdapter;
    ArrayList<Product> productSearchArrayList = new ArrayList<>();
    ImageView imageViewClose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_search  , container , false);
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);

        service = ApiClient.create();
        editTextSearch = view.findViewById(R.id.editTextSearch);
        imageViewClose = view.findViewById(R.id.closeImageView);

        commonApis = new CommonApis(service, getContext(), this);
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

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.getText().clear();
            }
        });

        setUpSearchRecyclerView();

        return view;
    }

    public void setUpSearchRecyclerView() {
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setHasFixedSize(true);
        productBottomSheetAdapter = new ProductBottomSheetAdapter(getContext(),
                productSearchArrayList, this);
        recyclerViewSearch.setAdapter(productBottomSheetAdapter);


    }

    @Override
    public void onSuccess(ArrayList<Product> result) {
        productSearchArrayList.clear();
        productSearchArrayList.addAll(result);
        productBottomSheetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void onSelected(Product product) {

    }
}

