package com.cyberprophets.todaytodolist.model.dataobjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Mironov S.V.
 * @since 04.10.2012
 */
public final class Date {
	public static String DATE_FORMAT = "yyyy-MM-dd";
	private final java.util.Date date;

	public Date() {
		date = new java.util.Date();
	}

	private Date(java.util.Date date) {
		this.date = date;
	}

	private java.util.Date getDate() {
		return date;
	}

	public int getDay() {
		return date.getDay();
	}

	public int getMonth() {
		return getDate().getMonth();
	}

	public int getYear() {
		return getDate().getYear();
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
		if (date == null) {
			if (other.date != null) {
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
