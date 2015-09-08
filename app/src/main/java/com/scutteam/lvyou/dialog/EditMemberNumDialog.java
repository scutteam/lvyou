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
public class EditMemberNumDialog extends Dialog implements View.OnClickListener {
    String Tag = "EditMemberNumDialog --> ";
    private Context mContext;
    private View contentView;
    private EditText etNum;
    private TextView tvSubmit;
    private int minNum = 0;
    private int maxNum = 0;

    private DialogListener dialogListener;


    public EditMemberNumDialog(Context context, int theme) {
        super(context, theme);
    }

    public EditMemberNumDialog(Context context) {
        this(context, R.style.ThemeDialogCustom);
        mContext = context;
        setCanceledOnTouchOutside(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        contentView = inflater.inflate(R.layout.dialog_edit_member_num, null);
        initView();
        initListener();
        setContentView(contentView);
    }

    private void initView() {
        Log.i(Tag, "initView");
        etNum = (EditText) contentView.findViewById(R.id.dialog_edit_member_num_num);
        tvSubmit = (TextView)contentView.findViewById(R.id.dialog_edit_member_num_submit);
    }

    private void initListener() {
        Log.i(Tag, "initListener");
        contentView.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        Log.i(Tag, "onClick");
        switch (view.getId()){
            case R.id.dialog_edit_member_num_submit:
                String numString = etNum.getText().toString();
                if(!"".equals(numString)) {
                    int num = Integer.parseInt(numString);
                    if(num < minNum){
                        Toast.makeText(mContext, "至少" + minNum + "人", Toast.LENGTH_SHORT).show();
                    }else if(num > maxNum){
                        Toast.makeText(mContext, "至多" + maxNum + "人", Toast.LENGTH_SHORT).show();
                    }else{
                        if(null != dialogListener){
                            dialogListener.refreshActivity(num + "");
                        }
                        dismiss();
                    }
                }else{
                    Toast.makeText(mContext, "请输入出团人数", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
}
