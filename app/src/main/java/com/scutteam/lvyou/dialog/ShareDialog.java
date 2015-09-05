package com.scutteam.lvyou.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
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

import org.apache.http.Header;
import org.json.JSONObject;


/**
 * Created by liujie on 15/8/18.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {
    String Tag = "ShareDialog --> ";
    private Context mContext;
    private View contentView;
    private LinearLayout llShareToWechat;
    private LinearLayout llShareToQQ;
    private String planLogicId;
    private String shareUrl;


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

    public void setPlanLogicId(String planLogicId) {
        this.planLogicId = planLogicId;
    }

    @Override
    public void onClick(final View view) {
        Log.i(Tag, "onClick");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("planId", planLogicId);
        client.post(Constants.URL + "/main/common.share_insurance_url.json", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    shareUrl = response.optString("data");
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
                } else {
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("liujie", responseString.toString());
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
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
        qqShareContent.setTargetUrl(shareUrl);
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
        weixinContent.setTargetUrl(shareUrl);
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
