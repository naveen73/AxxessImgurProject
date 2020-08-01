package com.example.axxessproject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This class provides the executor so that background runnable can be submitted or scheduled on it
 */
public class AppExecutors {

    private static AppExecutors instance;

    public static AppExecutors getInstance(){
        if(instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

    private final ScheduledExecutorService mBackGroundExecutor = Executors.newScheduledThreadPool(2);

    public ScheduledExecutorService getBackGroundExecutor(){
        return mBackGroundExecutor;
    }
}
