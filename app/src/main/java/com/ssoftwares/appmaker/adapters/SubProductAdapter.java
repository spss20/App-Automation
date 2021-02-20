package com.ssoftwares.appmaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.BuilderActivity;
import com.ssoftwares.appmaker.modals.SubProduct;

import java.util.List;

public class SubProductAdapter extends RecyclerView.Adapter<SubProductAdapter.SubProductViewHolder> {

    private Context mContext;
    private List<SubProduct> subProductList;

    public SubProductAdapter(Context mContext, List<SubProduct> subProductList) {
        this.mContext = mContext;
        this.subProductList = subProductList;
    }

    @NonNull
    @Override
    public SubProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.subproduct_item , parent , false);
        return new SubProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubProductViewHolder holder, int position) {
        SubProduct subProduct = subProductList.get(position);
        holder.subproductName.setText(subProduct.getName());

        if (subProduct.getImages()!= null && subProduct.getImages().size() != 0)
            Picasso.get().load(subProduct.getImages().get(0).getImageUrl()).into(holder.subproductImage);

        holder.itemView.setOnClickListener(v -> {
            if (subProduct.getApischema() == null){
                Toast.makeText(mContext, "Error: Json schema invalid, contact admin to fix this problem", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(mContext, BuilderActivity.class);
                intent.putExtra("config", subProduct.getApischema().toString());
                intent.putExtra("subproduct_name" , subProduct.getName());
                intent.putExtra("subproduct_id" , subProduct.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subProductList.size();
    }

    public static class SubProductViewHolder extends RecyclerView.ViewHolder {
        ImageView subproductImage;
        TextView subproductName;
        public SubProductViewHolder(@NonNull View itemView) {
            super(itemView);
            subproductImage = itemView.findViewById(R.id.subproduct_image);
            subproductName = itemView.findViewById(R.id.subproduct_name);
        }
    }

    public void updateData(List<SubProduct> subProductList){
        this.subProductList = subProductList;
        notifyDataSetChanged();
    }
}
