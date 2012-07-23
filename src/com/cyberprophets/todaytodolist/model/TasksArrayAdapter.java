package com.cyberprophets.todaytodolist.model;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;

public class TasksArrayAdapter extends ArrayAdapter<Task> {

	private List<Task> tasks;
	private Activity context;

	public TasksArrayAdapter(Activity context, List<Task> tasks) {
		super(context, R.layout.todo_list_row, tasks);
		this.context = context;
		this.tasks = tasks;
	}

	static class ViewHolder {
		protected TextView title;
		protected CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.todo_list_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) view.findViewById(R.id.task_title);
			viewHolder.checkBox = (CheckBox) view
					.findViewById(R.id.task_is_done);
			viewHolder.checkBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							Task task = (Task) viewHolder.checkBox.getTag();
							task.setDone(buttonView.isChecked());
						}
					});
			view.setTag(viewHolder);
			viewHolder.checkBox.setTag(tasks.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkBox.setTag(tasks.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.title.setText(tasks.get(position).getTitle());
		holder.checkBox.setChecked(tasks.get(position).isDone());
		return view;
	}
}
