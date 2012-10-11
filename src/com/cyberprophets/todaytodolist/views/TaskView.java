package com.cyberprophets.todaytodolist.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.activities.EditDailyTaskActivity;
import com.cyberprophets.todaytodolist.animation.AnimationUtils;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;

/**
 * �����, ���������� �� ����������� ������
 * 
 * @author Mironov S.V.
 * @since 15.08.2012
 */
public class TaskView extends LinearLayout {
	private final TextView title;
	private final TextView note;
	private final CheckBox isDone;
	private final ImageView editButton;
	private final ImageView deleteButton;
	private Task task;
	private final Model model;
	private final View taskActionsLayout;
	private final View noteLayout;

	public TaskView(Activity context, Task task, Model model) {
		super(context);
		View view = inflate(getContext(), R.layout.task_view, null);
		addView(view);

		this.model = model;
		setTask(task);
		title = (TextView) findViewById(R.id.task_title);
		note = (TextView) findViewById(R.id.task_note);
		isDone = (CheckBox) view.findViewById(R.id.task_is_done);
		editButton = (ImageView) view.findViewById(R.id.edit_task_button);
		deleteButton = (ImageView) view.findViewById(R.id.delete_task_button);
		taskActionsLayout = findViewById(R.id.task_actions_layout);
		noteLayout = findViewById(R.id.note_layout);

		fillView();

		getIsDone().setOnCheckedChangeListener(
				new OnCheckedTaskDoneChangeListener());
		getEditButton().setOnClickListener(new EditTaskButtonOnClickListener());
		getDeleteButton().setOnClickListener(
				new DeleteTaskButtonOnClickListener());
	}

	private View getTaskActionsLayout() {
		return taskActionsLayout;
	}

	private View getNoteLayout() {
		return noteLayout;
	}

	/**
	 * The method expands additional information about task.
	 * 
	 * @param expand
	 * @return
	 */
	public boolean expandTask(boolean expand) {
		// TODO проверить метод когда добавляют описание для задачи после
		// редактирования

		Animation expandAnimation = AnimationUtils.createExpandAnimation(
				getTaskActionsLayout(), expand);
		getTaskActionsLayout().startAnimation(expandAnimation);
		// getTaskActionsLayout().setVisibility(expand ? VISIBLE : GONE);

		if (getTask().getDescription() == null
				|| getTask().getDescription().length() == 0) {
			getNoteLayout().setVisibility(GONE);
		} else {
			expandAnimation = AnimationUtils.createExpandAnimation(
					getNoteLayout(), expand);
			getNoteLayout().startAnimation(expandAnimation);
		}
		return expand;
	}

	/**
	 * The method setExpanded() works without animation in contrast expandTask()
	 * method.
	 * 
	 * @param expanded
	 */
	public void setExpanded(boolean expanded) {
		getTaskActionsLayout().setVisibility(expanded ? VISIBLE : GONE);

		if (getTask().getDescription() == null
				|| getTask().getDescription().length() == 0) {
			getNoteLayout().setVisibility(GONE);
		} else {
			getNoteLayout().setVisibility(expanded ? VISIBLE : GONE);
		}
	}

	public void fillView() {
		if (getTask() == null) {
			return;
		}
		getTitle().setText(getTask().getTitle());
		getNote().setText(getTask().getDescription());
		if (getTask().isCompleted()) {
			getIsDone().setChecked(true);
			getTitle().setTextAppearance(getContext(),
					R.style.strikethroughText);
			getTitle().setPaintFlags(
					getTitle().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			getIsDone().setChecked(false);
			getTitle().setTextAppearance(getContext(), R.style.normalText);
		}
	}

	public Model getModel() {
		return model;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public TextView getTitle() {
		return title;
	}

	public TextView getNote() {
		return note;
	}

	public CheckBox getIsDone() {
		return isDone;
	}

	public ImageView getEditButton() {
		return editButton;
	}

	private ImageView getDeleteButton() {
		return deleteButton;
	}

	/**
	 * 
	 * 
	 * @author Mironov S.V.
	 * @since 24.07.2012
	 */
	private class OnCheckedTaskDoneChangeListener implements
			CompoundButton.OnCheckedChangeListener {

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Task task = getTask();
			if (isChecked) {
				getTitle().setTextAppearance(getContext(),
						R.style.strikethroughText);
				getTitle().setPaintFlags(
						getTitle().getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);
				if (task.isCompleted()) {
					return;
				}

			} else {
				getTitle().setTextAppearance(getContext(), R.style.normalText);
				getTitle().setPaintFlags(
						getTitle().getPaintFlags()
								^ Paint.STRIKE_THRU_TEXT_FLAG);
				if (!task.isCompleted()) {
					return;
				}
			}
			task.setCompleted(isChecked);
			getModel().saveTask(task);
		}
	};

	/**
	 * 
	 * 
	 * @author Mironov S.V.
	 * @since 24.07.2012
	 */
	private class EditTaskButtonOnClickListener implements OnClickListener {
		public void onClick(View v) {
			final Intent intent = new Intent(getContext(),
					EditDailyTaskActivity.class);
			intent.putExtra("id", getTask().getId().toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			final Activity contextActivity = (Activity) getContext();
			contextActivity.startActivityForResult(intent, 1);
		}
	};

	private class DeleteTaskButtonOnClickListener implements OnClickListener {

		public void onClick(View v) {
			Animation animation = android.view.animation.AnimationUtils
					.loadAnimation(getContext(), R.anim.push_left_out);
			startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					// setVisibility(GONE);
					getModel().deleteTask(getTask());
				}
			});

		}

	}
}
