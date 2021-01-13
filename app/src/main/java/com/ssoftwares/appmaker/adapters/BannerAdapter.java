package com.ssoftwares.appmaker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ssoftwares.appmaker.fragments.BannerFragment;
import com.ssoftwares.appmaker.modals.Banner;

import java.util.List;

public class BannerAdapter extends FragmentStateAdapter {

    private List<Banner> bannerList;

    public BannerAdapter(@NonNull FragmentActivity fragmentActivity , List<Banner> bannerList) {
        super(fragmentActivity);
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new BannerFragment(bannerList.get(position));
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public void updateData(List<Banner> bannerList){
        this.bannerList = bannerList;
        notifyDataSetChanged();
    }
}
