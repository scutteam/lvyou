package com.scutteam.lvyou.util.calendarlistview.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import com.scutteam.lvyou.R.styleable;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthView.OnDayClickListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SimpleMonthAdapter extends Adapter<SimpleMonthAdapter.ViewHolder> implements OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
    private final Context mContext;
    private final DatePickerController mController;
    private final Calendar calendar;
    private final SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays;
    private final Integer firstMonth;
    private final Integer lastMonth;
    private SimpleMonthView.OnDayClickListener onDayClickListener = null;

    public SimpleMonthAdapter(Context context, DatePickerController datePickerController, TypedArray typedArray) {
        this.typedArray = typedArray;
        this.calendar = Calendar.getInstance();
        this.firstMonth = calendar.get(Calendar.MONTH);
        this.lastMonth = calendar.get(Calendar.MONTH) + 1;
        this.selectedDays = new SimpleMonthAdapter.SelectedDays();
        this.mContext = context;
        this.mController = datePickerController;
        this.init();
    }

    public SimpleMonthAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        SimpleMonthView simpleMonthView = new SimpleMonthView(this.mContext, this.typedArray);
        return new SimpleMonthAdapter.ViewHolder(simpleMonthView, this);
    }

    public void onBindViewHolder(SimpleMonthAdapter.ViewHolder viewHolder, int position) {
        SimpleMonthView v = viewHolder.simpleMonthView;
        HashMap drawingParams = new HashMap();
        int month = (this.firstMonth.intValue() + position % 12) % 12;
        int year = position / 12 + this.calendar.get(1) + (this.firstMonth.intValue() + position % 12) / 12;
        int selectedFirstDay = -1;
        int selectedLastDay = -1;
        int selectedFirstMonth = -1;
        int selectedLastMonth = -1;
        int selectedFirstYear = -1;
        int selectedLastYear = -1;
        if(this.selectedDays.getFirst() != null) {
            selectedFirstDay = ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).day;
            selectedFirstMonth = ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).month;
            selectedFirstYear = ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).year;
        }

        if(this.selectedDays.getLast() != null) {
            selectedLastDay = ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getLast()).day;
            selectedLastMonth = ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getLast()).month;
            selectedLastYear = ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getLast()).year;
        }

        v.reuse();
        drawingParams.put("selected_begin_year", Integer.valueOf(selectedFirstYear));
        drawingParams.put("selected_last_year", Integer.valueOf(selectedLastYear));
        drawingParams.put("selected_begin_month", Integer.valueOf(selectedFirstMonth));
        drawingParams.put("selected_last_month", Integer.valueOf(selectedLastMonth));
        drawingParams.put("selected_begin_day", Integer.valueOf(selectedFirstDay));
        drawingParams.put("selected_last_day", Integer.valueOf(selectedLastDay));
        drawingParams.put("year", Integer.valueOf(year));
        drawingParams.put("month", Integer.valueOf(month));
        drawingParams.put("week_start", Integer.valueOf(this.calendar.getFirstDayOfWeek()));
        v.setMonthParams(drawingParams);
        v.invalidate();
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public int getItemCount() {
        int itemCount = (this.mController.getMaxYear() - this.calendar.get(1) + 1) * 12;
        if(this.firstMonth.intValue() != -1) {
            itemCount -= this.firstMonth.intValue();
        }

        if(this.lastMonth.intValue() != -1) {
            itemCount -= 12 - this.lastMonth.intValue() - 1;
        }

        return itemCount;
    }

    protected void init() {
        if(this.typedArray.getBoolean(styleable.DayPickerView_currentDaySelected, false)) {
            this.onDayTapped(new SimpleMonthAdapter.CalendarDay(System.currentTimeMillis()));
        }

    }

    public void onDayClick(SimpleMonthView simpleMonthView, SimpleMonthAdapter.CalendarDay calendarDay) {
        if(calendarDay != null) {
            this.onDayTapped(calendarDay);
        }

    }

    protected void onDayTapped(SimpleMonthAdapter.CalendarDay calendarDay) {
        this.mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
        this.setSelectedDay(calendarDay);
    }

    public void setSelectedDay(SimpleMonthAdapter.CalendarDay calendarDay) {
        if(this.selectedDays.getFirst() != null && this.selectedDays.getLast() == null) {
            this.selectedDays.setLast(calendarDay);
            if(((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).month < calendarDay.month) {
                for(int i = 0; i < ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).month - calendarDay.month - 1; ++i) {
                    this.mController.onDayOfMonthSelected(((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).year, ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).month + i, ((SimpleMonthAdapter.CalendarDay)this.selectedDays.getFirst()).day);
                }
            }
        } else if(this.selectedDays.getLast() != null) {
            this.selectedDays.setFirst(calendarDay);
            this.selectedDays.setLast(null);
        } else {
            this.selectedDays.setFirst(calendarDay);
        }

        this.notifyDataSetChanged();
    }

    public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() {
        return this.selectedDays;
    }

    public static class SelectedDays<K> implements Serializable {
        private static final long serialVersionUID = 3942549765282708376L;
        private K first;
        private K last;

        public SelectedDays() {
        }

        public K getFirst() {
            return this.first;
        }

        public void setFirst(K first) {
            this.first = first;
        }

        public K getLast() {
            return this.last;
        }

        public void setLast(K last) {
            this.last = last;
        }
    }

    public static class CalendarDay implements Serializable {
        private static final long serialVersionUID = -5456695978688356202L;
        private Calendar calendar;
        int day;
        int month;
        int year;

        public CalendarDay() {
            this.setTime(System.currentTimeMillis());
        }

        public CalendarDay(int year, int month, int day) {
            this.setDay(year, month, day);
        }

        public CalendarDay(long timeInMillis) {
            this.setTime(timeInMillis);
        }

        public CalendarDay(Calendar calendar) {
            this.year = calendar.get(1);
            this.month = calendar.get(2);
            this.day = calendar.get(5);
        }

        private void setTime(long timeInMillis) {
            if(this.calendar == null) {
                this.calendar = Calendar.getInstance();
            }

            this.calendar.setTimeInMillis(timeInMillis);
            this.month = this.calendar.get(2);
            this.year = this.calendar.get(1);
            this.day = this.calendar.get(5);
        }

        public void set(SimpleMonthAdapter.CalendarDay calendarDay) {
            this.year = calendarDay.year;
            this.month = calendarDay.month;
            this.day = calendarDay.day;
        }

        public void setDay(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public Date getDate() {
            if(this.calendar == null) {
                this.calendar = Calendar.getInstance();
            }

            this.calendar.set(this.year, this.month, this.day);
            return this.calendar.getTime();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ year: ");
            stringBuilder.append(this.year);
            stringBuilder.append(", month: ");
            stringBuilder.append(this.month);
            stringBuilder.append(", day: ");
            stringBuilder.append(this.day);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        final SimpleMonthView simpleMonthView;

        public ViewHolder(View itemView, OnDayClickListener onDayClickListener) {
            super(itemView);
            this.simpleMonthView = (SimpleMonthView)itemView;
            this.simpleMonthView.setLayoutParams(new LayoutParams(-1, -1));
            this.simpleMonthView.setClickable(true);
            this.simpleMonthView.setOnDayClickListener(onDayClickListener);
        }
    }

}
