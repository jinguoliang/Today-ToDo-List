package com.cyberprophets.todaytodolist.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.adapters.TasksByDateAdapter;
import com.cyberprophets.todaytodolist.model.ModelListener;
import com.cyberprophets.todaytodolist.model.Task;

/**
 * 
 * @author Mironov S.V.
 * @since 01.08.2012
 */
public class TasksByDateActivity extends TasksListActivity implements
		ModelListener {
	private ImageButton nextDateButton;
	private ImageButton previousDateButton;
	private EditText selectedDateText;

	private static String DATE_FORMAT = "dd.MM.yyyy";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_by_date_activity_layout);

		previousDateButton = (ImageButton) findViewById(R.id.previous_date_button);
		nextDateButton = (ImageButton) findViewById(R.id.next_date_button);
		selectedDateText = (EditText) findViewById(R.id.selected_date);

		super.init(R.id.add_task, R.id.tasks_list_footer);

		previousDateButton
				.setOnClickListener(new PreviousDateButtonOnClickListener());
		nextDateButton.setOnClickListener(new NextDateButtonOnClickListener());
		getModel().addModelListener(this);
	}

	public ImageButton getNextDateButton() {
		return nextDateButton;
	}

	public ImageButton getPreviousDateButton() {
		return previousDateButton;
	}

	public EditText getSelectedDateText() {
		return selectedDateText;
	}

	@Override
	protected void fillData() {
		TasksByDateAdapter adapter = null;
		if (getListAdapter() == null) {
			adapter = new TasksByDateAdapter(getModel(), this, getDate()
					.getTime());
			setListAdapter(adapter);
		}
		outputFooterData();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String date = simpleDateFormat.format(getDate().getTime());
		getSelectedDateText().setText(date);
	}

	private class PreviousDateButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			getDate().add(Calendar.DAY_OF_MONTH, -1);
			fillData();
		}
	}

	private class NextDateButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			getDate().add(Calendar.DAY_OF_MONTH, 1);
			fillData();
		}

	}

	private void outputFooterData() {
		TasksByDateAdapter adapter = (TasksByDateAdapter) getListAdapter();
		int doneTasksCount = 0;
		for (Task task : adapter.getTasks()) {
			if (task.isDone()) {
				doneTasksCount++;
			}
		}

		getTasksListFooter().setText(
				getString(R.string.not_complete_tasks) + ": "
						+ (adapter.getTasks().size() - doneTasksCount) + "; "
						+ getString(R.string.complete_tasks) + ": "
						+ doneTasksCount);
	}

	public void taskCreated(Task task) {
		outputFooterData();
	}

	public void taskDeleted(Task task) {
		outputFooterData();
	}

	public void taskChanged(Task oldTask, Task newTask) {
		outputFooterData();

	}
}