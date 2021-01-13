package com.ssoftwares.appmaker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ssoftwares.appmaker.fragments.BannerFragment;
import com.ssoftwares.appmaker.fragments.ImageFragment;
import com.ssoftwares.appmaker.modals.Image;

import java.util.List;

public class ProductBannerAdapter extends FragmentStateAdapter {

    private List<Image> imagesList;

    public ProductBannerAdapter(@NonNull FragmentActivity fragmentActivity , List<Image> images) {
        super(fragmentActivity);
        this.imagesList = images;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ImageFragment(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public void updateData(List<Image> imagesList){
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }
}
