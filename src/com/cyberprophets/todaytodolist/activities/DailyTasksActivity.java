package com.cyberprophets.todaytodolist.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.adapters.BaseTasksAdapter;
import com.cyberprophets.todaytodolist.adapters.DailyTasksAdapter;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.ModelListener;
import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;
import com.cyberprophets.todaytodolist.views.TaskView;

/**
 * 
 * @author Mironov S.V.
 * @since 01.08.2012
 */
public class DailyTasksActivity extends ListActivity implements ModelListener {
	private EditText newTaskTitle;
	private TextView tasksListFooter;
	private Model model;
	private Calendar calendar;
	private final Handler uiHandler = new Handler();

	private ImageView nextDateButton;
	private ImageView previousDateButton;
	private TextView selectedDateText;

	private static String DATE_FORMAT = "dd.MM.yyyy";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_tasks_activity_layout);

		previousDateButton = (ImageView) findViewById(R.id.previous_date_button);
		nextDateButton = (ImageView) findViewById(R.id.next_date_button);
		selectedDateText = (TextView) findViewById(R.id.selected_date);

		tasksListFooter = (TextView) findViewById(R.id.tasks_list_footer);
		newTaskTitle = (EditText) findViewById(R.id.add_task);
		newTaskTitle.setInputType(InputType.TYPE_CLASS_TEXT);

		calendar = Calendar.getInstance();
		// date.setTime(new Date());

		this.model = new Model(this);
		getModel().activate();
		fillData();

		newTaskTitle.setOnKeyListener(new AddNewTaskOnKeyListener());
		registerForContextMenu(getListView());

		previousDateButton
				.setOnClickListener(new PreviousDateButtonOnClickListener());
		nextDateButton.setOnClickListener(new NextDateButtonOnClickListener());
		getModel().addModelListener(this);
	}

	public Calendar getCalendar() {
		return calendar;
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
		((BaseTasksAdapter) getListAdapter()).toogleTask(position);
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
	 * 
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
				getModel().createNewDailyTask(editText.getText().toString(),
						Date.valueOf(getCalendar().getTime()));
				editText.getText().clear();

				return true;
			}
			return false;
		}
	}

	public ImageView getNextDateButton() {
		return nextDateButton;
	}

	public ImageView getPreviousDateButton() {
		return previousDateButton;
	}

	public TextView getSelectedDateText() {
		return selectedDateText;
	}

	protected void fillData() {
		if (getListAdapter() == null) {
			setListAdapter(new DailyTasksAdapter(getModel(), this, null));
		}
		DailyTasksAdapter adapter = (DailyTasksAdapter) getListAdapter();
		adapter.setDate(Date.valueOf(getCalendar().getTime()));
		outputFooterData();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String date = simpleDateFormat.format(getCalendar().getTime());
		getSelectedDateText().setText(date);
	}

	private class PreviousDateButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			getCalendar().add(Calendar.DAY_OF_MONTH, -1);
			fillData();
		}
	}

	private class NextDateButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			getCalendar().add(Calendar.DAY_OF_MONTH, 1);
			fillData();
		}

	}

	private void outputFooterData() {
		DailyTasksAdapter adapter = (DailyTasksAdapter) getListAdapter();
		int doneTasksCount = 0;
		for (Task task : adapter.getTasks()) {
			if (task.isCompleted()) {
				doneTasksCount++;
			}
		}

		getTasksListFooter().setText(
				getString(R.string.active_tasks) + ": "
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

	public void taskSaved(Task newTask) {
		// TODO Auto-generated method stub
	}
}