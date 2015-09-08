package com.scutteam.lvyou.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import org.w3c.dom.Text;


/**
 * Created by liujie on 15/8/18.
 */
public class DeletePlanDialog extends Dialog implements View.OnClickListener {
    String Tag = "DeletePlanDialog --> ";
    private Context mContext;
    private View contentView;
    private TextView tvSure;
    private TextView tvCancel;
    private String planLogicId;
    private DialogListener listener;


    public DeletePlanDialog(Context context, int theme) {
        super(context, theme);
    }

    public DeletePlanDialog(Context context, DialogListener listener){
        this(context);
        this.listener = listener;
    }

    public DeletePlanDialog(Context context) {
        this(context, R.style.ThemeDialogCustom);
        mContext = context;
        setCanceledOnTouchOutside(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        contentView = inflater.inflate(R.layout.dialog_delete, null);
        initView();
        initListener();
        setContentView(contentView);
    }

    private void initView() {
        Log.i(Tag, "initView");
        tvSure = (TextView)contentView.findViewById(R.id.dialog_delete_plan_sure);
        tvCancel = (TextView)contentView.findViewById(R.id.dialog_delete_plan_cancel);
    }

    private void initListener() {
        Log.i(Tag, "initListener");
        contentView.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        Log.i(Tag, "onClick");
        switch (view.getId()){
            case R.id.dialog_delete_plan_sure:
                deleteJourneyPlan();
                dismiss();
                break;

            case R.id.dialog_delete_plan_cancel:
                dismiss();
                break;

            default:
                break;
        }

    }

    private void deleteJourneyPlan(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("id", planLogicId);
        client.post(Constants.URL + "/user/trip.del.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    Toast.makeText(mContext, "删除订单成功", Toast.LENGTH_SHORT).show();
                    if(null != listener){
                        listener.refreshActivity("");
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

    public void setPlanLogicId(String planLogicId) {
        this.planLogicId = planLogicId;
    }
}
