package com.ssoftwares.appmaker.demoadapter;

import android.content.Context;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.demoutils.FontItem;

import java.util.List;


public class CustomSpinnerAdapter extends ArrayAdapter<FontItem> {
    LayoutInflater inflater;

    public CustomSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId,
                                @NonNull List<FontItem> objects) {
        super(context, resource, textViewResourceId, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return fontView(convertView, position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return fontView(convertView, position);
    }

    private View fontView(View convertView, int position) {

        FontItem fontItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = inflater.inflate(R.layout.spinneritem, null, false);

            holder.txtTitle = (TextView) rowview.findViewById(R.id.spinnerTextview);

            if (position == 0) {

            } else {
                String selectedFont = fontItem.getFontFamilyName();
                holder.txtTitle.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                        new StringBuilder("fonts/")
                        .append(selectedFont + ".ttf").toString()));
            }
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(fontItem.getFontFamilyName());


        return rowview;
    }

    private class viewHolder {
        TextView txtTitle;
    }
}
