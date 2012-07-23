package com.cyberprophets.todaytodolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.Task;
import com.cyberprophets.todaytodolist.model.TasksArrayAdapter;

public class TodayActivity extends Activity {

	private ListView tasksListView;
	private Model model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.today_activity);
		tasksListView = (ListView) findViewById(R.id.task_list);

		this.model = new Model(this);
		getModel().activate();
		model.createNewTask("Test task");
		fillData();

		tasksListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	private void fillData() {
		ArrayAdapter<Task> adapter = new TasksArrayAdapter(this, getModel()
				.getAllTasks());
		tasksListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.today_activity, menu);
		return true;
	}

	public Model getModel() {
		return model;
	}

}
