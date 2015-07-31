package com.scutteam.lvyou.util.calendarlistview.library;

import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthAdapter.CalendarDay;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthAdapter.SelectedDays;

public interface DatePickerController {
	int getMaxYear();

	void onDayOfMonthSelected(int var1, int var2, int var3);
}
