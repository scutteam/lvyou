package com.scutteam.lvyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.util.DensityUtil;
import com.scutteam.lvyou.util.calendarlistview.library.DatePickerController;
import com.scutteam.lvyou.util.calendarlistview.library.DayPickerView;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthAdapter;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthView;

import java.util.Calendar;

/**
 * Created by liujie on 15/7/21.
 */
public class SelectDayDialog extends Dialog implements DayPickerView.SDRefreshListener {
    private DialogListener mListener = null;
    private Context mContext = null;
    private DayPickerView dayPicker = null;
    private TextView tvTitle = null;
    private int minDay;
    private int maxDay;
    private boolean isOneDay = false;


    public SelectDayDialog(Context context, DialogListener listener) {
        super(context);
        this.mListener = listener;
        this.mContext = context;
        this.setCanceledOnTouchOutside(true);
        this.setCanceledOnTouchOutside(true);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initListener();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.dialog_select_day, null);
        dayPicker = (DayPickerView) dialogView.findViewById(R.id.pickerView);
        tvTitle = (TextView) dialogView.findViewById(R.id.sdd_day_title);
        this.setContentView(dialogView);
    }

    private void initListener() {
        dayPicker.setmController(new DatePickerController() {
            @Override
            public int getMaxYear() {
                return Calendar.getInstance().get(Calendar.YEAR) + 1;
            }

            @Override
            public void onDayOfMonthSelected(int year, int month, int day) {
                Log.i("date selected", year + "/" + (month + 1) + "/" + day);
            }
        });
        dayPicker.setSdRefreshListener(this);
    }

    public void setMinDay(int day) {
        minDay = day;
        if (null != dayPicker)
            dayPicker.setMinDay(day);
    }

    public void setMaxDay(int day) {
        maxDay = day;
        if (null != dayPicker)
            dayPicker.setMaxDay(day);
    }

    /**
     * 当日历里面选择变化时调用
     *
     * @param selectedDays
     */
    @Override
    public void refresh(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
        if (null == selectedDays.getFirst()) {
            tvTitle.setText("选择出发日期");
        } else if (null == selectedDays.getLast()) {
            if(!isOneDay) {
                tvTitle.setText("选择返回日期");
            }else{
                selectedDays.setLast(selectedDays.getFirst());
                if (null != mListener) {
                    mListener.refreshActivity(selectedDays);
                }
                dismiss();
            }
        } else {
            if (null != mListener) {
                mListener.refreshActivity(selectedDays);
            }
            dismiss();
        }
    }

    public void setIsOneDay(boolean isOneDay) {
        this.isOneDay = isOneDay;
    }
}
