package com.ssoftwares.appmaker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.adapters.AdminPanelAdapter;
import com.ssoftwares.appmaker.adapters.ProductBottomSheetAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.interfaces.AdminPanelSelectedListener;
import com.ssoftwares.appmaker.interfaces.ProductSelectedListener;
import com.ssoftwares.appmaker.modals.AdminPanel;
import com.ssoftwares.appmaker.modals.Cpanel;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanelBottomSheetFragment extends BottomSheetDialogFragment {


    private AdminPanelSelectedListener listener;
    private ApiService service;
    private AdminPanelAdapter adapter;
    private SessionManager sessionManager;

    public static AdminPanelBottomSheetFragment newInstance(AdminPanelSelectedListener listener) {
        final AdminPanelBottomSheetFragment fragment = new AdminPanelBottomSheetFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ApiClient.create();
        sessionManager = new SessionManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView adminPanelRecycler = view.findViewById(R.id.items_recycler);
        adapter = new AdminPanelAdapter(getContext(), new ArrayList<>() , listener);
        adminPanelRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adminPanelRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adminPanelRecycler.setAdapter(adapter);
        getAdminPanels();
    }

    private void getAdminPanels() {
        service.getAdminPanels(sessionManager.getToken() , "id:DESC")
                .enqueue(new Callback<List<AdminPanel>>() {
                    @Override
                    public void onResponse(Call<List<AdminPanel>> call, Response<List<AdminPanel>> response) {
                        if (response.body() != null) {
                            adapter.updateData(response.body());
                        } else {
                            if (response.code() == 401)
                                Toast.makeText(getContext(), "Unauthorized : Please logout and login again.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Failed to fetch cpanels list", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AdminPanel>> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(getContext());
                    }
                });
    }

}