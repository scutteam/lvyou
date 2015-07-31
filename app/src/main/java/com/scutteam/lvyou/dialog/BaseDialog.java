package com.scutteam.lvyou.dialog;

/**
 * 基本的对话框
 * 含有标题，内容和确定取消两个按钮
 * 可以选择显示其中的任意项内容，可以在内容中嵌套ListView
 * @author liujie
 * 2015 1 23
 */
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.util.DensityUtil;
import com.scutteam.lvyou.util.ListViewUtil;


public class BaseDialog extends Dialog {

	protected Context mContext;

	private View frameTitle;
	private TextView tvTitle;
	private Button btnConfirm, btnCancel;
	private LinearLayout contain;
	private View content;
	private OnConfirmListener onConfirmListener;
	private OnCancelListener onCancelListener;
	
	
	protected BaseDialog(Context context, int theme){
		super(context, theme);
	}

	public BaseDialog(Context context){
		super(context, R.style.base_dialog);
		mContext = context;
		setContentView(R.layout.dialog_base);
		this.setCanceledOnTouchOutside(false);
		
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.width = (int) (DensityUtil.getScreenWidthPx(mContext) * 0.9);
		
		dialogWindow.setAttributes(params);

		frameTitle = findViewById(R.id.base_dialog_title_frame);
		tvTitle = (TextView) findViewById(R.id.base_dialog_title);
		tvTitle.setText("提示");

		btnConfirm = (Button) findViewById(R.id.base_dialog_confirm);
		btnConfirm.setVisibility(View.GONE);
		btnConfirm.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (onConfirmListener != null) {
					onConfirmListener.onConfirm();
				}
			}

		});

		btnCancel = (Button) findViewById(R.id.base_dialog_cancel);
		btnCancel.setVisibility(View.GONE);
		btnCancel.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (onCancelListener != null) {
					onCancelListener.onCancel();
				}
			}

		});

		contain = (LinearLayout) findViewById(R.id.base_dialog_content);
	}

	/**
	 * 设置标题
	 * @param text
	 */
	public void setTitle(String text) {
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(text);
	}

	public interface OnConfirmListener {
		public void onConfirm();
	}

	/**
	 * 显示确定按钮并设置监听
	 * @param l
	 */
	public void setOnConfirmListener(OnConfirmListener l) {
		onConfirmListener = l;
		btnConfirm.setVisibility(View.VISIBLE);
	}

	public interface OnCancelListener {
		public void onCancel();
	}

	/**
	 * 显示取消按钮并设置监听
	 */
	public void setOnCancelListener(OnCancelListener l) {
		onCancelListener = l;
		btnCancel.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置取消按钮的文字
	 */
	public void setCancelBtnText(String text){
		btnCancel.setText(text);
	}
	
	/**
	 * 设置确认按钮的文字
	 */
	public void setConfirmBtnText(String text){
		btnConfirm.setText(text);
	}

	/**
	 * 用layout文件的id去设置对话框内容
	 * @param layoutId
	 */
	public void setContent(int layoutId) {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		content = mInflater.inflate(layoutId, null);
		contain.removeAllViews();
		contain.addView(content, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	/**
	 * 用view去设置dialog的内容
	 * @param view
	 */
	public void setContent(View view) {
		content = view;
		contain.removeAllViews();
		contain.addView(content, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	/**
	 * 获取content内容部分的view
	 * @return
	 */
	public View getContent() {
		return content;
	}

	/**
	 * 显示文字信息
	 * @param mesg
	 */
	public void setContentMessage(String mesg) {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		content = mInflater.inflate(R.layout.base_dialog_mesg, null);
		contain.removeAllViews();
		contain.addView(content, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		TextView tvMesg = (TextView) (content
				.findViewById(R.id.base_dialog_mesg_text));
		tvMesg.setText(mesg);
	}

	/**
	 * 隐藏对话框标题
	 */
	public void hideTitle() {
		frameTitle.setVisibility(View.GONE);
	}

	/**
	 * 显示对话框
	 */
	@Override
	public void show() {
		super.show();
		findListViewAndSetHeight(contain);  // contain是一个scrollView，若嵌套了ListView，需设置ListView的实际高度以完全显示ListView
	}

	/**
	 * 设置ListView的实际高度以完全显示ListView
	 * @param v
	 */
	private void findListViewAndSetHeight(ViewGroup v) {
		for (int i = 0; i < v.getChildCount(); ++i) {
			View childView = v.getChildAt(i);
			if (childView instanceof ListView) {
				ListViewUtil
						.setListViewHeightBasedOnChildren((ListView) childView);
			} else if (childView instanceof ViewGroup) {
				findListViewAndSetHeight((ViewGroup) childView);
			}
		}
	}
}

