package com.cyberprophets.todaytodolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cyberprophets.todaytodolist.model.Task;
import com.cyberprophets.todaytodolist.model.TasksArrayAdapter;

/**
 * 
 * @author Mironov S.V.
 * @since 01.08.2012
 */
public class TasksByDateActivity extends TasksListActivity {

	private ImageButton nextDateButton;
	private ImageButton previousDateButton;
	private EditText selectedDateText;
	private Calendar selectedDate;

	private static String DATE_FORMAT = "dd.MM.yyyy";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_by_date_activity_layout);

		previousDateButton = (ImageButton) findViewById(R.id.previous_date_button);
		nextDateButton = (ImageButton) findViewById(R.id.next_date_button);
		selectedDateText = (EditText) findViewById(R.id.selected_date);
		selectedDate = Calendar.getInstance();
		selectedDate.setTime(new Date());

		super.init(R.id.add_task, R.id.tasks_list_footer);

		previousDateButton
				.setOnClickListener(new PreviousDateButtonOnClickListener());
		nextDateButton.setOnClickListener(new NextDateButtonOnClickListener());
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

	public Calendar getSelectedDate() {
		return selectedDate;
	}

	@Override
	protected void fillData() {
		List<Task> tasks = getModel().getTasksByDate(
				getSelectedDate().getTime());
		ArrayAdapter<Task> adapter = new TasksArrayAdapter(getModel(), this,
				tasks);
		getTasksListView().setAdapter(adapter);
		int doneTasksCount = 0;
		for (Task task : tasks) {
			if (task.isDone()) {
				doneTasksCount++;
			}
		}

		getTasksListFooter().setText(
				getString(R.string.not_complete_tasks) + ": "
						+ (tasks.size() - doneTasksCount) + "; "
						+ getString(R.string.complete_tasks) + ": "
						+ doneTasksCount);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String date = simpleDateFormat.format(getSelectedDate().getTime());
		getSelectedDateText().setText(date);
	}

	private class PreviousDateButtonOnClickListener implements OnClickListener {

		public void onClick(View arg0) {
			getSelectedDate().add(Calendar.DAY_OF_MONTH, -1);
			fillData();
		}

	}

	private class NextDateButtonOnClickListener implements OnClickListener {

		public void onClick(View arg0) {
			getSelectedDate().add(Calendar.DAY_OF_MONTH, 1);
			fillData();
		}

	}
}