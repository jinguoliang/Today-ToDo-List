package com.cyberprophets.todaytodolist.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.ModelListener;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;
import com.cyberprophets.todaytodolist.views.TaskView;

/**
 * Класс адаптера для отображения простых задач в виде списка
 * 
 * @author Mironov S.V.
 * @since 16.08.2012
 */
public abstract class BaseTasksAdapter extends BaseAdapter implements
		ModelListener {
	private static boolean DEFAULT_EXPANDED_TASK_STATE = false;
	private final Activity context;
	private final Model model;
	private final Map<UUID, Boolean> expandedTasks = new HashMap<UUID, Boolean>();
	private Task expandedTask;

	public BaseTasksAdapter(Model model, Activity context) {
		this.context = context;
		this.model = model;
		getModel().addModelListener(this);
	}

	protected void init() {
		expandedTask = null;
		expandedTasks.clear();
		for (int i = 0; i < getCount(); i++) {
			expandedTasks.put(((Task) getItem(i)).getId(),
					DEFAULT_EXPANDED_TASK_STATE);
		}
	}

	public Activity getContext() {
		return context;
	}

	private Map<UUID, Boolean> getExpandedTasks() {
		return expandedTasks;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public Model getModel() {
		return model;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TaskView taskView = null;
		if (convertView == null) {
			taskView = new TaskView(getContext(), (Task) getItem(position),
					getModel());
		} else {
			taskView = (TaskView) convertView;
			Task task = (Task) getItem(position);
			taskView.setTask(task);
			boolean isExpanded = taskView.expandTask(getExpandedTasks().get(
					task.getId()));
			getExpandedTasks().put(task.getId(), isExpanded);
			taskView.fillView();
		}
		return taskView;
	}

	public void taskChanged(Task oldTask, Task newTask) {
		notifyDataSetChanged();
	}

	public void taskCreated(Task task) {
		getExpandedTasks().put(task.getId(), DEFAULT_EXPANDED_TASK_STATE);
		notifyDataSetChanged();
	}

	public void taskDeleted(Task task) {
		getExpandedTasks().remove(task.getId());
		notifyDataSetChanged();
	}

	public void toogleTask(int position) {
		Task task = (Task) getItem(position);

		if (expandedTask != null && !expandedTask.equals(task)) {
			getExpandedTasks().put(expandedTask.getId(), false);
		}
		expandedTask = task;
		getExpandedTasks().put(task.getId(),
				!getExpandedTasks().get(task.getId()));
		notifyDataSetChanged();
	}
}
