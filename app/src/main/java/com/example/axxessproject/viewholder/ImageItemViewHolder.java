package com.example.axxessproject.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.axxessproject.R;
import com.example.axxessproject.adapter.OnImageListener;
import com.example.axxessproject.models.ImageItem;

public class ImageItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public AppCompatImageView image;
    OnImageListener onImageListener;
    RequestManager requestManager;
    ViewPreloadSizeProvider viewPreloadSizeProvider;

    public ImageItemViewHolder(@NonNull View itemView, OnImageListener onImageListener,
                               RequestManager requestManager,ViewPreloadSizeProvider preloadSizeProvider) {
        super(itemView);

        this.onImageListener = onImageListener;
        image = itemView.findViewById(R.id.image);
        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = preloadSizeProvider;

        itemView.setOnClickListener(this);
    }

    public void onBind(ImageItem imageItem){
        requestManager
                .load(imageItem.getImageUrl())
                .into(image);
        viewPreloadSizeProvider.setView(image);
    }

    @Override
    public void onClick(View v) {
        onImageListener.onImageClick(getAdapterPosition());
    }
}





