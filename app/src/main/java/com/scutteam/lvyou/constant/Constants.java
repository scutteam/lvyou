package com.scutteam.lvyou.constant;

/**
 * Created by admin on 15/7/19.
 */
public class Constants {
    
    public static String PREFERENCE_NAME = "lvyou";
    public static String URL = "http://112.74.196.140:58080/etbear/";
    public static String IMAGE_URL = "http://112.74.196.140:58080/etbear/image/";
    public static final String DESCRIPTOR = "com.umeng.share";

    private static final String TIPS = "请移步官方网站 ";
    private static final String END_TIPS = ", 查看相关说明.";
    public static final String TENCENT_OPEN_URL = TIPS + "http://wiki.connect.qq.com/android_sdk使用说明"
            + END_TIPS;
    public static final String PERMISSION_URL = TIPS + "http://wiki.connect.qq.com/openapi权限申请"
            + END_TIPS;

    public static final String SOCIAL_LINK = "http://www.umeng.com/social";
    public static final String SOCIAL_TITLE = "友盟社会化组件帮助应用快速整合分享功能";
    public static final String SOCIAL_IMAGE = "http://www.umeng.com/images/pic/banner_module_social.png";

    public static final String SOCIAL_CONTENT = "友盟社会化组件（SDK）让移动应用快速整合社交分享功能，我们简化了社交平台的接入，为开发者提供坚实的基础服务：（一）支持各大主流社交平台，" +
            "（二）支持图片、文字、gif动图、音频、视频；@好友，关注官方微博等功能" +
            "（三）提供详尽的后台用户社交行为分析。http://www.umeng.com/social";

    public static final int REQUEST_MODIFY_USER_NAME = 2002;
    public static final int REQUEST_MODIFY_USER_REAL_NAME = 2003;
    public static final int REQUEST_MODIFY_USER_PHONE = 2004;
    public static final int REQUEST_SELECT_STAY = 2005;
    public static final int REQUEST_SELECT_VIEW_SPOT = 2006;
    
    public static final int MODIFY_NAME_SUCCESS = 3000;
    public static final int MODIFY_REAL_NAME_SUCCESS = 4000;
    public static final int MODIFY_BIND_PHONE_SUCCESS = 5000;
    public static final int RESULT_SELECT_STAY = 6000;
    public static final int RESULT_SELECT_VIEW_SPOT = 7000;

    public static class Sp{
        public static final String PRE_NAME = "easy_bear";
    }
}
