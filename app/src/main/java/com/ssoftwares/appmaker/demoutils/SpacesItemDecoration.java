package com.ssoftwares.appmaker.demoutils;

import android.graphics.Rect;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int spaces;

    public SpacesItemDecoration(int spaces) {
        this.spaces = spaces;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.left = spaces;
            outRect.right = 0;

        } else {
            outRect.left = 0;
            outRect.right = spaces;
        }

    }
}
