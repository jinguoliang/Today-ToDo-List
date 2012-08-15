package com.cyberprophets.todaytodolist;

import java.util.List;

import android.os.Bundle;

import com.cyberprophets.todaytodolist.model.Task;
import com.cyberprophets.todaytodolist.model.TasksArrayAdapter;

public class GlobalTasksActivity extends TasksListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_tasks_activity_layout);
		super.init(R.id.add_task, R.id.tasks_list_footer);
	}

	@Override
	protected void fillData() {
		List<Task> tasks = getModel().getNotCompleteTasks();
		TasksArrayAdapter adapter = new TasksArrayAdapter(getModel(), this,
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
						+ (tasks.size() - doneTasksCount));
	}

}
