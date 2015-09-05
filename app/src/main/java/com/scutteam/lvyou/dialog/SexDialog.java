package com.scutteam.lvyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.interfaces.AvatarOrSexDialogListener;

/**
 * Created by admin on 15/7/25.
 */
public class SexDialog extends Dialog implements View.OnClickListener {

    private TextView mTvMale;
    private TextView mTvFemale;
    private AvatarOrSexDialogListener listener;
    private Context mContext;

    public SexDialog(Context context) {
        super(context);
    }

    public SexDialog(Context context, int theme) {
        super(context, theme);

        mContext = context;
    }

    public void setListener(AvatarOrSexDialogListener Llistener) {
        listener = Llistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_sex);

        initView();
        initListener();
    }

    public void initView() {
        mTvMale = (TextView) findViewById(R.id.tv_male);
        mTvFemale = (TextView) findViewById(R.id.tv_female);
    }

    public void initListener() {
        mTvMale.setOnClickListener(this);
        mTvFemale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_male:
                listener.onClickMale();
                break;
            case R.id.tv_female:
                listener.onClickFemale();
                break;
            default:
                break;
        }
    }
}
