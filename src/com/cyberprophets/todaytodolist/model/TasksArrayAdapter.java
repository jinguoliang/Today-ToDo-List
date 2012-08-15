package com.cyberprophets.todaytodolist.model;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class TasksArrayAdapter extends BaseAdapter {
	private List<Task> tasks;
	private Activity context;
	private Model model;

	public TasksArrayAdapter(Model model, Activity context, List<Task> tasks) {
		this.context = context;
		this.tasks = tasks;
		this.model = model;
	}

	public Activity getContext() {
		return context;
	}

	public Model getModel() {
		return model;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	static class ViewHolder {
		protected Task task;
		protected TextView title;
		protected CheckBox checkBox;
		protected ImageButton deletTaskButton;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TaskView taskView = null;
		if (convertView == null) {
			taskView = new TaskView(getContext(), (Task) getItem(position),
					getModel());
			taskView.setOnClickListener(new OnTaskViewClickListener());
		} else {
			taskView = (TaskView) convertView;
			taskView.setTask((Task) getItem(position));
			taskView.fillView();
		}
		return taskView;
	}

	public int getCount() {
		return getTasks().size();
	}

	public Object getItem(int arg0) {
		return getTasks().get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	};

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 25.07.2012
	 */
	private class OnTaskViewClickListener implements OnClickListener {

		public void onClick(View v) {
			TaskView taskView = (TaskView) v;
			taskView.toggleNote();
			notifyDataSetChanged();
			// Intent intent = new Intent(getContext(), EditTaskActivity.class);
			// intent.putExtra("id", taskView.getTask().getId().toString());
			// (getContext()).startActivityForResult(intent, 1);
		}
	}

}
