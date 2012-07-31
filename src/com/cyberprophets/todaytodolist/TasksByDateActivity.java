package com.cyberprophets.todaytodolist;

import java.util.Date;
import java.util.List;

import com.cyberprophets.todaytodolist.model.Task;
import com.cyberprophets.todaytodolist.model.TasksArrayAdapter;

import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * 
 * @author Mironov S.V.
 * @since 01.08.2012
 */
public class TasksByDateActivity extends TasksListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init(R.layout.tasks_by_date_activity_layout, R.id.add_task,
				R.id.tasks_list_footer);
	}

	@Override
	protected void fillData() {
		List<Task> tasks = getModel().getTasksByDate(new Date());
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
	}
}