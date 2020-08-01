package com.example.axxessproject.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.axxessproject.AppExecutors;
import com.example.axxessproject.models.ImageItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
/**
 * This class provides function to make network call and fetch results from as live data. Internally
 * it uses retrofit ImageApi interface to make calls to network. Repository class uses this calls to
 * get live data returned from network
 */
public class ImageApiClient {

    private static final String TAG = "ImageApiClient";

    private static ImageApiClient instance;
    private MutableLiveData<List<ImageItem>> mImageItems;
    private RetrieveImagesRunnable mRetrieveImagesRunnable;
    private MutableLiveData<Boolean> mImageRequestTimeout = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();

    public static ImageApiClient getInstance(){
        if(instance == null){
            instance = new ImageApiClient();
        }
        return instance;
    }

    private ImageApiClient(){
        mImageItems = new MutableLiveData<>();
    }

    public LiveData<List<ImageItem>> getImages(){
        return mImageItems;
    }

    public LiveData<Boolean> isImageRequestTimedOut(){
        return mImageRequestTimeout;
    }

    public void searchImageApi(String query, int pageNumber){
        if(mRetrieveImagesRunnable != null){
            mRetrieveImagesRunnable = null;
        }
        mRetrieveImagesRunnable = new RetrieveImagesRunnable(query, pageNumber);
        AppExecutors.getInstance().getBackGroundExecutor().submit(mRetrieveImagesRunnable);
    }

    private class RetrieveImagesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveImagesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getImages(query, pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<ImageItem> list = new ArrayList<ImageItem>(((ImageSearchResponse)response.body()).getImageItems());
                    if(pageNumber == 1){
                        mImageItems.postValue(list);
                    }
                    else{
                        List<ImageItem> currentImages = mImageItems.getValue();
                        currentImages.addAll(list);
                        mImageItems.postValue(currentImages);
                    }
                }
                else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    mImageItems.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mImageItems.postValue(null);
            }

        }

        private Call<ImageSearchResponse> getImages(String query, int pageNumber){
            return ServiceGenerator.getImageApi().searchImages(query, String.valueOf(pageNumber));
        }

    }

}
