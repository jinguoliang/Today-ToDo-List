package com.cyberprophets.todaytodolist.model.dataobjects.tasks;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Класс задачи со списком подзадач
 * 
 * @author Mironov S.V.
 * @since 03.10.2012
 */
public class ComplexTask<T extends Task> extends Task {
	protected List<T> subTasks = new LinkedList<T>();

	public ComplexTask() {
		super();
	}

	public ComplexTask(String title) {
		super(title);
	}

	public ComplexTask(UUID id, String title, String description,
			boolean completed) {
		super(id, title, description, completed);
	}

	public List<T> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(List<T> subTasks) {
		this.subTasks = subTasks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((subTasks == null) ? 0 : subTasks.hashCode());
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
		ComplexTask other = (ComplexTask) obj;
		if (subTasks == null) {
			if (other.subTasks != null) {
				return false;
			}
		} else if (!subTasks.equals(other.subTasks)) {
			return false;
		}
		return true;
	}

}
