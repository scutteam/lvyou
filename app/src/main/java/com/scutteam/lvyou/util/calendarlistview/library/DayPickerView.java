//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.scutteam.lvyou.util.calendarlistview.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.scutteam.lvyou.R;

public class DayPickerView extends RecyclerView {
    protected Context mContext;
    protected SimpleMonthAdapter mAdapter;
    private DatePickerController mController;
    protected int mCurrentScrollState;
    protected long mPreviousScrollPosition;
    protected int mPreviousScrollState;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;
    private int minDay;
    private int maxDay;

    public void setSdRefreshListener(SDRefreshListener sdRefreshListener) {
        this.sdRefreshListener = sdRefreshListener;
        this.getSimpleMonthAdapter().setSdRefreshListener(sdRefreshListener);
    }

    private SDRefreshListener sdRefreshListener = null;

    public DayPickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DayPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentScrollState = 0;
        this.mPreviousScrollState = 0;
        if(!this.isInEditMode()) {
            this.typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);
            this.setLayoutParams(new LayoutParams(-1, -1));
            this.init(context);
        }

    }

    public void setmController(DatePickerController mController) {
        this.mController = mController;
        this.setUpAdapter();
        this.setAdapter(this.mAdapter);
    }

    public void init(Context paramContext) {
        this.setLayoutManager(new LinearLayoutManager(paramContext));
        this.mContext = paramContext;
        this.setUpListView();
        this.onScrollListener = new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                SimpleMonthView child = (SimpleMonthView)recyclerView.getChildAt(0);
                if(child != null) {
                    DayPickerView.this.mPreviousScrollPosition = (long)dy;
                    DayPickerView.this.mPreviousScrollState = DayPickerView.this.mCurrentScrollState;
                }
            }
        };
    }

    protected void setUpAdapter() {
        if(this.mAdapter == null) {
            this.mAdapter = new SimpleMonthAdapter(this.getContext(), this.mController, this.typedArray);
        }

        this.mAdapter.notifyDataSetChanged();
    }

    protected void setUpListView() {
        this.setVerticalScrollBarEnabled(false);
        this.setOnScrollListener(this.onScrollListener);
        this.setFadingEdgeLength(0);
    }

    public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() {
        return this.mAdapter.getSelectedDays();
    }

    public SimpleMonthAdapter getSimpleMonthAdapter() {
        return mAdapter;
    }


    public void setMinDay(int day){
        minDay = day;
        if(null != mAdapter)
            mAdapter.setMinDay(day);
    }

    public void setMaxDay(int day){
        maxDay = day;
        if(null != mAdapter)
            mAdapter.setMaxDay(day);
    }

    public interface SDRefreshListener{
        public void refresh(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays);
    }
}
