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


/**
 * Created by liujie on 15/8/18.
 */
public class ShareDialog extends Dialog implements View.OnClickListener{
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
    private void initView(){
        Log.i(Tag, "initView");
        llShareToWechat = (LinearLayout)contentView.findViewById(R.id.dialog_share_wechat);
        llShareToQQ = (LinearLayout)contentView.findViewById(R.id.dialog_share_qq);
    }

    private void initListener(){
        Log.i(Tag , "initListener");
        contentView.setOnClickListener(this);
        llShareToWechat.setOnClickListener(this);
        llShareToQQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(Tag, "onClick");
        switch (view.getId()){
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

    private void doShareToQQ(){
        // 首先在您的Activity中添加如下成员变量
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)mContext, Constants.Account.QQ_APP_ID, Constants.Account.QQ_APP_KEY);
        qqSsoHandler.addToSocialSDK();

        // 设置分享内容
        mController.setShareContent("分享链接给团友，让团友也获取保险");
        mController.setAppWebSite(SHARE_MEDIA.QQ, "www.baidu.com");

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("分享链接给团友，让团友也获取保险");
        qqShareContent.setTitle("hello, title");
        qqShareContent.setShareImage(new UMImage(mContext , R.drawable.icon_left_arrow));
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
                        if (eCode == 200) {
                            Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
                        } else {
                            String eMsg = "";
                            if (eCode == -101) {
                                eMsg = "没有授权";
                            }
                        }
                    }
                });
        dismiss();
    }

    private void doShareToWechat(){
        Log.i(Tag, "share to wechat");
        dismiss();
    }
}
