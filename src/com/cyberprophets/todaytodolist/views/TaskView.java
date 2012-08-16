package com.cyberprophets.todaytodolist.views;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.Task;

/**
 * Класс, отвечающий за отображение задачи
 * 
 * @author Mironov S.V.
 * @since 15.08.2012
 */
public class TaskView extends LinearLayout {
	private final TextView title;
	private final TextView note;
	private final CheckBox isDone;
	private final ImageButton deleteButton;
	private Task task;
	private final Model model;
	private final View noteLayout;

	public TaskView(Context context, Task task, Model model) {
		super(context);
		View view = inflate(getContext(), R.layout.task_view, null);
		addView(view);

		this.model = model;
		setTask(task);
		title = (TextView) findViewById(R.id.task_title);
		note = (TextView) findViewById(R.id.task_note);
		isDone = (CheckBox) view.findViewById(R.id.task_is_done);
		deleteButton = (ImageButton) view.findViewById(R.id.delete_task_button);
		noteLayout = findViewById(R.id.note_layout);

		fillView();

		getIsDone().setOnCheckedChangeListener(
				new OnCheckedTaskDoneChangeListener());
		getDeleteButton().setOnClickListener(
				new DeleteTaskButtonOnClickListener());

		showNote(false);
	}

	private View getNoteLayout() {
		return noteLayout;
	}

	public void showNote(boolean show) {
		getNoteLayout().setVisibility(show ? VISIBLE : GONE);
	}

	public void fillView() {
		if (getTask() == null) {
			return;
		}
		getTitle().setText(getTask().getTitle());
		getNote().setText(getTask().getDescription());
		if (getTask().isDone()) {
			getIsDone().setChecked(true);
			getTitle().setTextAppearance(getContext(), R.style.boldText);
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

	public ImageButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * Слушатель нажатия кнопки завершения задачи
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
				getTitle().setTextAppearance(getContext(), R.style.boldText);
				getTitle().setPaintFlags(
						getTitle().getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);
				if (task.isDone()) {
					return;
				}

			} else {
				getTitle().setTextAppearance(getContext(), R.style.normalText);
				getTitle().setPaintFlags(
						getTitle().getPaintFlags()
								^ Paint.STRIKE_THRU_TEXT_FLAG);
				if (!task.isDone()) {
					return;
				}
			}
			task.setDone(isChecked);
			getModel().saveTask(task);
		}
	};

	/**
	 * Слушатель нажатия кнопки удаления задачи
	 * 
	 * @author Mironov S.V.
	 * @since 24.07.2012
	 */
	private class DeleteTaskButtonOnClickListener implements OnClickListener {
		public void onClick(View v) {
			getModel().deleteTask(getTask());
		}
	};
}
