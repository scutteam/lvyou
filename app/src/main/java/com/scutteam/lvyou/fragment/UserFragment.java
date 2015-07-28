package com.scutteam.lvyou.fragment;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.activity.ModifyBindPhoneActivity;
import com.scutteam.lvyou.activity.ModifyPasswordActivity;
import com.scutteam.lvyou.activity.ModifyUserNameActivity;
import com.scutteam.lvyou.activity.SetRealNameActivity;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.AvatarDialog;
import com.scutteam.lvyou.dialog.SexDialog;
import com.scutteam.lvyou.interfaces.AvatarOrSexDialogListener;
import com.scutteam.lvyou.widget.CircleImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by admin on 15/7/22.
 */
public class UserFragment extends Fragment implements View.OnClickListener, AvatarOrSexDialogListener {

    private View view;
    private String sessionid;
    private String profile_image_url;
    private String nickName;
    private String realName;
    private String cardNum;
    private String phone;
    private String gender;//M-男;F-女
    private String resume;
    private String school;
    private String address;
    private Long user_id;
    private int level;
    
    private Bitmap photo;
    private static final int REFRESH_UI = 2000000;
    private static final int REFRESH_AVATAR = 2000001;
    private static final int REFRESH_SEX = 2000003;
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case REFRESH_UI:
                    LvYouApplication.setScreenName(nickName);
                    LvYouApplication.setImageProfileUrl(profile_image_url);
                    LvYouApplication.setReal_name(realName);
                    LvYouApplication.setPhone(phone);
                    LvYouApplication.setSex(gender);
                    
