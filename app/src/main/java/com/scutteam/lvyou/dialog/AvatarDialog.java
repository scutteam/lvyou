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
public class AvatarDialog extends Dialog implements View.OnClickListener {
    
    private TextView mTvCamera;
    private TextView mTvPicture;
    private AvatarOrSexDialogListener listener;
    private Context mContext;
    
    public AvatarDialog(Context context) {
        super(context);
    }

    public AvatarDialog(Context context, int theme) {
        super(context, theme);

        mContext = context;
    }
    
    public void setListener(AvatarOrSexDialogListener Llistener) {
        listener = Llistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_avatar);
        
        initView();
        initListener();
    }

    public void initView() {
        mTvCamera = (TextView) findViewById(R.id.tv_camera);
        mTvPicture = (TextView) findViewById(R.id.tv_picture);
    }
    
    public void initListener() {
        mTvCamera.setOnClickListener(this);
        mTvPicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camera:
                listener.onClickCamera();
                break;
            case R.id.tv_picture:
                listener.onClickPicture();
                break;
            default:
                break;
        }
    }
}
