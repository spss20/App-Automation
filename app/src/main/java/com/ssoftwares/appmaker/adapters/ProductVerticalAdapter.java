package com.ssoftwares.appmaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.ProductDetailActivity;
import com.ssoftwares.appmaker.modals.Product;

import java.util.List;

public class ProductVerticalAdapter extends RecyclerView.Adapter<ProductVerticalAdapter.ProductViewHolder> {

    private final Context mContext;
    private List<Product> productList;

    public ProductVerticalAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductVerticalAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item_vertical , parent , false);
        return new ProductVerticalAdapter.ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductVerticalAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        if (product.getImages().size() != 0 && product.getImages().get(0).getImageUrl() != null)
            Picasso.get().load(product.getImages().get(0).getImageUrl()).into(holder.productImage);

        holder.productName.setText(product.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , ProductDetailActivity.class);
                intent.putExtra("data" , product);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
        }
    }

    public void updateData(List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }
}
