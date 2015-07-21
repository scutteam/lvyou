package com.scutteam.lvyou.application;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;

public class LvYouApplication extends Application {

    public static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
//                .createDefault(this);
//
//        //Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);

        ActiveAndroid.initialize(this);
    }
    
    public static Context getInstance() {
        return context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ActiveAndroid.dispose();
    }
}
