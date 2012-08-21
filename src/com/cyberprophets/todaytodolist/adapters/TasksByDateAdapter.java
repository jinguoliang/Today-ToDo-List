package com.cyberprophets.todaytodolist.adapters;

import java.util.Date;
import java.util.List;

import android.app.Activity;

import com.cyberprophets.todaytodolist.model.Category;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.Task;

/**
 * Адаптер для отображения задач по конкретной дате в ListView
 * 
 * @author Mironov S.V.
 * @since 16.08.2012
 */
public class TasksByDateAdapter extends TasksAdapter {
	private Date date;
	private List<Task> tasks;

	public TasksByDateAdapter(Model model, Activity context, Date date) {
		super(model, context);
		setDate(date);
		tasks = getTasksFromModel(getDate());
		init();
	}

	public int getCount() {
		return getTasks().size();
	}

	public Date getDate() {
		return date;
	}

	public Object getItem(int position) {
		return getTasks().get(position);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	private List<Task> getTasksFromModel(Date date) {
		return getModel().getTasksByDate(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void taskChanged(Task oldTask, Task newTask) {
		tasks = getTasksFromModel(getDate());
		super.taskChanged(oldTask, newTask);
	}

	@Override
	public void taskCreated(Task task) {
		tasks = getTasksFromModel(getDate());
		super.taskCreated(task);
	}

	@Override
	public void taskDeleted(Task task) {
		tasks = getTasksFromModel(getDate());
		super.taskCreated(task);
	}

	public void categoryCreated(Category category) {

	}

	public void categoryChanged(Category oldCategory, Category newCategory) {

	}

	public void categoryDeleted(Category category) {
	}
}
