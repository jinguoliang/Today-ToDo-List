package com.cyberprophets.todaytodolist.activities;

import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.animation.ActivitySwitcher;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.DailyTask;

/**
 * 
 * 
 * @author Mironov
 * @since 23.07.2012
 */
public class EditDailyTaskActivity extends Activity {
	private static final String KEY_TASKID = "id";

	private static final int RESULT_OK = 0;
	private static final int RESULT_ERROR = 1;

	private EditText taskTitle;
	private EditText taskDescription;
	private DatePicker taskDate;
	private Button confirmButton;

	private UUID editingTaskId;

	private Model model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_daily_task_activity);

		taskTitle = (EditText) findViewById(R.id.title);
		taskDescription = (EditText) findViewById(R.id.description);
		taskDate = (DatePicker) findViewById(R.id.date);
		confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new ConfirmButtonOnClickListener());

		model = new Model(this);

		String stringTaskId = savedInstanceState != null ? savedInstanceState
				.getString(KEY_TASKID) : null;
		editingTaskId = stringTaskId != null ? UUID.fromString(stringTaskId)
				: null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		ActivitySwitcher.animationOut(findViewById(R.id.container),
				getWindowManager());
		;
		getModel().deactivate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivitySwitcher.animationIn(findViewById(R.id.container),
				getWindowManager());
		getModel().activate();
		getTaskIdFromIntent();
		fillActivity();
	}

	private void fillActivity() {
		if (editingTaskId != null) {
			DailyTask editingTask = getModel().getDailyTask(editingTaskId);
			getTaskTitle().setText(editingTask.getTitle());
			getTaskDescription().setText(editingTask.getDescription());
			Date taskDate = editingTask.getDate();
			getTaskDate().updateDate(taskDate.getYear(), taskDate.getMonth(),
					taskDate.getDay());
		}
	}

	private void getTaskIdFromIntent() {
		if (editingTaskId == null) {
			Bundle extras = getIntent().getExtras();
			String stringTaskId = extras != null ? extras.getString(KEY_TASKID)
					: null;
			editingTaskId = stringTaskId != null ? UUID
					.fromString(stringTaskId) : null;
		}
	}

	public EditText getTaskTitle() {
		return taskTitle;
	}

	public EditText getTaskDescription() {
		return taskDescription;
	}

	public DatePicker getTaskDate() {
		return taskDate;
	}

	public Button getConfirmButton() {
		return confirmButton;
	}

	public Model getModel() {
		return model;
	}

	public UUID getEditingTaskId() {
		return editingTaskId;
	}

	private int saveTask() {
		String title = getTaskTitle().getText().toString();
		String description = getTaskDescription().getText().toString();
		Calendar calendar = Calendar.getInstance();
		calendar.set(getTaskDate().getYear(), getTaskDate().getMonth(),
				getTaskDate().getDayOfMonth());
		Date date = Date.valueOf(calendar.getTime());

		DailyTask editingTask = getModel().getDailyTask(getEditingTaskId());
		if (editingTask != null) {
			editingTask.setTitle(title);
			editingTask.setDescription(description);
			editingTask.setDate(date);
			getModel().saveTask(editingTask);
			return RESULT_OK;
		}
		return RESULT_ERROR;
	}

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 24.07.2012
	 */
	private class ConfirmButtonOnClickListener implements OnClickListener {

		public void onClick(View v) {
			int result = saveTask();
			setResult(result);

			switch (result) {
			case RESULT_OK:
				Toast.makeText(EditDailyTaskActivity.this,
						R.string.task_saved_message, Toast.LENGTH_SHORT).show();
				break;
			case RESULT_ERROR:
				Toast.makeText(EditDailyTaskActivity.this,
						R.string.task_not_saved_message, Toast.LENGTH_SHORT)
						.show();
				break;
			}

			ActivitySwitcher.animationOut(findViewById(R.id.container),
					getWindowManager(),
					new ActivitySwitcher.AnimationFinishedListener() {
						public void onAnimationFinished() {
						}
					});
			finish();
		}

	};
}
