package com.cyberprophets.todaytodolist.model.dataobjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @author Mironov S.V.
 * @since 04.10.2012
 */
public final class Date {
	public static String DATE_FORMAT = "yyyy-MM-dd";
	private final Calendar calendar = Calendar.getInstance();

	public Date() {
		calendar.setTime(new java.util.Date());
	}

	private Date(java.util.Date date) {
		calendar.setTime(date);
	}

	private java.util.Date getDate() {
		return calendar.getTime();
	}

	private Calendar getCalendar() {
		return calendar;
	}

	public int getDay() {
		return getCalendar().get(Calendar.DAY_OF_MONTH);
	}

	public int getMonth() {
		return getCalendar().get(Calendar.MONTH);
	}

	public int getYear() {
		return getCalendar().get(Calendar.YEAR);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getDay();
		result = prime * result + getMonth();
		result = prime * result + getYear();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Date)) {
			return false;
		}
		Date other = (Date) obj;
		if (getCalendar() == null) {
			if (other.getCalendar() != null) {
				return false;
			}
		} else if (getDay() != other.getDay() || getMonth() != other.getMonth()
				|| getYear() != other.getYear()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(getDate());
	}

	public java.util.Date toDate() {
		return new java.util.Date(getYear(), getMonth(), getDay());
	}

	public static Date valueOf(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return new Date(dateFormat.parse(date));
	}

	public static Date valueOf(java.util.Date date) {
		if (date == null) {
			return null;
		}
		return new Date(date);
	}
}
