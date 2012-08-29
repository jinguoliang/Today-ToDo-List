package com.cyberprophets.todaytodolist.activities;

import java.util.Calendar;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.adapters.TasksAdapter;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.task.Task;
import com.cyberprophets.todaytodolist.views.TaskView;

/**
 * ����������� ����� �� ����������� ������ �����
 * 
 * @author Mironov S.V.
 * @since 01.08.2012
 */
public abstract class TasksListActivity extends ListActivity {
	private EditText newTaskTitle;
	private TextView tasksListFooter;
	private Model model;
	private Calendar date;
	private final Handler uiHandler = new Handler();

	protected abstract void fillData();

	public Calendar getDate() {
		return date;
	}

	protected Model getModel() {
		return model;
	}

	protected TextView getTasksListFooter() {
		return tasksListFooter;
	}

	protected Handler getUiHandler() {
		return uiHandler;
	}

	protected void init(int newTasksTitleId, int tasksListFooterId) {
		tasksListFooter = (TextView) findViewById(tasksListFooterId);
		newTaskTitle = (EditText) findViewById(newTasksTitleId);
		newTaskTitle.setInputType(InputType.TYPE_CLASS_TEXT);

		date = Calendar.getInstance();
		date.setTime(new Date());

		this.model = new Model(this);
		getModel().activate();
		fillData();

		newTaskTitle.setOnKeyListener(new AddNewTaskOnKeyListener());
		registerForContextMenu(getListView());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater()
				.inflate(R.menu.tasks_list_activity_context_menu, menu);
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {
		((TasksAdapter) getListAdapter()).toogleTask(position);
		// getUiHandler().post(new Runnable() {
		// public void run() {
		// Toast.makeText(TasksListActivity.this, "Item click",
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		// Intent intent = new Intent(TasksListActivity.this,
		// EditTaskActivity.class);
		// intent.putExtra(KEY_TASKID, ((Task) tasksListView
		// .getItemAtPosition(position)).getId().toString());
		// startActivityForResult(intent, ACTIVITY_EDIT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fillData();
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.context_menu_delete_task:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Task task = ((TaskView) info.targetView).getTask();
			getModel().deleteTask(task);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * ��������� ������� �������� "����" ��� ���������� ������.
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
				getModel().createNewTask(editText.getText().toString(),
						getDate().getTime());
				editText.getText().clear();

				return true;
			}
			return false;
		}
	}

}
