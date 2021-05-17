package com.ssoftwares.appmaker.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.ProductDetailActivity;
import com.ssoftwares.appmaker.modals.Banner;

public class BannerFragment extends Fragment {


    private Banner banner;


    public BannerFragment() {
        // Required empty public constructor
    }

    public BannerFragment(Banner banner) {
        this.banner = banner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        if (banner != null) {
            ImageView imageView = view.findViewById(R.id.banner_image);
            Picasso.get().load(banner.getImage().getFormats().getMedium().getImageUrl()).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (banner.getProduct() != null) {
                        if (banner.getProduct().getId() != null) {
                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                            intent.putExtra("product_id", banner.getProduct().getId());
                            intent.putExtra("product_name", banner.getProduct().getName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "No Product Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "No Product Found", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

        return view;
    }
}