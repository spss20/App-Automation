package com.ssoftwares.appmaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.modals.Banner;

public class BannerFragment extends Fragment {


    private Banner banner;

    public BannerFragment() {
        // Required empty public constructor
    }

    public BannerFragment(Banner banner){
        this.banner = banner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        if (banner != null){
            ImageView imageView = view.findViewById(R.id.banner_image);
            Picasso.get().load(banner.getImage().getImageUrl()).into(imageView);
        }
        return view;
    }
}