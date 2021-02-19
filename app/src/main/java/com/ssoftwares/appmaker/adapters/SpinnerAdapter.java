package com.ssoftwares.appmaker.adapters;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.modals.TagType;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<TagType> tagTypeArrayList;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext,
                          ArrayList<TagType> tagTypeArrayList) {
        this.context = applicationContext;
        this.tagTypeArrayList = tagTypeArrayList;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return tagTypeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.layout_spinnertype, null);

        TextView names = (TextView) view.findViewById(R.id.text1);

        names.setText(tagTypeArrayList.get(i).getName());
        return view;
    }
}