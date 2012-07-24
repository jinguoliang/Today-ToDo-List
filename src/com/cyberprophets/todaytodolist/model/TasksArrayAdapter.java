package com.cyberprophets.todaytodolist.model;

import java.util.List;

import android.app.Activity;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;

public class TasksArrayAdapter extends ArrayAdapter<Task> {

	private List<Task> tasks;
	private Activity context;
	private Model model;

	public TasksArrayAdapter(Model model, Activity context, List<Task> tasks) {
		super(context, R.layout.todo_list_row, tasks);
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

	static class ViewHolder {
		protected Task task;
		protected TextView title;
		protected CheckBox checkBox;
		protected ImageButton deletTaskButton;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.todo_list_row, null);

			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.task = tasks.get(position);
			viewHolder.title = (TextView) view.findViewById(R.id.task_title);

			viewHolder.checkBox = (CheckBox) view
					.findViewById(R.id.task_is_done);
			viewHolder.checkBox
					.setOnCheckedChangeListener(new OnCheckedTaskDoneChangeListener(
							viewHolder));

			viewHolder.deletTaskButton = (ImageButton) view
					.findViewById(R.id.delete_task_button);
			viewHolder.deletTaskButton
					.setOnClickListener(new DeleteTaskButtonOnClickListener(
							viewHolder));
			view.setTag(viewHolder);
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).task = tasks.get(position);
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		initViewHolder(holder);
		return view;
	}

	private void initViewHolder(ViewHolder viewHolder) {
		if ((viewHolder != null) && (viewHolder.task != null)) {
			Task task = viewHolder.task;
			viewHolder.title.setText(task.getTitle());
			viewHolder.checkBox.setChecked(task.isDone());
			if (task.isDone()) {
				viewHolder.checkBox.setChecked(true);
				viewHolder.title.setTextAppearance(getContext(),
						R.style.boldText);
			} else {
				viewHolder.checkBox.setChecked(false);
				viewHolder.title.setTextAppearance(getContext(),
						R.style.normalText);
			}
		}
	}

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 24.07.2012
	 */
	private class OnCheckedTaskDoneChangeListener implements
			CompoundButton.OnCheckedChangeListener {
		private ViewHolder viewHolder;

		public OnCheckedTaskDoneChangeListener(ViewHolder viewHolder) {
			this.viewHolder = viewHolder;
		}

		public ViewHolder getViewHolder() {
			return viewHolder;
		}

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Task task = getViewHolder().task;
			task.setDone(buttonView.isChecked());
			getModel().saveTask(task);
			if (buttonView.isChecked()) {
				viewHolder.title.setTextAppearance(getContext(),
						R.style.boldText);
			} else {
				viewHolder.title.setTextAppearance(getContext(),
						R.style.normalText);
			}
		}
	};

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 24.07.2012
	 */
	private class DeleteTaskButtonOnClickListener implements OnClickListener {
		private ViewHolder viewHolder;

		public DeleteTaskButtonOnClickListener(ViewHolder viewHolder) {
			this.viewHolder = viewHolder;
		}

		public ViewHolder getViewHolder() {
			return viewHolder;
		}

		public void onClick(View v) {
			getModel().deleteTask(getViewHolder().task);
		}
	};
}
