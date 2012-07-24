package com.cyberprophets.todaytodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.Task;
import com.cyberprophets.todaytodolist.model.TasksArrayAdapter;

public class TodayActivity extends Activity {
	private static final String KEY_TASKID = "id";

	private EditText newTaskTitle;
	private ListView tasksListView;
	private Model model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.today_activity);
		tasksListView = (ListView) findViewById(R.id.list);

		this.model = new Model(this);
		getModel().activate();
		fillData();

		tasksListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		tasksListView.setOnItemClickListener(new OnTaskItemClickListener());
		newTaskTitle = (EditText) findViewById(R.id.add_task);
		newTaskTitle.setInputType(InputType.TYPE_CLASS_TEXT);

		newTaskTitle.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					EditText editText = (EditText) v;
					getModel().createNewTask(editText.getText().toString());
					editText.getText().clear();
					return true;
				}
				return false;
			}
		});

		registerForContextMenu(tasksListView);
	}

	public Model getModel() {
		return model;
	}

	private void fillData() {
		ArrayAdapter<Task> adapter = new TasksArrayAdapter(getModel(), this,
				getModel().getAllTasks());
		tasksListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.today_activity, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.today_activity_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_delete:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			getModel().deleteTask(
					(Task) tasksListView.getItemAtPosition(info.position));
			return true;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	};

	private class OnTaskItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(TodayActivity.this,
					EditTaskActivity.class);
			intent.putExtra(KEY_TASKID, ((Task) tasksListView
					.getItemAtPosition(arg2)).getId().toString());
			startActivity(intent);
		}
	}

}
