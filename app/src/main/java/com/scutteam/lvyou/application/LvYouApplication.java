package com.scutteam.lvyou.application;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LvYouApplication extends Application {

    public static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        ImageLoader.getInstance().init(configuration);
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
