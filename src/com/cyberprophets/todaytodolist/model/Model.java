package com.cyberprophets.todaytodolist.model;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.cyberprophets.todaytodolist.model.db.DatabaseAdapter;

/**
 * 
 * @author Mironov S.V.
 * @since 18.07.2012
 */
public class Model {
	private final Context context;
	private SourceAdapter sourceAdapter;

	public Model(Context context) {
		this.context = context;
		setSourceAdapter(new DatabaseAdapter(getContext()));
	}

	public void activate() {
		getSourceAdapter().open();
	}

	public void deactivate() {
		getSourceAdapter().close();
	}

	public void createNewTask(String title) {
		Task task = new Task(title, new Date());
		getSourceAdapter().saveTask(task);
	}

	public void saveTask(Task task) {
		getSourceAdapter().saveTask(task);
	}

	public void deleteTask(Task task) {
		getSourceAdapter().deleteTask(task);
	}

	public List<Task> getTasksByDate(Date date) {
		return getSourceAdapter().getTasksByDate(date);
	}

	public List<Task> getAllTasks() {
		return getSourceAdapter().getAllTasks();
	}

	private Context getContext() {
		return context;
	}

	private SourceAdapter getSourceAdapter() {
		return sourceAdapter;
	}

	private void setSourceAdapter(SourceAdapter sourceAdapter) {
		this.sourceAdapter = sourceAdapter;
	}
}
