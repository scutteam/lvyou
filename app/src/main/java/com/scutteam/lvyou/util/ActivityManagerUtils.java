package com.scutteam.lvyou.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-21
 * TODO Activity收集以及释放
 */

public class ActivityManagerUtils {

    private ArrayList<Activity> activityList = new ArrayList<Activity>();

    private static ActivityManagerUtils activityManagerUtils;

    private ActivityManagerUtils() {

    }

    public static ActivityManagerUtils getInstance() {
        if (null == activityManagerUtils) {
            activityManagerUtils = new ActivityManagerUtils();
        }
        return activityManagerUtils;
    }

    public Activity getTopActivity() {
        return activityList.get(activityList.size() - 1);
    }

    public void addActivity(Activity ac) {
        activityList.add(ac);
    }

    public void removeActivity(Activity ac) {
        activityList.remove(ac);
    }

    public void removeAllActivity() {
        for (Activity ac : activityList) {
            if (null != ac) {
                if (!ac.isFinishing()) {
                    ac.finish();
                }
                ac = null;
            }
        }
        activityList.clear();
    }
}
