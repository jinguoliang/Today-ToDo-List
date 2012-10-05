package com.cyberprophets.todaytodolist.model.dataobjects.tasks;

import java.util.UUID;

import com.cyberprophets.todaytodolist.model.dataobjects.Date;

/**
 * Задача на день
 * 
 * @author Mironov S.V.
 * @since 03.10.2012
 */
public class DailyTask extends Task {
	protected Date date;

	public DailyTask() {
		super();
	}

	public DailyTask(String name, Date date) {
		super(name);
		setDate(date);
	}

	public DailyTask(UUID id, String title, String description,
			boolean completed, Date date) {
		super(id, title, description, completed);
		setDate(date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DailyTask other = (DailyTask) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (date.getDay() != other.date.getDay()
				|| date.getMonth() != other.date.getMonth()
				|| date.getYear() != other.date.getYear()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DailyTask [date=" + date + ", title=" + title
				+ ", description=" + description + ", completed=" + completed
				+ "]";
	}

}
