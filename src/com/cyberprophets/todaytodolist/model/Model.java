package com.cyberprophets.todaytodolist.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Context;

import com.cyberprophets.todaytodolist.model.db.DatabaseAdapter;

/**
 * 
 * @author Mironov S.V.
 * @since 18.07.2012
 */
public class Model {
	private static List<ModelListener> listeners = new ArrayList<ModelListener>();
	private final Context context;
	private SourceAdapter sourceAdapter;

	public Model(Context context) {
		this.context = context;
		setSourceAdapter(new DatabaseAdapter(getContext()));
	}

	public List<ModelListener> getListeners() {
		return listeners;
	}

	public void addModelListener(ModelListener modelListener) {
		if (!getListeners().contains(modelListener)) {
			getListeners().add(modelListener);
		}
	}

	public void activate() {
		getSourceAdapter().open();
	}

	public void deactivate() {
		getSourceAdapter().close();
	}

	public void createNewTask(String title) {
		createNewTask(title, new Date());
	}

	public void createNewTask(String title, Date date) {
		Task task = new Task(title, date);
		getSourceAdapter().saveTask(task);

		for (ModelListener listener : getListeners()) {
			listener.taskCreated(task);
		}
	}

	public Task getTask(UUID id) {
		return getSourceAdapter().getTask(id);
	}

	public void saveTask(Task task) {
		Task oldTask = getTask(task.getId());
		getSourceAdapter().saveTask(task);

		for (ModelListener listener : getListeners()) {
			listener.taskChanged(oldTask, task);
		}
	}

	public void deleteTask(Task task) {
		getSourceAdapter().deleteTask(task);

		for (ModelListener listener : getListeners()) {
			listener.taskDeleted(task);
		}
	}

	public List<Task> getTasksByDate(Date date) {
		return getSourceAdapter().getTasksByDate(date);
	}

	public List<Task> getNotCompleteTasks() {
		return getSourceAdapter().getNotCompleteTasks();
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
