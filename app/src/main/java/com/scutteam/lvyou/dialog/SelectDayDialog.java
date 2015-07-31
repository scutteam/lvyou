package com.scutteam.lvyou.dialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.util.calendarlistview.library.DatePickerController;
import com.scutteam.lvyou.util.calendarlistview.library.DayPickerView;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthAdapter;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthView;

/**
 * Created by liujie on 15/7/21.
 */
public class SelectDayDialog extends BaseDialog {
    private DialogListener mListener = null;
    private Context mContext = null;

    private DayPickerView dayPicker = null;

    public SelectDayDialog(Context context, DialogListener listener){
        super(context);
        this.mListener = listener;
        this.mContext = context;
        hideTitle();
        init(mContext);
    }

    private void init(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_select_day, null);
        setContent(dialogView);

        dayPicker = (DayPickerView)dialogView.findViewById(R.id.pickerView);
        dayPicker.setmController(new DatePickerController() {
            @Override
            public int getMaxYear() {
                return 2015;
            }

            @Override
            public void onDayOfMonthSelected(int year, int month, int day) {
                Log.i("date selected", year + "/" + (month + 1) + "/" + day);
            }
        });
    }
}
