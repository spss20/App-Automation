package com.ssoftwares.appmaker.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.interfaces.CpanelSelectedListener;
import com.ssoftwares.appmaker.modals.Cpanel;

import java.util.List;

public class CpanelAdapter extends RecyclerView.Adapter<CpanelAdapter.CpanelViewHolder> {

    private Context mContext;
    private List<Cpanel> cpanelList;
    private CpanelSelectedListener listener;

    public CpanelAdapter(Context mContext, List<Cpanel> cpanelList , CpanelSelectedListener listener) {
        this.mContext = mContext;
        this.cpanelList = cpanelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CpanelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cpanel_item , parent , false);
        return new CpanelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CpanelViewHolder holder, int position) {
        Cpanel cpanel = cpanelList.get(position);
        holder.domainName.setText(cpanel.getDomain());
        holder.clientName.setText(cpanel.getClient_name());
        holder.createdOn.setText(cpanel.getCreated_at());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(cpanel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cpanelList.size();
    }

    public static class CpanelViewHolder extends RecyclerView.ViewHolder {
        TextView domainName;
        TextView clientName;
        TextView createdOn;
        public CpanelViewHolder(@NonNull View v) {
            super(v);
            domainName = v.findViewById(R.id.domain_name);
            clientName = v.findViewById(R.id.client_name);
            createdOn = v.findViewById(R.id.created_on);
        }
    }

    public void updateData(List<Cpanel> cpanelList){
        this.cpanelList = cpanelList;
        notifyDataSetChanged();
    }
}
