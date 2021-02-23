package com.ssoftwares.appmaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.interfaces.ProductSelectedListener;
import com.ssoftwares.appmaker.modals.Category;
import com.ssoftwares.appmaker.modals.Attachment;
import com.ssoftwares.appmaker.modals.Product;

import java.util.List;

public class ProductBottomSheetAdapter extends RecyclerView.Adapter<ProductBottomSheetAdapter.ProductViewHolder> {

    private Context mContext;
    private List<Product> productList;
    private ProductSelectedListener listener;

    public ProductBottomSheetAdapter(Context mContext, List<Product> productList , ProductSelectedListener listener) {
        this.mContext = mContext;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item_2 , parent , false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productShortDesc.setText(product.getShort_description());
        List<Category> categories = product.getCategories();
        for (int i = 0; i< categories.size(); i++){
            if (i == categories.size()-1){
                holder.productCategory.append(categories.get(i).getName());
            } else
                holder.productCategory.append(categories.get(i).getName() + ", ");
        }
        if (product.getImages().size() != 0) {
            Attachment image = product.getImages().get(0);
            if (image.getFormats() != null) {
                Picasso.get().load(image.getFormats().getThumbnail().getImageUrl())
                        .into(holder.productImage);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productShortDesc;
        TextView productCategory;

        public ProductViewHolder(@NonNull View v) {
            super(v);
            productImage = v.findViewById(R.id.product_image);
            productName = v.findViewById(R.id.product_name);
            productShortDesc = v.findViewById(R.id.product_short_desc);
            productCategory = v.findViewById(R.id.product_category);
        }
    }

    public void updateData(List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }
}
