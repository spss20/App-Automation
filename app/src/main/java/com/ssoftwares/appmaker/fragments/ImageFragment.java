package com.ssoftwares.appmaker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.modals.Banner;
import com.ssoftwares.appmaker.modals.Image;

public class ImageFragment extends Fragment {

    private final Image image;

    public ImageFragment(Image image){
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        if (image != null){
            ImageView imageView = view.findViewById(R.id.banner_image);
            Picasso.get().load(image.getImageUrl()).into(imageView);
        }
        return view;
    }
}
