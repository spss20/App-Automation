package com.ssoftwares.appmaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.MainActivity;
import com.ssoftwares.appmaker.activities.ProductsActivity;
import com.ssoftwares.appmaker.fragments.ProductsFragment;
import com.ssoftwares.appmaker.modals.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private final Context mContext;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.categoryList = categoryList;
        mContext = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent,
                false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryAppsCount.setText(category.getProducts().size() + " Apps");
        Picasso.get().load(category.getImage().getFormats().getSmall().getImageUrl()).into(holder.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category_id" , category.getId());
                bundle.putString("category_name" , category.getName());

//                ProductsFragment fragment = new ProductsFragment();
//                fragment.setArguments(bundle);
//                FragmentTransaction transaction =   ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
//                transaction.addToBackStack(null);
//                transaction.replace(R.id.container , fragment);
//                transaction.commit();
                Intent intent = new Intent(mContext, ProductsActivity.class);
                intent.putExtra("category_id", category.getId());
                intent.putExtra("category_name", category.getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView categoryAppsCount;
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryAppsCount = itemView.findViewById(R.id.category_apps_count);
            categoryImage = itemView.findViewById(R.id.category_image);
        }
    }

    public void updateData(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }
}
