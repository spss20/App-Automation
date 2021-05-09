package com.ssoftwares.appmaker.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.interfaces.AdminPanelSelectedListener;
import com.ssoftwares.appmaker.modals.AdminPanel;

import java.util.List;

public class AdminPanelAdapter extends RecyclerView.Adapter<AdminPanelAdapter.AdminPanelViewholder> {

    private Context mContext;
    private List<AdminPanel> adminPanelsList;
    private AdminPanelSelectedListener listener;

    public AdminPanelAdapter(Context mContext, List<AdminPanel> adminPanelsList , AdminPanelSelectedListener listener) {
        this.mContext = mContext;
        this.adminPanelsList = adminPanelsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminPanelViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.admin_panel_item , parent , false);
        return new AdminPanelViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPanelViewholder holder, int position) {
        AdminPanel adminPanel = adminPanelsList.get(position);
        holder.companyName.setText(adminPanel.getCompany_name());
        holder.baseUrl.setText(adminPanel.getBaseUrl());
        holder.productName.setText(adminPanel.getProduct().getName());

        Picasso.get().load(adminPanel.getCompany_logo().getFormats().getThumbnail().getImageUrl())
                .into(holder.companyLogo);

        holder.itemView.setOnClickListener(v -> listener.onSelected(adminPanel));
    }

    @Override
    public int getItemCount() {
        return adminPanelsList.size();
    }

    public void updateData(List<AdminPanel> adminPanelsList) {
        this.adminPanelsList = adminPanelsList;
        notifyDataSetChanged();
    }

    public static class AdminPanelViewholder extends RecyclerView.ViewHolder{
        TextView companyName;
        ImageView companyLogo;
        TextView baseUrl;
        TextView productName;

        public AdminPanelViewholder(@NonNull View v) {
            super(v);
            companyName = v.findViewById(R.id.company_name);
            companyLogo = v.findViewById(R.id.company_logo);
            baseUrl = v.findViewById(R.id.base_url);
            productName = v.findViewById(R.id.product_name);
        }
    }
}
