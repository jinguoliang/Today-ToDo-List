package com.cyberprophets.todaytodolist.activities;

import android.os.Bundle;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.adapters.CategoryTasksAdapter;
import com.cyberprophets.todaytodolist.adapters.TasksAdapter;

public class GlobalTasksActivity extends TasksListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_tasks_activity_layout);
		super.init(R.id.add_task, R.id.tasks_list_footer);
	}

	@Override
	protected void fillData() {
		// List<Task> tasks = getModel().getNotCompleteTasks();
		TasksAdapter adapter = new CategoryTasksAdapter(getModel(), this);
		setListAdapter(adapter);
		// int doneTasksCount = 0;
		// for (Task task : tasks) {
		// if (task.isDone()) {
		// doneTasksCount++;
		// }
		// }
		//
		// getTasksListFooter().setText(
		// getString(R.string.not_complete_tasks) + ": "
		// + (tasks.size() - doneTasksCount));
	}

}
