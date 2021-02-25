package com.ssoftwares.appmaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.BuilderActivity;
import com.ssoftwares.appmaker.adapters.CpanelAdapter;
import com.ssoftwares.appmaker.api.ApiClient;
import com.ssoftwares.appmaker.api.ApiService;
import com.ssoftwares.appmaker.interfaces.CpanelSelectedListener;
import com.ssoftwares.appmaker.modals.Cpanel;
import com.ssoftwares.appmaker.modals.Product;
import com.ssoftwares.appmaker.modals.SelectItemDynamicLayout;
import com.ssoftwares.appmaker.utils.AppUtils;
import com.ssoftwares.appmaker.utils.Dimensions;
import com.ssoftwares.appmaker.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CpanelBottomSheetFragment extends BottomSheetDialogFragment {


    private CpanelSelectedListener listener;
    private SessionManager sessionManager;
    private ApiService service;
    private CpanelAdapter adapter;

    public static CpanelBottomSheetFragment newInstance(CpanelSelectedListener listener) {
        final CpanelBottomSheetFragment fragment = new CpanelBottomSheetFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        service = ApiClient.create();
        sessionManager = new SessionManager(getContext());
        return inflater.inflate(R.layout.activity_cpanel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView cpanelRecycler = view.findViewById(R.id.cpanel_recycler);
        adapter = new CpanelAdapter(getContext(), new ArrayList<>(), new CpanelSelectedListener() {
            @Override
            public void onSelected(Cpanel cpanel) {
                listener.onSelected(cpanel);
            }
        });
        cpanelRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cpanelRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        cpanelRecycler.setAdapter(adapter);
        getCpanels();
    }

    private void getCpanels() {
        service.getCpanels(sessionManager.getToken(), sessionManager.getUserId() , "id:DESC")
                .enqueue(new Callback<List<Cpanel>>() {
                    @Override
                    public void onResponse(Call<List<Cpanel>> call, Response<List<Cpanel>> response) {
                        if (response.body() != null) {
                            adapter.updateData(response.body());
                        } else {
                            if (response.code() == 401)
                                Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Failed to fetch cpanels list", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Cpanel>> call, Throwable t) {
                        AppUtils.handleNoInternetConnection(getContext());
                    }
                });
    }
}