package com.aj.projectscheduler.utils;

import java.util.Calendar;
import java.util.Date;

public class TaskScheduler {

	public static Date getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// ignore weekends
	public static Date getEndDate(Date startDate, int duration) {
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(startDate);
		int computedDuration = 0;
		while (computedDuration < duration) {
			endCal.add(Calendar.DAY_OF_MONTH, 1);
			if (isWeekday(endCal)) {
				computedDuration++;
			}
		}
		return endCal.getTime();
	}

	public static int getDuration(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		int workDays = 0;

		// Return 0 if start and end are the same
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(startDate);
			endCal.setTime(startDate);
		}

		do {
			// excluding start date
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			if (isWeekday(startCal)) {
				workDays++;
			}
		} while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); // excluding end date

		return workDays;
	}

	private static boolean isWeekday(Calendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
	}

	public static Date getNextWeekday(Date start) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		while (!isWeekday(cal)) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return cal.getTime();
	}

	public static Date getNextWeekday() {
		return getNextWeekday(getToday());
	}

	public static Date getLastWeekday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		while (!isWeekday(cal)) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return cal.getTime();
	}
}
