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
    public static final int REQUEST_SELECT_RECOMMEND_TRIP = 2007;
    public static final int REQUEST_LOGIN = 2008;
    public static final int REQUEST_GET_VIEW_SPOT_DETAIL = 2009;
    public static final int REQUEST_GET_RECOMMENT_DETAIL = 2010;
    
    public static final int MODIFY_NAME_SUCCESS = 3000;
    public static final int MODIFY_REAL_NAME_SUCCESS = 4000;
    public static final int MODIFY_BIND_PHONE_SUCCESS = 5000;
    public static final int RESULT_SELECT_STAY = 6000;
    public static final int RESULT_SELECT_VIEW_SPOT = 7000;
    public static final int RESULT_SELECT_RECOMMEND_TRIP = 8000;
    public static final int RESULT_LOGIN = 9000;
    public static final int RESULT_GET_VIEW_SPOT_DETAIL = 10000;
    public static final int RESULT_GET_RECOMMENT_DETAIL = 11000;

    public static class Sp{
        public static final String PRE_NAME = "easy_bear";
    }

    public static class Account{
        public static final String QQ_APP_ID = "1104822256";
        public static final String QQ_APP_KEY = "67flFLahNyh2nQPu";
        public static final String WECHAT_APP_ID = "wx3df763a18258c764";
        public static final String WECHAT_KEY = "7f8421f48f3553895454a55efcfeee1e";
    }

    public static class Config{
        public static final int IMAGE_WIDTH = 480;
        public static final int IMAGE_HEIGHT = 360;

        /* 商户PID **/
        public static final String PARTNER = "2088611807463433";
        /* 商户收款账号 **/
        public static final String SELLER = "huangzibin172@qq.com";
        /* 商户私钥，pkcs8格式 **/
        public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKND9721eDw0kOp4" +
                "JT1OOo6HnV04UYEgqJQFLVnxmxOc8+YLDXWGlJtnSDP/iUqxFBqODz+SBW/dZ2Sw" +
                "hsSltmgq9CoKtD2nEjJXgnVpeJmA8kcZZVVY7DhfQoz+9Rph3tu8ggGegpz/eJ7Y" +
                "niDY6vbJ1+zH4izLIawtsfLvD4VzAgMBAAECgYEAi6V/Kg0mIpjzuxm5AI8BFxh4" +
                "SVv6mvBMQQ+MkvpNIqGFHKcng/bw9VuKLq/Lxs9f2rbX5BAKBnziCwXSwDDdqy4U" +
                "aie1VAeHE1cvjZoPOa+oYQwHLGTzCk1jd6Oa3fPs/ERmnXThAfO5XC6IODffEvj1" +
                "IgbW8r/3dfY0IAqs+AECQQDNtoVZIL2xF+rOBEHR6qHOnbQ00edOdQ0BScBi3zrz" +
                "ltVBaAlLXCShEQSQ60m0Pb8aQ9ho3wx8NC0M4A3IJW/zAkEAyy0XimBoMOTuEjF+" +
                "MFukVD5bfGCbnhsIJT5NXOe42v7rCW2gXo5hee/Xefyu4aNpxmmKEYRqeqZfY9oW" +
                "Qqh0gQJAexwxX66ld3d90T9g+LS3k0R0FWxNRnnsh4nQthssV508EQGnFk+VqELb" +
                "/bKiEFknphtWRyS9fxNmpp7sAIzPGwJAfWPAyXeiFbrTaor889861Xr+fw4oPDU2" +
                "m7WNjS69GPqGF3V+qZx5cAWjF/fn/f0aBIeR3Cm6hSM0b61iW5UXAQJBAKUNiVLk" +
                "ZDq97tlSNPaoT3Kqv1LSrpc37SqpFKb0aD1b+1rkIljFW0UTO2Qz3bYObCFW4PIV" +
                "tmycKTVGKe7pw+Y=";
        /* 支付宝公钥 **/
        public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    }
}
