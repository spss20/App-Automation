package com.ssoftwares.appmaker.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssoftwares.appmaker.R;
import com.ssoftwares.appmaker.activities.SplashBuilderActivity;
import com.ssoftwares.appmaker.demoadapter.ThumbnailAdapter;
import com.ssoftwares.appmaker.demoutils.Bitmap_Utils;
import com.ssoftwares.appmaker.demoutils.SpacesItemDecoration;
import com.ssoftwares.appmaker.interfaces.FiltersListFragmentListener;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class FilterListFragment extends BottomSheetDialogFragment implements FiltersListFragmentListener {

    RecyclerView recyclerView;
   ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;


    FiltersListFragmentListener listener;
    static FilterListFragment instance;

    static Bitmap bitmap;

    public static FiltersListFragmentListener getInstance(Bitmap bitmapSave) {
        bitmap=bitmapSave;
        if(instance == null){
            instance=new FilterListFragment();

        }
        return instance;
    }

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    public FilterListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_filter_list, container, false);

        thumbnailItems =new ArrayList<>();
        adapter=new ThumbnailAdapter(thumbnailItems,this,getActivity());
        recyclerView=(RecyclerView)itemView.findViewById(R.id.filter_recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(adapter);

        displayThumbnail(bitmap);

        return itemView;

    }

    public void displayThumbnail(final Bitmap bitmap) {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImg;
                if(bitmap == null){
                    thumbImg = Bitmap_Utils.getBitmapFromAssets(getActivity(), SplashBuilderActivity.picture_name,100,100);
                }else {
                    thumbImg= Bitmap.createScaledBitmap(bitmap,100,100,false);
                }

                if(thumbImg == null){
                    return;
                }
                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                //add normal bitmap first
                ThumbnailItem thumbnailItem= new ThumbnailItem();
                thumbnailItem.image=thumbImg;
                thumbnailItem.filterName="Normal";
                ThumbnailsManager.addThumb(thumbnailItem);


                List<Filter> filters= FilterPack.getFilterPack(getActivity());
                for (Filter filter : filters){
                    ThumbnailItem tI=new ThumbnailItem();
                    tI.image=thumbImg;
                    tI.filter=filter;
                    tI.filterName=filter.getName();
                    ThumbnailsManager.addThumb(tI);
                }

                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        };
        new Thread(r).start();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        if(listener != null){
            listener.onFilterSelected(filter);
        }
    }
}
