package com.scutteam.lvyou.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.PlanDetail;
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
public class SubmitJourneyPlanDialog extends Dialog implements View.OnClickListener {
    String Tag = "SubmitJourneyPlanDialog --> ";
    private Context mContext;
    private View contentView;
    private TextView tvHint;
    private EditText etName;
    private EditText etPhone;
    private TextView tvSubmit;

    private String plan_logic_id;

    private DialogListener listener;


    public SubmitJourneyPlanDialog(Context context, int theme) {
        super(context, theme);
    }

    public SubmitJourneyPlanDialog(Context context) {
        this(context, R.style.ThemeDialogCustom);
        mContext = context;
        setCanceledOnTouchOutside(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        contentView = inflater.inflate(R.layout.dialog_submit_journey_plan, null);
        initView();
        initListener();
        setContentView(contentView);
    }

    public SubmitJourneyPlanDialog(Context context, String plan_logic_id, DialogListener listener){
        this(context);
        this.plan_logic_id = plan_logic_id;
        this.listener = listener;
    }

    private void initView() {
        Log.i(Tag, "initView");
        tvHint = (TextView)contentView.findViewById(R.id.dialog_submit_journey_plan_hint);
        etName = (EditText)contentView.findViewById(R.id.dialog_submit_journey_plan_name);
        etPhone = (EditText)contentView.findViewById(R.id.dialog_submit_journey_plan_phone);
        tvSubmit = (TextView)contentView.findViewById(R.id.dialog_submit_journey_plan_submit);
    }

    private void initListener() {
        Log.i(Tag, "initListener");
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(Tag, "onClick");
        switch (view.getId()) {
            case R.id.dialog_submit_journey_plan_submit:
                doSubmit();
                break;

            default:
                break;
        }
    }

    private void doSubmit() {
        String name = "";
        String phone = "";
        name = etName.getText().toString();
        phone = etPhone.getText().toString();
        if(name.length() == 0){
            Toast.makeText(mContext, "名字不能为空", Toast.LENGTH_SHORT).show();
        }else if(phone.length() == 0){
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else{
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("sessionid", LvYouApplication.getSessionId());
            params.put("id", plan_logic_id);
            params.put("name", name);
            params.put("tel", phone);
            client.post(Constants.URL + "/user/trip.submit_contact.do", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("liujie", response.toString());
                    if (0 == response.optInt("code")) {
                        listener.refreshActivity(0 + "");
                    } else {
                        Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("liujie", responseString.toString());
                    Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        }
    }
}