                    refreshUI();
                    break;
                case REFRESH_SEX:
                    refreshUI();
                    break;
                case REFRESH_AVATAR:
                    //准备穿过去
                    mCivAvatar.setImageBitmap(photo);
                    break;
            }
        }
    };
    private AvatarDialog avatarDialog;
    private SexDialog sexDialog;
    private RelativeLayout mRlAvatarLayout;
    private RelativeLayout mRlUserNameLayout;
    private RelativeLayout mRlRealNameLayout;
    private RelativeLayout mRlSexLayout;
    private RelativeLayout mRlPhoneLayout;
    private RelativeLayout mRlChangePasswordLayout;
    private CircleImageView mCivAvatar;
    private TextView mTvName;
    private TextView mTvRealName;
    private TextView mTvSex;
    private TextView mTvPhone;
    public static final int SELECT_PIC_BY_PICK_PHOTO = 1;
    public static final int SELECT_PIC_BY_TAKE_PHOTO = 2;
    private static final int CUT_PHOTO = 3;
    private Uri photoUri;
    
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void refreshUI() {
        if(LvYouApplication.image_profile_url != null && LvYouApplication.image_profile_url.length() > 0 && !LvYouApplication.image_profile_url.equals("null")) {
            ImageLoader.getInstance().displayImage(LvYouApplication.image_profile_url, mCivAvatar);
        } else {
            mCivAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.default_icon_gray));
        }
        
        if(LvYouApplication.screen_name != null && LvYouApplication.screen_name.length() > 0 && !LvYouApplication.screen_name.equals("null")) {
            mTvName.setText(LvYouApplication.screen_name);
        } else {
            mTvName.setText("未填写");
        }

        if(LvYouApplication.real_name != null && LvYouApplication.real_name.length() > 0 && !LvYouApplication.real_name.equals("null")) {
            mTvRealName.setText(LvYouApplication.real_name);
            mTvRealName.setAlpha(1.0f);
        } else {
            mTvRealName.setText("未填写");
            mTvRealName.setAlpha(0.5f);
        }
        
        if(LvYouApplication.sex != null && LvYouApplication.sex.length() > 0 && !LvYouApplication.sex.equals("null")) {
            mTvSex.setText(LvYouApplication.sex.equals("M") ? "男" : "女");
            mTvSex.setAlpha(1.0f);
        } else {
            mTvSex.setText("未填写");
            mTvSex.setAlpha(0.5f);
        }

        if(LvYouApplication.phone != null && LvYouApplication.phone.length() > 0 && !LvYouApplication.phone.equals("null")) {
            mTvPhone.setText(LvYouApplication.phone);
            mTvPhone.setAlpha(1.0f);
        } else {
            mTvPhone.setText("未填写");
            mTvPhone.setAlpha(0.5f);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user,null);
        
        initUserData();
        initView();
        initListener();
        return view;
    }

    public void initUserData() {
        sessionid = LvYouApplication.getSessionId();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid",sessionid);
        client.post(getActivity(), Constants.URL + "user/mobilelogin.info.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    
                    int code = response.getInt("code");
                    if(code == 0) {
                        JSONObject dataObject = response.getJSONObject("data");
                        
                        if(dataObject.has("id")) {
                            user_id = dataObject.getLong("id");
                        }
                        if(dataObject.has("level")) {
                            level = dataObject.getInt("level");
                        }
                        if(dataObject.has("nickName")) {
                            nickName = dataObject.getString("nickName");
                        }
                        if(dataObject.has("realName")) {
                            realName = dataObject.getString("realName");
                        }
                        if(dataObject.has("cardNum")) {
                            cardNum = dataObject.getString("cardNum");
                        }
                        if(dataObject.has("phone")) {
                            phone = dataObject.getString("phone");
                        }
                        if(dataObject.has("gender")) {
                            gender = dataObject.getString("gender");
                        }
                        if(dataObject.has("faceIcon")) {
                            profile_image_url = Constants.IMAGE_URL + dataObject.getString("faceIcon");
                        }
                        if(dataObject.has("resume")) {
                            resume = dataObject.getString("resume");
                        }
                        if(dataObject.has("school")) {
                            school = dataObject.getString("school");
                        }
                        if(dataObject.has("address")) {
                            address = dataObject.getString("address");
                        }

                        mHandler.sendEmptyMessage(REFRESH_UI);
                        
                    } else if(code == 1) {
                        Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                            
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    
    public void initView() {
        mRlAvatarLayout = (RelativeLayout) view.findViewById(R.id.rl_avatar_layout);
        mRlUserNameLayout = (RelativeLayout) view.findViewById(R.id.rl_user_name_layout);
        mRlRealNameLayout = (RelativeLayout) view.findViewById(R.id.rl_real_name_layout);
        mRlSexLayout = (RelativeLayout) view.findViewById(R.id.rl_sex_layout);
        mRlPhoneLayout = (RelativeLayout) view.findViewById(R.id.rl_phone_layout);
        mRlChangePasswordLayout = (RelativeLayout) view.findViewById(R.id.rl_change_password_layout);
        mCivAvatar = (CircleImageView) view.findViewById(R.id.civ_avatar);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvRealName = (TextView) view.findViewById(R.id.tv_real_name);
        mTvSex = (TextView) view.findViewById(R.id.tv_sex);
        mTvPhone = (TextView) view.findViewById(R.id.tv_phone);
    }
    
    public void initListener() {
        mRlAvatarLayout.setOnClickListener(this);
        mRlUserNameLayout.setOnClickListener(this);
        mRlRealNameLayout.setOnClickListener(this);
        mRlSexLayout.setOnClickListener(this);
        mRlPhoneLayout.setOnClickListener(this);
        mRlChangePasswordLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_avatar_layout:
                avatarDialog = new AvatarDialog(getActivity(),R.style.TransparentDialog);
                avatarDialog.setListener(this);
                avatarDialog.show();
                break;
            case R.id.rl_user_name_layout:
                intent = new Intent();
                intent.setClass(getActivity(), ModifyUserNameActivity.class);
                startActivityForResult(intent, Constants.REQUEST_MODIFY_USER_NAME);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                break;
            case R.id.rl_real_name_layout:
                intent = new Intent();
                intent.setClass(getActivity(), SetRealNameActivity.class);
                startActivityForResult(intent, Constants.REQUEST_MODIFY_USER_REAL_NAME);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                break;
            case R.id.rl_sex_layout:
                sexDialog = new SexDialog(getActivity(),R.style.TransparentDialog);
                sexDialog.setListener(this);
                sexDialog.show();
                break;
            case R.id.rl_phone_layout:
                intent = new Intent();
                intent.setClass(getActivity(), ModifyBindPhoneActivity.class);
                startActivityForResult(intent, Constants.REQUEST_MODIFY_USER_PHONE);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                break;
            case R.id.rl_change_password_layout:
                intent = new Intent();
                intent.setClass(getActivity(), ModifyPasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClickCamera() {
        avatarDialog.dismiss();

        takePhoto();
    }

    public void takePhoto() {
        String SDState = Environment.getExternalStorageState();
        if (!SDState.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity().getApplicationContext(), "内存卡不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            photoUri = getActivity().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new ContentValues());
            if (photoUri != null) {
                Intent i = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(i, SELECT_PIC_BY_TAKE_PHOTO);

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "发生意外，无法写入相册",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "发生意外，无法写入相册",Toast.LENGTH_SHORT).show();
        }
    }
    
    

    @Override
    public void onClickPicture() {
        avatarDialog.dismiss();

        Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(choosePictureIntent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    public void onClickMale() {
        sexDialog.dismiss();
        
        setSex("M");
    }

    @Override
    public void onClickFemale() {
        sexDialog.dismiss();

        setSex("F");
    }
    
    public void setSex(final String sex) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid",LvYouApplication.getSessionId());
        params.put("gender",sex);
        client.post(getActivity(),Constants.URL + "user/personal.modify_gender.do",params,new JsonHttpResponseHandler(){

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        Toast.makeText(getActivity(),"修改性别成功",Toast.LENGTH_SHORT).show();

                        LvYouApplication.setSex(sex);
                        mHandler.sendEmptyMessage(REFRESH_SEX);
                    } else {
                        Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case SELECT_PIC_BY_TAKE_PHOTO:
                    beginCrop(photoUri);
                    break;
                case SELECT_PIC_BY_PICK_PHOTO:
                    beginCrop(data.getData());
                    break;
                case CUT_PHOTO:
                    handleCrop(data);
                    break;
            }
        }
    }

    public void beginCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，注意如果return-data=true情况下,其实得到的是缩略图，并不是真实拍摄的图片大小，
        // 而原因是拍照的图片太大，所以这个宽高当你设置很大的时候发现并不起作用，就是因为返回的原图是缩略图，但是作为头像还是够清晰了
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //返回图片数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CUT_PHOTO);
    }

    public void handleCrop(Intent result) {
        Bundle extras = result.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");

            String base64Bitmap = bitmapToBase64(photo);
            
            startChangeAvatar(base64Bitmap);
        }
    }
    
    public void startChangeAvatar(String base64Bitmap) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("img_base64",base64Bitmap);
        params.put("sessionid",LvYouApplication.getSessionId());
        client.post(getActivity(),Constants.URL + "user/personal.modify_face.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        Toast.makeText(getActivity(),"修改头像成功",Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(REFRESH_AVATAR);
                    } else {
                        Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
