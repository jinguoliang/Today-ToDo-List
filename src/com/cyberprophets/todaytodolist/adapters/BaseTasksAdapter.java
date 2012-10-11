package com.cyberprophets.todaytodolist.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.ModelListener;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;
import com.cyberprophets.todaytodolist.views.TaskView;

/**
 * Адаптер для отображения простых задач в виде списка
 * 
 * @author Mironov S.V.
 * @since 16.08.2012
 */
public abstract class BaseTasksAdapter extends BaseAdapter implements
		ModelListener {
	private final Activity context;
	private final Model model;
	private Task previousExpandedTask;
	private Task currentExpandedTask;

	public BaseTasksAdapter(Model model, Activity context) {
		this.context = context;
		this.model = model;
		getModel().addModelListener(this);
	}

	protected void init() {
		currentExpandedTask = null;
	}

	public Activity getContext() {
		return context;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public Model getModel() {
		return model;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TaskView taskView = null;
		Task task = (Task) getItem(position);
		if (convertView == null) {
			taskView = new TaskView(getContext(), task, getModel());
			taskView.setExpanded(false);
		} else {
			taskView = (TaskView) convertView;
			taskView.setTask(task);
			taskView.setExpanded(false);
		}
		if (task.equals(previousExpandedTask)) {
			taskView.setExpanded(true);
			taskView.expandTask(false);
			previousExpandedTask = null;
		}

		if (task.equals(currentExpandedTask)) {
			boolean isExpanded = taskView.expandTask(task
					.equals(currentExpandedTask) ? true : false);
			if (!isExpanded) {
				currentExpandedTask = null;
			}
		}
		taskView.fillView();
		return taskView;
	}

	public void taskChanged(Task oldTask, Task newTask) {
		notifyDataSetChanged();
	}

	public void taskCreated(Task task) {
		notifyDataSetChanged();
	}

	public void taskDeleted(Task task) {
		notifyDataSetChanged();
	}

	public void toogleTask(int position) {
		Task task = (Task) getItem(position);
		if (task.equals(currentExpandedTask)) {
			previousExpandedTask = currentExpandedTask;
			currentExpandedTask = null;
		} else {
			previousExpandedTask = currentExpandedTask;
			currentExpandedTask = task;
		}
		notifyDataSetChanged();
	}

}
