package com.example.axxessproject.repository;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.axxessproject.AppExecutors;
import com.example.axxessproject.models.ImageItem;
import com.example.axxessproject.network.ImageApiClient;
import com.example.axxessproject.persistence.ImageDao;
import com.example.axxessproject.persistence.ImageDatabase;

import java.util.List;

/**
 * This is general repository class as part of MVVM architechture. All view models of activities
 * talk with this repository class to make network calls or db calls.
 */
public class ImageRepository {

    private static ImageRepository instance;
    private ImageApiClient mImageApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<ImageItem>> mImageItems = new MediatorLiveData<>();
    private ImageDao imageDao;

    public static ImageRepository getInstance(Context context){
        if(instance == null){
            instance = new ImageRepository(context);
        }
        return instance;
    }

    private ImageRepository(Context context){
        imageDao = ImageDatabase.getInstance(context).getImageDao();
        mImageApiClient = ImageApiClient.getInstance();
        initMediators();
    }

    private void initMediators(){
        LiveData<List<ImageItem>> recipeListApiSource = mImageApiClient.getImages();
        mImageItems.addSource(recipeListApiSource, new Observer<List<ImageItem>>() {
            @Override
            public void onChanged(@Nullable List<ImageItem> images) {
                if(images != null){
                    boolean isQueryFinished = false;
                    mImageItems.setValue(images);
                    if (images.size() == 0) mIsQueryExhausted.setValue(true);
                }
            }
        });
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    public LiveData<List<ImageItem>> getImages(){
        return mImageItems;
    }

    public LiveData<ImageItem> getImageById(String imageId){
        return imageDao.getImage(imageId);
    }

    public void saveImageItem(final ImageItem imageItem) {
        AppExecutors.getInstance().getBackGroundExecutor().submit(new Runnable() {
            @Override
            public void run() {
                imageDao.insertImageItem(imageItem);
            }
        });
    }

    public void searchImageApi(String query, int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mImageApiClient.searchImageApi(query, pageNumber);
    }

    public void searchNextPage(){
        searchImageApi(mQuery, mPageNumber + 1);
    }

    public LiveData<Boolean> isImageRequestTimedOut(){
        return mImageApiClient.isImageRequestTimedOut();
    }
}
