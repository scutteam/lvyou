package com.scutteam.lvyou.util.calendarlistview.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;

import com.scutteam.lvyou.R.*;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthAdapter.CalendarDay;

import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SimpleMonthView extends View {
    public static final String VIEW_PARAMS_HEIGHT = "height";
    public static final String VIEW_PARAMS_MONTH = "month";
    public static final String VIEW_PARAMS_YEAR = "year";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_DAY = "selected_begin_day";
    public static final String VIEW_PARAMS_SELECTED_LAST_DAY = "selected_last_day";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_MONTH = "selected_begin_month";
    public static final String VIEW_PARAMS_SELECTED_LAST_MONTH = "selected_last_month";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_YEAR = "selected_begin_year";
    public static final String VIEW_PARAMS_SELECTED_LAST_YEAR = "selected_last_year";
    public static final String VIEW_PARAMS_WEEK_START = "week_start";
    private static final int SELECTED_CIRCLE_ALPHA = 128;
    protected static int DEFAULT_HEIGHT = 32;
    protected static final int DEFAULT_NUM_ROWS = 6;
    protected static int DAY_SELECTED_CIRCLE_SIZE;
    protected static int DAY_SEPARATOR_WIDTH = 1;
    protected static int MINI_DAY_NUMBER_TEXT_SIZE;
    protected static int MIN_HEIGHT = 10;
    protected static int MONTH_DAY_LABEL_TEXT_SIZE;
    protected static int MONTH_HEADER_SIZE;
    protected static int MONTH_LABEL_TEXT_SIZE;
    protected int mPadding = 0;
    private String mDayOfWeekTypeface;
    private String mMonthTitleTypeface;
    protected Paint mMonthDayLabelPaint;
    protected Paint mMonthNumPaint;
    protected Paint mMonthTitleBGPaint;
    protected Paint mMonthTitlePaint;
    protected Paint mSelectedCirclePaint;
    protected int mCurrentDayTextColor;
    protected int mMonthTextColor;
    protected int mDayTextColor;
    protected int mDayNumColor;
    protected int mMonthTitleBGColor;
    protected int mPreviousDayColor;
    protected int mSelectedDaysColor;
    private final StringBuilder mStringBuilder;
    protected boolean mHasToday = false;
    protected boolean mIsPrev = false;
    protected int mSelectedBeginDay = -1;
    protected int mSelectedLastDay = -1;
    protected int mSelectedBeginMonth = -1;
    protected int mSelectedLastMonth = -1;
    protected int mSelectedBeginYear = -1;
    protected int mSelectedLastYear = -1;
    protected int mToday = -1;
    protected int mWeekStart = 1;
    protected int mNumDays = 7;
    protected int mNumCells;
    private int mDayOfWeekStart;
    protected int mMonth;
    protected Boolean mDrawRect;
    protected int mRowHeight;
    protected int mWidth;
    protected int mYear;
    final Time today;
    private final Calendar mCalendar;
    private final Calendar mDayLabelCalendar;
    private final Boolean isPrevDayEnabled;
    private int mNumRows;
    private DateFormatSymbols mDateFormatSymbols;
    private SimpleMonthView.OnDayClickListener mOnDayClickListener;

    public SimpleMonthView(Context context, TypedArray typedArray) {
        super(context);
        this.mNumCells = this.mNumDays;
        this.mDayOfWeekStart = 0;
        this.mRowHeight = DEFAULT_HEIGHT;
        this.mNumRows = 6;
        this.mDateFormatSymbols = new DateFormatSymbols();
        Resources resources = context.getResources();
        this.mDayLabelCalendar = Calendar.getInstance();
        this.mCalendar = Calendar.getInstance();
        this.today = new Time(Time.getCurrentTimezone());
        this.today.setToNow();
        this.mDayOfWeekTypeface = resources.getString(string.sans_serif);
        this.mMonthTitleTypeface = resources.getString(string.sans_serif);
        this.mCurrentDayTextColor = typedArray.getColor(styleable.DayPickerView_colorCurrentDay, resources.getColor(color.normal_day));
        this.mMonthTextColor = typedArray.getColor(styleable.DayPickerView_colorMonthName, resources.getColor(color.normal_day));
        this.mDayTextColor = typedArray.getColor(styleable.DayPickerView_colorDayName, resources.getColor(color.normal_day));
        this.mDayNumColor = typedArray.getColor(styleable.DayPickerView_colorNormalDay, resources.getColor(color.normal_day));
        this.mPreviousDayColor = typedArray.getColor(styleable.DayPickerView_colorPreviousDay, resources.getColor(color.normal_day));
        this.mSelectedDaysColor = typedArray.getColor(styleable.DayPickerView_colorSelectedDayBackground, resources.getColor(color.selected_day_background));
        this.mMonthTitleBGColor = typedArray.getColor(styleable.DayPickerView_colorSelectedDayText, resources.getColor(color.selected_day_text));
        //this.mDrawRect = Boolean.valueOf(typedArray.getBoolean(styleable.DayPickerView_drawRoundRect, false));
        this.mDrawRect = true;
        this.mStringBuilder = new StringBuilder(50);
        MINI_DAY_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(styleable.DayPickerView_textSizeDay, resources.getDimensionPixelSize(dimen.text_size_day));
        MONTH_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(styleable.DayPickerView_textSizeMonth, resources.getDimensionPixelSize(dimen.text_size_month));
        MONTH_DAY_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(styleable.DayPickerView_textSizeDayName, resources.getDimensionPixelSize(dimen.text_size_day_name));
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(styleable.DayPickerView_headerMonthHeight, resources.getDimensionPixelOffset(dimen.header_month_height));
        DAY_SELECTED_CIRCLE_SIZE = typedArray.getDimensionPixelSize(styleable.DayPickerView_selectedDayRadius, resources.getDimensionPixelOffset(dimen.selected_day_radius));
        this.mRowHeight = (typedArray.getDimensionPixelSize(styleable.DayPickerView_calendarHeight, resources.getDimensionPixelOffset(dimen.calendar_height)) - MONTH_HEADER_SIZE) / 6;
        this.isPrevDayEnabled = Boolean.valueOf(typedArray.getBoolean(styleable.DayPickerView_enablePreviousDay, true));
        this.initView();
    }

    /**
     * offset代表一个月第一个星期之前有几天（比如一个月第一天是星期3，那么offset是2）
     * mNumCells代表一个月有多少天
     * mNumDays代表一个星期几天
     *
     * @return
     */
    private int calculateNumRows() {
        int offset = this.findDayOffset();
        int dividend = (offset + this.mNumCells) / this.mNumDays;
        int remainder = (offset + this.mNumCells) % this.mNumDays;
        return dividend + (remainder > 0 ? 1 : 0);
    }

    private void drawMonthDayLabels(Canvas canvas) {
        int y = MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE / 2;
        int dayWidthHalf = (this.mWidth - this.mPadding * 2) / (this.mNumDays * 2);

        for (int i = 0; i < this.mNumDays; ++i) {
            int calendarDay = (i + this.mWeekStart) % this.mNumDays;
            int x = (2 * i + 1) * dayWidthHalf + this.mPadding;
            this.mDayLabelCalendar.set(7, calendarDay);
            canvas.drawText(this.mDateFormatSymbols.getShortWeekdays()[this.mDayLabelCalendar.get(7)].toUpperCase(Locale.getDefault()), (float) x, (float) y, this.mMonthDayLabelPaint);
        }

    }

    private void drawMonthTitle(Canvas canvas) {
        int x = (this.mWidth + 2 * this.mPadding) / 2;
        int y = (MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) / 2 + MONTH_LABEL_TEXT_SIZE / 3;
        StringBuilder stringBuilder = new StringBuilder(this.getMonthAndYearString().toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        canvas.drawText(stringBuilder.toString(), (float) x, (float) y, this.mMonthTitlePaint);
    }

    private int findDayOffset() {
        return (this.mDayOfWeekStart < this.mWeekStart
                ? this.mDayOfWeekStart + this.mNumDays
                : this.mDayOfWeekStart) - this.mWeekStart;
    }

    private String getMonthAndYearString() {
        byte flags = 52;
        this.mStringBuilder.setLength(0);
        long millis = this.mCalendar.getTimeInMillis();
        return DateUtils.formatDateRange(this.getContext(), millis, millis, flags);
    }

    private void onDayClick(CalendarDay calendarDay) {
        if (this.mOnDayClickListener != null
                && (this.isPrevDayEnabled.booleanValue()
                  || calendarDay.month != this.today.month
                  || calendarDay.year != this.today.year
                  || calendarDay.day >= this.today.monthDay)) {
            this.mOnDayClickListener.onDayClick(this, calendarDay);
        }

    }

    private boolean sameDay(int monthDay, Time time) {
        return this.mYear == time.year && this.mMonth == time.month && monthDay == time.monthDay;
    }

    private boolean prevDay(int monthDay, Time time) {
        return this.mYear < time.year
                || this.mYear == time.year
                && this.mMonth < time.month
                || this.mMonth == time.month
                && monthDay < time.monthDay;
    }

    protected void drawMonthNums(Canvas canvas) {
        int y = (this.mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE;
        int paddingDay = (this.mWidth - 2 * this.mPadding) / (2 * this.mNumDays);
        int dayOffset = this.findDayOffset();

        for (int day = 1; day <= this.mNumCells; ++day) {
            int x = paddingDay * (1 + dayOffset * 2) + this.mPadding;
            if (this.mMonth == this.mSelectedBeginMonth
                    && this.mSelectedBeginDay == day
                    && this.mSelectedBeginYear == this.mYear
                    || this.mMonth == this.mSelectedLastMonth
                    && this.mSelectedLastDay == day
                    && this.mSelectedLastYear == this.mYear) {
                if (this.mDrawRect.booleanValue()) {
                    RectF rectF = new RectF((float) (x - DAY_SELECTED_CIRCLE_SIZE),
                            (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE),
                            (float) (x + DAY_SELECTED_CIRCLE_SIZE),
                            (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE));
                    canvas.drawRoundRect(rectF, 10.0F, 10.0F, this.mSelectedCirclePaint);
                } else {
                    canvas.drawCircle((float) x, (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3), (float) DAY_SELECTED_CIRCLE_SIZE, this.mSelectedCirclePaint);
                }
            }

            if (this.mHasToday && this.mToday == day) {
                this.mMonthNumPaint.setColor(this.mCurrentDayTextColor);
                this.mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(1));
            } else {
                this.mMonthNumPaint.setColor(this.mDayNumColor);
                this.mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(0));
            }

            if (this.mMonth == this.mSelectedBeginMonth
                    && this.mSelectedBeginDay == day
                    && this.mSelectedBeginYear == this.mYear
                    || this.mMonth == this.mSelectedLastMonth
                    && this.mSelectedLastDay == day
                    && this.mSelectedLastYear == this.mYear) {
                this.mMonthNumPaint.setColor(this.mMonthTitleBGColor);
            }

            if (this.mSelectedBeginDay != -1
                    && this.mSelectedLastDay != -1
                    && this.mSelectedBeginYear == this.mSelectedLastYear
                    && this.mSelectedBeginMonth == this.mSelectedLastMonth
                    && this.mSelectedBeginDay == this.mSelectedLastDay
                    && day == this.mSelectedBeginDay
                    && this.mMonth == this.mSelectedBeginMonth
                    && this.mYear == this.mSelectedBeginYear) {
                this.mMonthNumPaint.setColor(this.mMonthTitleBGColor);
                RectF rectF = new RectF((float) (x - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (x + DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE));
                canvas.drawRoundRect(rectF, 10.0F, 10.0F, this.mSelectedCirclePaint);
            }

            if (this.mSelectedBeginDay != -1 && this.mSelectedLastDay != -1 && this.mSelectedBeginYear == this.mSelectedLastYear && this.mSelectedBeginYear == this.mYear && (this.mMonth == this.mSelectedBeginMonth && this.mSelectedLastMonth == this.mSelectedBeginMonth && (this.mSelectedBeginDay < this.mSelectedLastDay && day > this.mSelectedBeginDay && day < this.mSelectedLastDay || this.mSelectedBeginDay > this.mSelectedLastDay && day < this.mSelectedBeginDay && day > this.mSelectedLastDay) || this.mSelectedBeginMonth < this.mSelectedLastMonth && this.mMonth == this.mSelectedBeginMonth && day > this.mSelectedBeginDay || this.mSelectedBeginMonth < this.mSelectedLastMonth && this.mMonth == this.mSelectedLastMonth && day < this.mSelectedLastDay || this.mSelectedBeginMonth > this.mSelectedLastMonth && this.mMonth == this.mSelectedBeginMonth && day < this.mSelectedBeginDay || this.mSelectedBeginMonth > this.mSelectedLastMonth && this.mMonth == this.mSelectedLastMonth && day > this.mSelectedLastDay)) {
                this.mMonthNumPaint.setColor(this.mMonthTitleBGColor);
                RectF rectF = new RectF((float) (x - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (x + DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE));
                canvas.drawRoundRect(rectF, 10.0F, 10.0F, this.mSelectedCirclePaint);
            }

            if (this.mSelectedBeginDay != -1 && this.mSelectedLastDay != -1 && this.mSelectedBeginYear != this.mSelectedLastYear && (this.mSelectedBeginYear == this.mYear && this.mMonth == this.mSelectedBeginMonth || this.mSelectedLastYear == this.mYear && this.mMonth == this.mSelectedLastMonth) && (this.mSelectedBeginMonth < this.mSelectedLastMonth && this.mMonth == this.mSelectedBeginMonth && day < this.mSelectedBeginDay || this.mSelectedBeginMonth < this.mSelectedLastMonth && this.mMonth == this.mSelectedLastMonth && day > this.mSelectedLastDay || this.mSelectedBeginMonth > this.mSelectedLastMonth && this.mMonth == this.mSelectedBeginMonth && day > this.mSelectedBeginDay || this.mSelectedBeginMonth > this.mSelectedLastMonth && this.mMonth == this.mSelectedLastMonth && day < this.mSelectedLastDay)) {
                this.mMonthNumPaint.setColor(this.mMonthTitleBGColor);
                RectF rectF = new RectF((float) (x - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (x + DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE));
                canvas.drawRoundRect(rectF, 10.0F, 10.0F, this.mSelectedCirclePaint);
            }

            if (this.mSelectedBeginDay != -1 && this.mSelectedLastDay != -1 && this.mSelectedBeginYear == this.mSelectedLastYear && this.mYear == this.mSelectedBeginYear && (this.mMonth > this.mSelectedBeginMonth && this.mMonth < this.mSelectedLastMonth && this.mSelectedBeginMonth < this.mSelectedLastMonth || this.mMonth < this.mSelectedBeginMonth && this.mMonth > this.mSelectedLastMonth && this.mSelectedBeginMonth > this.mSelectedLastMonth)) {
                this.mMonthNumPaint.setColor(this.mMonthTitleBGColor);
                RectF rectF = new RectF((float) (x - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (x + DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE));
                canvas.drawRoundRect(rectF, 10.0F, 10.0F, this.mSelectedCirclePaint);
            }

            if (this.mSelectedBeginDay != -1 && this.mSelectedLastDay != -1 && this.mSelectedBeginYear != this.mSelectedLastYear && (this.mSelectedBeginYear < this.mSelectedLastYear && (this.mMonth > this.mSelectedBeginMonth && this.mYear == this.mSelectedBeginYear || this.mMonth < this.mSelectedLastMonth && this.mYear == this.mSelectedLastYear) || this.mSelectedBeginYear > this.mSelectedLastYear && (this.mMonth < this.mSelectedBeginMonth && this.mYear == this.mSelectedBeginYear || this.mMonth > this.mSelectedLastMonth && this.mYear == this.mSelectedLastYear))) {
                this.mMonthNumPaint.setColor(this.mMonthTitleBGColor);
                RectF rectF = new RectF((float) (x - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - DAY_SELECTED_CIRCLE_SIZE),
                        (float) (x + DAY_SELECTED_CIRCLE_SIZE),
                        (float) (y - MINI_DAY_NUMBER_TEXT_SIZE / 3 + DAY_SELECTED_CIRCLE_SIZE));
                canvas.drawRoundRect(rectF, 10.0F, 10.0F, this.mSelectedCirclePaint);
            }

            if (!this.isPrevDayEnabled.booleanValue() && this.prevDay(day, this.today) && this.today.month == this.mMonth && this.today.year == this.mYear) {
                this.mMonthNumPaint.setColor(this.mPreviousDayColor);
//                this.mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(2));
            }

            canvas.drawText(String.format("%d", new Object[]{Integer.valueOf(day)}), (float) x, (float) y, this.mMonthNumPaint);
            ++dayOffset;
            if (dayOffset == this.mNumDays) {
                dayOffset = 0;
                y += this.mRowHeight;
            }
        }

    }

    public CalendarDay getDayFromLocation(float x, float y) {
        int padding = this.mPadding;
        if (x >= (float) padding && x <= (float) (this.mWidth - this.mPadding)) {
            int yDay = (int) (y - (float) MONTH_HEADER_SIZE) / this.mRowHeight;
            int day = 1 + ((int) ((x - (float) padding) * (float) this.mNumDays / (float) (this.mWidth - padding - this.mPadding)) - this.findDayOffset()) + yDay * this.mNumDays;
            return this.mMonth <= 11 && this.mMonth >= 0 && CalendarUtils.getDaysInMonth(this.mMonth, this.mYear) >= day && day >= 1 ? new CalendarDay(this.mYear, this.mMonth, day) : null;
        } else {
            return null;
        }
    }

    protected void initView() {
        this.mMonthTitlePaint = new Paint();
        this.mMonthTitlePaint.setFakeBoldText(true);
        this.mMonthTitlePaint.setAntiAlias(true);
        this.mMonthTitlePaint.setTextSize((float) MONTH_LABEL_TEXT_SIZE);
        this.mMonthTitlePaint.setTypeface(Typeface.create(this.mMonthTitleTypeface, 1));
        this.mMonthTitlePaint.setColor(this.mMonthTextColor);
        this.mMonthTitlePaint.setTextAlign(Align.CENTER);
        this.mMonthTitlePaint.setStyle(Style.FILL);
        this.mMonthTitleBGPaint = new Paint();
        this.mMonthTitleBGPaint.setFakeBoldText(true);
        this.mMonthTitleBGPaint.setAntiAlias(true);
        this.mMonthTitleBGPaint.setColor(this.mMonthTitleBGColor);
        this.mMonthTitleBGPaint.setTextAlign(Align.CENTER);
        this.mMonthTitleBGPaint.setStyle(Style.FILL);
        this.mSelectedCirclePaint = new Paint();
        this.mSelectedCirclePaint.setFakeBoldText(true);
        this.mSelectedCirclePaint.setAntiAlias(true);
        this.mSelectedCirclePaint.setColor(this.mSelectedDaysColor);
        this.mSelectedCirclePaint.setTextAlign(Align.CENTER);
        this.mSelectedCirclePaint.setStyle(Style.FILL);
        this.mSelectedCirclePaint.setAlpha(128);
        this.mMonthDayLabelPaint = new Paint();
        this.mMonthDayLabelPaint.setAntiAlias(true);
        this.mMonthDayLabelPaint.setTextSize((float) MONTH_DAY_LABEL_TEXT_SIZE);
        this.mMonthDayLabelPaint.setColor(this.mDayTextColor);
        this.mMonthDayLabelPaint.setTypeface(Typeface.create(this.mDayOfWeekTypeface, 0));
        this.mMonthDayLabelPaint.setStyle(Style.FILL);
        this.mMonthDayLabelPaint.setTextAlign(Align.CENTER);
        this.mMonthDayLabelPaint.setFakeBoldText(true);
        this.mMonthNumPaint = new Paint();
        this.mMonthNumPaint.setAntiAlias(true);
        this.mMonthNumPaint.setTextSize((float) MINI_DAY_NUMBER_TEXT_SIZE);
        this.mMonthNumPaint.setStyle(Style.FILL);
        this.mMonthNumPaint.setTextAlign(Align.CENTER);
        this.mMonthNumPaint.setFakeBoldText(false);
    }

    protected void onDraw(Canvas canvas) {
        this.drawMonthTitle(canvas);
        this.drawMonthDayLabels(canvas);
        this.drawMonthNums(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), this.mRowHeight * this.mNumRows + MONTH_HEADER_SIZE);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mWidth = w;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            CalendarDay calendarDay = this.getDayFromLocation(event.getX(), event.getY());
            if (calendarDay != null) {
                this.onDayClick(calendarDay);
            }
        }

        return true;
    }

    public void reuse() {
        this.mNumRows = 6;
        this.requestLayout();
    }

    public void setMonthParams(HashMap<String, Integer> params) {
        if (!params.containsKey("month") && !params.containsKey("year")) {
            throw new InvalidParameterException("You must specify month and year for this view");
        } else {
            this.setTag(params);
            if (params.containsKey("height")) {
                this.mRowHeight = ((Integer) params.get("height")).intValue();
                if (this.mRowHeight < MIN_HEIGHT) {
                    this.mRowHeight = MIN_HEIGHT;
                }
            }

            if (params.containsKey("selected_begin_day")) {
                this.mSelectedBeginDay = ((Integer) params.get("selected_begin_day")).intValue();
            }

            if (params.containsKey("selected_last_day")) {
                this.mSelectedLastDay = ((Integer) params.get("selected_last_day")).intValue();
            }

            if (params.containsKey("selected_begin_month")) {
                this.mSelectedBeginMonth = ((Integer) params.get("selected_begin_month")).intValue();
            }

            if (params.containsKey("selected_last_month")) {
                this.mSelectedLastMonth = ((Integer) params.get("selected_last_month")).intValue();
            }

            if (params.containsKey("selected_begin_year")) {
                this.mSelectedBeginYear = ((Integer) params.get("selected_begin_year")).intValue();
            }

            if (params.containsKey("selected_last_year")) {
                this.mSelectedLastYear = ((Integer) params.get("selected_last_year")).intValue();
            }

            this.mMonth = ((Integer) params.get("month")).intValue();
            this.mYear = ((Integer) params.get("year")).intValue();
            this.mHasToday = false;
            this.mToday = -1;
            this.mCalendar.set(2, this.mMonth);
            this.mCalendar.set(1, this.mYear);
            this.mCalendar.set(5, 1);
            this.mDayOfWeekStart = this.mCalendar.get(7);
            if (params.containsKey("week_start")) {
                this.mWeekStart = ((Integer) params.get("week_start")).intValue();
            } else {
                this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
            }

            this.mNumCells = CalendarUtils.getDaysInMonth(this.mMonth, this.mYear);

            for (int i = 0; i < this.mNumCells; ++i) {
                int day = i + 1;
                if (this.sameDay(day, this.today)) {
                    this.mHasToday = true;
                    this.mToday = day;
                }

                this.mIsPrev = this.prevDay(day, this.today);
            }

            this.mNumRows = this.calculateNumRows();
        }
    }

    public void setOnDayClickListener(SimpleMonthView.OnDayClickListener onDayClickListener) {
        this.mOnDayClickListener = onDayClickListener;
    }

    public interface OnDayClickListener {
        void onDayClick(SimpleMonthView var1, CalendarDay var2);
    }

}
