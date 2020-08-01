package com.example.axxessproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.axxessproject.adapter.ImageRecyclerAdapter;
import com.example.axxessproject.adapter.OnImageListener;
import com.example.axxessproject.models.ImageItem;
import com.example.axxessproject.viewmodel.ImageListViewModel;

import java.util.List;

/**
* This is the Launcher activity which displays search screen along with results in a recycler view.
*/
public class ImageListActivity extends AppCompatActivity implements OnImageListener {
    private static final String TAG = "RecipeListActivity";

    private ProgressBar mProgressBar;
    private ImageListViewModel mImageListViewModel;
    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter mAdapter;
    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setIconifiedByDefault(false);

        mImageListViewModel = ViewModelProviders.of(this).get(ImageListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        initSearchView();
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        mSearchView.requestFocus();
    }
    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .override(80, 80);
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    private void initRecyclerView(){
        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();
        mAdapter = new ImageRecyclerAdapter(this, initGlide(), viewPreloader);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));

        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<String>(
                Glide.with(this),
                mAdapter,
                viewPreloader,
                60);

        mRecyclerView.addOnScrollListener(preloader);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!mRecyclerView.canScrollVertically(1)){
                    Boolean queryExh = mImageListViewModel.isQueryExhausted().getValue();
                    if (queryExh == null || !queryExh) {
                        mAdapter.displayLoading();
                        mImageListViewModel.searchNextPage();
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void subscribeObservers(){
        mImageListViewModel.getImages().observe(this, new Observer<List<ImageItem>>() {
            @Override
            public void onChanged(@Nullable List<ImageItem> recipes) {
                showProgressBar(false);
                mImageListViewModel.setIsPerformingQuery(false);
                if(recipes != null){
                    mAdapter.setImages(recipes);
                }
            }
        });

        mImageListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d(TAG, "onChanged: the query is exhausted..." + aBoolean);
                if(aBoolean) {
                    mAdapter.setQueryExhausted();
                }
            }
        });
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                Glide.with(ImageListActivity.this).clear(this);
                mAdapter.clearList();
                showProgressBar(true);
                mImageListViewModel.searchImageApi(s, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }



    public void showProgressBar(boolean visibility){
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onImageClick(int position) {
        ImageItem imageItem = mAdapter.getSelectedImage(position);
        if (imageItem.getImageUrl() != null) {
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra("image", imageItem);
            startActivity(intent);
        }
    }
}