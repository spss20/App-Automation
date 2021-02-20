package com.ssoftwares.appmaker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ssoftwares.appmaker.fragments.ImageFragment;
import com.ssoftwares.appmaker.modals.Attachment;

import java.util.List;

public class ProductBannerAdapter extends FragmentStateAdapter {

    private List<Attachment> imagesList;

    public ProductBannerAdapter(@NonNull FragmentActivity fragmentActivity , List<Attachment> images) {
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

    public void updateData(List<Attachment> imagesList){
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }
}
