package com.cyberprophets.todaytodolist;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.ModelListener;
import com.cyberprophets.todaytodolist.model.Task;

/**
 * 
 * @author Mironov S.V.
 * @since 01.08.2012
 */
public abstract class TasksListActivity extends ListActivity implements
		ModelListener {
	private static final String KEY_TASKID = "id";
	private static final int ACTIVITY_EDIT = 1;

	private EditText newTaskTitle;
	private TextView tasksListFooter;
	private ListView tasksListView;
	private Model model;

	private final Handler uiHandler = new Handler();

	protected void init(int activityLayoutId, int newTasksTitleId,
			int tasksListFooterId) {
		setContentView(activityLayoutId);
		tasksListView = getListView();
		tasksListFooter = (TextView) findViewById(tasksListFooterId);
		newTaskTitle = (EditText) findViewById(newTasksTitleId);
		newTaskTitle.setInputType(InputType.TYPE_CLASS_TEXT);

		this.model = new Model(this);
		getModel().activate();
		getModel().addModelListener(this);
		fillData();

		newTaskTitle.setOnKeyListener(new AddNewTaskOnKeyListener());
	}

	protected TextView getTasksListFooter() {
		return tasksListFooter;
	}

	protected Model getModel() {
		return model;
	}

	protected ListView getTasksListView() {
		return tasksListView;
	}

	protected Handler getUiHandler() {
		return uiHandler;
	}

	protected abstract void fillData();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.today_activity, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	};

	public void taskCreated(Task task) {
		getUiHandler().post(new Runnable() {
			public void run() {
				fillData();
			}
		});
	}

	public void taskDeleted(Task task) {
		getUiHandler().post(new Runnable() {
			public void run() {
				fillData();
			}
		});
	};

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Toast.makeText(this, "Item click", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(TasksListActivity.this,
				EditTaskActivity.class);
		intent.putExtra(KEY_TASKID, ((Task) tasksListView
				.getItemAtPosition(position)).getId().toString());
		startActivityForResult(intent, ACTIVITY_EDIT);
	}

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 24.07.2012
	 */
	protected class AddNewTaskOnKeyListener implements OnKeyListener {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if ((event.getAction() == KeyEvent.ACTION_DOWN)
					&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
				EditText editText = (EditText) v;
				if (editText.getText().toString().length() == 0) {
					return false;
				}
				getModel().createNewTask(editText.getText().toString());
				editText.getText().clear();

				return true;
			}
			return false;
		}
	}

	public void taskChanged(Task oldTask, Task newTask) {
		getUiHandler().post(new Runnable() {
			public void run() {
				fillData();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		fillData();
	};

	
}
