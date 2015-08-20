package com.scutteam.lvyou.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


/**
 * Created by liujie on 15/8/18.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {
    String Tag = "ShareDialog --> ";
    private Context mContext;
    private View contentView;
    private LinearLayout llShareToWechat;
    private LinearLayout llShareToQQ;

    public ShareDialog(Context context, int theme) {
        super(context, theme);
    }

    public ShareDialog(Context context) {
        this(context, R.style.ThemeDialogCustom);
        mContext = context;
        setCanceledOnTouchOutside(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        contentView = inflater.inflate(R.layout.dialog_share, null);
        initView();
        initListener();
        setContentView(contentView);
    }

    private void initView() {
        Log.i(Tag, "initView");
        llShareToWechat = (LinearLayout) contentView.findViewById(R.id.dialog_share_wechat);
        llShareToQQ = (LinearLayout) contentView.findViewById(R.id.dialog_share_qq);
    }

    private void initListener() {
        Log.i(Tag, "initListener");
        contentView.setOnClickListener(this);
        llShareToWechat.setOnClickListener(this);
        llShareToQQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(Tag, "onClick");
        switch (view.getId()) {
            case R.id.dialog_share_wechat:
                doShareToWechat();
                break;

            case R.id.dialog_share_qq:
                doShareToQQ();
                break;

            default:
                break;
        }
    }

    private void doShareToQQ() {
        // 首先在您的Activity中添加如下成员变量
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, Constants.Account.QQ_APP_ID, Constants.Account.QQ_APP_KEY);
        qqSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("分享链接给团友，让团友也获取保险");
        qqShareContent.setTitle("获取保险");
        qqShareContent.setShareImage(new UMImage(mContext, R.drawable.app_icon));
        qqShareContent.setTargetUrl("http://www.baidu.com");
        mController.setShareMedia(qqShareContent);

        mController.postShare(mContext, SHARE_MEDIA.QQ,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                        Log.i("liujie", "start");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                    }
                });
        dismiss();
    }

    private void doShareToWechat() {
        Log.i(Tag, "share to wechat");
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

        UMWXHandler wxHandler = new UMWXHandler(mContext, Constants.Account.WECHAT_APP_ID, Constants.Account.WECHAT_KEY);
        wxHandler.addToSocialSDK();

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent("分享链接给团友，让团友也获取保险");
        //设置title
        weixinContent.setTitle("获取保险");
        weixinContent.setShareImage(new UMImage(mContext, R.drawable.app_icon));
        //设置分享内容跳转URL
        weixinContent.setTargetUrl("http://www.baidu.com");
        mController.setShareMedia(weixinContent);

        mController.postShare(mContext, SHARE_MEDIA.WEIXIN,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                    }
                });

        dismiss();
    }

}
