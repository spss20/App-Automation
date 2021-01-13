package com.ssoftwares.appmaker.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.interfaces.OnClickInterface;
import com.ssoftwares.appmaker.modals.Demo;
import com.ssoftwares.appmaker.modals.Image;

import java.io.File;
import java.util.List;

public class DemosAdapter extends RecyclerView.Adapter<DemosAdapter.DemoViewHolder> {

    private Context mContext;
    private List<Demo> demoList;
    private OnClickInterface onClickInterface;

    public DemosAdapter(Context mContext, List<Demo> demoList, OnClickInterface onClick) {
        this.mContext = mContext;
        this.demoList = demoList;
        this.onClickInterface = onClick;
    }

    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_demo_item , parent , false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolder holder, int position) {
        Demo demo = demoList.get(position);
        holder.demoCredentials.setText(demo.getCredentials());
        Picasso.get().load(demo.getImage().getImageUrl()).into(holder.demoImage);
        holder.demoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.onClick(demo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return demoList.size();
    }

    public static class DemoViewHolder extends RecyclerView.ViewHolder {
        CardView demoCardView;
        ImageView demoImage;
        TextView demoCredentials;
        public DemoViewHolder(@NonNull View itemView) {
            super(itemView);
            demoCardView = itemView.findViewById(R.id.demo_cardview);
            demoImage = itemView.findViewById(R.id.demo_image);
            demoCredentials = itemView.findViewById(R.id.demo_credentials);
        }
    }

    public void updateData(List<Demo> demoList){
        this.demoList = demoList;
        notifyDataSetChanged();
    }
}
