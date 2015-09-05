package com.scutteam.lvyou.application;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class LvYouApplication extends Application {

    public static Context context;
    public static String session_id;
    public static String user_id;
    public static String screen_name;
    public static String image_profile_url;
    public static String real_name;
    public static String sex;
    public static String phone;
    
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        ActiveAndroid.initialize(this);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public static void setReal_name(String real_name) {
        LvYouApplication.real_name = real_name;
    }

    public static void setSex(String sex) {
        LvYouApplication.sex = sex;
    }

    public static void setPhone(String phone) {
        LvYouApplication.phone = phone;
    }

    public static String getScreenName() {
        return screen_name;
    }

    public static void setScreenName(String screen_name) {
        LvYouApplication.screen_name = screen_name;
    }

    public static String getImageProfileUrl() {
        return image_profile_url;
    }

    public static void setImageProfileUrl(String image_profile_url) {
        LvYouApplication.image_profile_url = image_profile_url;
    }

    public static String getSessionId() {
        return session_id;
    }
    
    public static void clearAllInfo() {
        screen_name = null;
        image_profile_url = null;
        real_name = null;
        sex = null;
        phone = null;
        user_id = null;
    }

    public static void setSessionId(String session_id) {
        LvYouApplication.session_id = session_id;
    }

    public static void setUserId(String uid){
        user_id = uid;
    }

    public static String getUserId(){
        return user_id;
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
