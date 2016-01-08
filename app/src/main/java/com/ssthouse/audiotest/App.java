package com.ssthouse.audiotest;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by ssthouse on 2016/1/7.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
