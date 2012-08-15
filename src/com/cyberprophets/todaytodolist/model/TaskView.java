package com.cyberprophets.todaytodolist.model;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;

/**
 * Класс, отвечающий за отображение задачи
 * 
 * @author Mironov S.V.
 * @since 15.08.2012
 */
public class TaskView extends LinearLayout {
	private TextView title;
	private TextView note;
	private CheckBox isDone;
	private ImageButton deleteButton;
	private Task task;
	private Model model;

	public TaskView(Context context, Task task, Model model) {
		super(context);
		this.model = model;
		setTask(task);
		View view = inflate(getContext(), R.layout.task_view, null);
		addView(view);
		title = (TextView) findViewById(R.id.task_title);
		note = (TextView) findViewById(R.id.task_note);
		isDone = (CheckBox) view.findViewById(R.id.task_is_done);
		deleteButton = (ImageButton) view.findViewById(R.id.delete_task_button);

		fillView();
		showNote(false);

		getIsDone().setOnCheckedChangeListener(
				new OnCheckedTaskDoneChangeListener());
		getDeleteButton().setOnClickListener(
				new DeleteTaskButtonOnClickListener());
	}

	public void toggleNote() {
		showNote(!noteIsShow());
	}

	public boolean noteIsShow() {
		if (findViewById(R.id.note_layout).getVisibility() == VISIBLE) {
			return true;
		}
		return false;
	}

	public void showNote(boolean show) {
		findViewById(R.id.note_layout).setVisibility(show ? VISIBLE : GONE);
	}

	public void fillView() {
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
