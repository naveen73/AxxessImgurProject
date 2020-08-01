package com.example.axxessproject.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.axxessproject.R;
import com.example.axxessproject.models.ImageItem;
import com.example.axxessproject.viewholder.ImageItemViewHolder;
import com.example.axxessproject.viewholder.LoadingViewHolder;
import com.example.axxessproject.viewholder.SearchExhaustedViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the adapter class.This is responsible for displaying search results in a recycler view in
 * grid fashion.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ListPreloader.PreloadModelProvider<String>{
    private static String TAG = "ImageRecyclerAdapter";

    private static final int IMAGE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int EXHAUSTED_TYPE = 4;
    private RequestManager requestManager;

    private List<ImageItem> mImages = new ArrayList<>();
    private OnImageListener mOnImageListener;
    private ViewPreloadSizeProvider<String> preloadSizeProvider;

    public ImageRecyclerAdapter(OnImageListener mOnImageListener, RequestManager requestManager,
                                ViewPreloadSizeProvider<String> viewPreloadSizeProvider) {
        this.mOnImageListener = mOnImageListener;
        this.requestManager = requestManager;
        this.preloadSizeProvider = viewPreloadSizeProvider;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch (i){

            case IMAGE_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_image_list_item, viewGroup, false);
                return new ImageItemViewHolder(view, mOnImageListener, requestManager, preloadSizeProvider);
            }

            case LOADING_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }

            case EXHAUSTED_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }

            default:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_image_list_item, viewGroup, false);
                return new ImageItemViewHolder(view, mOnImageListener, requestManager, preloadSizeProvider);
            }
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if(itemViewType == IMAGE_TYPE){
            ((ImageItemViewHolder)viewHolder).onBind(mImages.get(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mImages.get(position).getTitle().equals("LOADING...")){
            return LOADING_TYPE;
        }
        else if(mImages.get(position).getTitle().equals("EXHAUSTED...")){
            return EXHAUSTED_TYPE;
        }
        else{
            return IMAGE_TYPE;
        }
    }

    public void setQueryExhausted(){
        ImageItem exhaustedImage = new ImageItem();
        exhaustedImage.setTitle("EXHAUSTED...");
        mImages.add(exhaustedImage);
        notifyDataSetChanged();
    }


    public void displayLoading(){
        if (!isLoading()) {
            ImageItem image = new ImageItem();
            image.setTitle("LOADING...");
            mImages.add(image);
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if(mImages != null){
            if(mImages.size() > 0){
                return mImages.get(mImages.size() - 1).getTitle().equals("LOADING...");
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if(mImages != null){
            return mImages.size();
        }
        return 0;
    }

    public void setImages(List<ImageItem> images){
        mImages = new ArrayList<>(images);
        notifyDataSetChanged();
    }

    public void clearList() {
        mImages = Collections.emptyList();
        notifyDataSetChanged();
    }

    public ImageItem getSelectedImage(int position){
        if(mImages != null){
            if(mImages.size() > 0){
                return mImages.get(position);
            }
        }
        return null;
    }

    @NonNull
    @Override
    public List<String> getPreloadItems(int position) {
        String url = mImages.get(position).getImageUrl();
        if(url == null || TextUtils.isEmpty(url)){
            return Collections.emptyList();
        }
        return Collections.singletonList(url);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull String item) {
        return requestManager.load(item);
    }
}
