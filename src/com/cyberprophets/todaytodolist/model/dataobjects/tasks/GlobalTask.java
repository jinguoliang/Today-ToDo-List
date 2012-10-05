package com.cyberprophets.todaytodolist.model.dataobjects.tasks;

import java.util.UUID;

/**
 * Глобальная задача
 * 
 * @author Mironov S.V.
 * @since 13.08.2012
 */
public class GlobalTask extends ComplexTask<GlobalTask> {
	public GlobalTask() {
		super();
	}

	public GlobalTask(String name) {
		super(name);
	}

	public GlobalTask(UUID id, String title, String description,
			boolean completed) {
		super(id, title, description, completed);
	}

	@Override
	public String toString() {
		return "GlobalTask [subTasks=" + subTasks + ", title=" + title
				+ ", description=" + description + ", completed=" + completed
				+ "]";
	}

}
