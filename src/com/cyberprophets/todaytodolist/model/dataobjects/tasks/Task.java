package com.cyberprophets.todaytodolist.model.dataobjects.tasks;

import java.util.UUID;

import com.cyberprophets.todaytodolist.model.dataobjects.DataObject;

/**
 * Класс задачи
 * 
 * @author Mironov S.V.
 * @since 13.08.2012
 */
public class Task extends DataObject {
	protected String title;
	protected String description;
	protected boolean completed = false;

	public Task() {
		super();
	}

	public Task(String title) {
		this();
		setTitle(title);
	}

	public Task(UUID id, String title, String description, boolean completed) {
		super(id);
		setTitle(title);
		setDescription(description);
		setCompleted(completed);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (completed ? 1231 : 1237);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Task other = (Task) obj;
		if (completed != other.completed) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Task [title=" + title + ", description=" + description
				+ ", completed=" + completed + "]";
	}

}
