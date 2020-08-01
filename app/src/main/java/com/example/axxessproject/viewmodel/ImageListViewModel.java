package com.example.axxessproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.axxessproject.models.ImageItem;
import com.example.axxessproject.repository.ImageRepository;

import java.util.List;

/**
 * View Model associated with ImageListActivity
 */
public class ImageListViewModel extends AndroidViewModel {
    private ImageRepository mImageRepository;
    private boolean mIsPerformingQuery;

    public ImageListViewModel(@NonNull Application application) {
        super(application);
        mImageRepository = ImageRepository.getInstance(application);
        mIsPerformingQuery = false;
    }


    public LiveData<List<ImageItem>> getImages(){
        return mImageRepository.getImages();
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mImageRepository.isQueryExhausted();
    }

    public void searchImageApi(String query, int pageNumber){
        mIsPerformingQuery = true;
        mImageRepository.searchImageApi(query, pageNumber);
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery && !isQueryExhausted().getValue()){
            mImageRepository.searchNextPage();
        }
    }

    public void setIsPerformingQuery(Boolean isPerformingQuery){
        mIsPerformingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery(){
        return mIsPerformingQuery;
    }
}
