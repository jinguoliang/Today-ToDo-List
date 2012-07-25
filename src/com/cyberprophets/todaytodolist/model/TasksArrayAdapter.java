package com.cyberprophets.todaytodolist.model;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.EditTaskActivity;
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

	public List<Task> getTasks() {
		return tasks;
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
			LayoutInflater inflator = getContext().getLayoutInflater();
			view = inflator.inflate(R.layout.todo_list_row, null);

			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.task = getTasks().get(position);
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

			view.setOnClickListener(new OnItmeClickListener(viewHolder));
			view.setTag(viewHolder);
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).task = getTasks().get(position);
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
				viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
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
			if (isChecked) {
				viewHolder.title.setTextAppearance(getContext(),
						R.style.boldText);
				viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
				if (task.isDone()) {
					return;
				}

			} else {
				viewHolder.title.setTextAppearance(getContext(),
						R.style.normalText);
				viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags()
						^ Paint.STRIKE_THRU_TEXT_FLAG);
				if (!task.isDone()) {
					return;
				}
			}
			task.setDone(isChecked);
			getModel().saveTask(task);
		}
	};

	@Override
	public int getCount() {
		return getTasks().size();
	}

	@Override
	public Task getItem(int position) {
		return getTasks().get(position);
	}

	@Override
	public long getItemId(int position) {
		// return super.getItemId(position);
		return 0;
	}

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
	}

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 25.07.2012
	 */
	private class OnItmeClickListener implements OnClickListener {

		private ViewHolder viewHolder;

		private OnItmeClickListener(ViewHolder viewHolder) {
			this.viewHolder = viewHolder;
		}

		public ViewHolder getViewHolder() {
			return viewHolder;
		}

		public void onClick(View v) {
			Intent intent = new Intent(getContext(), EditTaskActivity.class);
			intent.putExtra("id", getViewHolder().task.getId().toString());
			getContext().startActivityForResult(intent, 1);
		}
	};

}
