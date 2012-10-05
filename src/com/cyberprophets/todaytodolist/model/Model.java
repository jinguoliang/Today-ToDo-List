package com.cyberprophets.todaytodolist.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;

import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.DailyTask;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.GlobalTask;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;
import com.cyberprophets.todaytodolist.model.sourcemanager.SourceManager;
import com.cyberprophets.todaytodolist.model.sourcemanager.db.DatabaseSourceManager;

/**
 * Модель менеджера задач
 * 
 * @author Mironov S.V.
 * @since 18.07.2012
 */
public class Model {
	private static List<ModelListener> listeners = new ArrayList<ModelListener>();
	private final Context context;
	private SourceManager sourceManager;

	public Model(Context context) {
		this.context = context;
		setSourceManager(new DatabaseSourceManager(getContext()));
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
		getSourceManager().open();
	}

	public void deactivate() {
		getSourceManager().close();
	}

	private Context getContext() {
		return context;
	}

	private SourceManager getSourceManager() {
		return sourceManager;
	}

	private void setSourceManager(SourceManager sourceAdapter) {
		this.sourceManager = sourceAdapter;
	}

	public List<DailyTask> getDailyTasks(Date date) {
		return getSourceManager().getDailyTasks(date);
	}

	public DailyTask getDailyTask(UUID id) {
		return getSourceManager().getDailyTask(id);
	}

	public List<GlobalTask> getAllGlobalTasks() {
		return getSourceManager().getAllGlobalTasks();
	}

	public GlobalTask getGlobalTask(UUID id) {
		return getSourceManager().getGlobalTask(id);
	}

	public DailyTask createNewDailyTask(String title, Date date) {
		DailyTask newTask = new DailyTask(title, date);
		if (!getSourceManager().save(newTask)) {
			return null;
		}

		for (ModelListener listener : getListeners()) {
			listener.taskCreated(newTask);
		}
		return newTask;
	}

	public GlobalTask createNewGlobalTask(String title, GlobalTask parentTask) {
		GlobalTask newTask = new GlobalTask(title);
		if (parentTask != null) {
			parentTask.getSubTasks().add(newTask);
			getSourceManager().save(parentTask);
		} else {
			if (!getSourceManager().save(newTask)) {
				return null;
			}
		}
		for (ModelListener listener : getListeners()) {
			listener.taskCreated(newTask);
		}
		return newTask;
	}

	public void saveTask(Task task) {
		getSourceManager().save(task);

		for (ModelListener listener : getListeners()) {
			listener.taskSaved(task);
		}
	}

	public void deleteTask(Task task) {
		getSourceManager().delete(task);
		for (ModelListener listener : getListeners()) {
			listener.taskDeleted(task);
		}
	}
}
