package com.cyberprophets.todaytodolist.adapters;

import java.util.List;

import android.app.Activity;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.Task;

public class CategoryTasksAdapter extends TasksAdapter {
	private List<Task> tasks;

	public CategoryTasksAdapter(Model model, Activity context) {
		super(model, context);
		tasks = getTasksFromModel();
		init();
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public int getCount() {
		return getTasks().size();
	}

	public Object getItem(int position) {
		return getTasks().get(position);
	}

	private List<Task> getTasksFromModel() {
		return getModel().getAllTasks();
	}

	@Override
	public void taskCreated(Task task) {
		tasks = getTasksFromModel();
		super.taskCreated(task);
	}

	@Override
	public void taskDeleted(Task task) {
		tasks = getTasksFromModel();
		super.taskCreated(task);
	}
}
