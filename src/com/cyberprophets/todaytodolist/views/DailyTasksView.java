package com.cyberprophets.todaytodolist.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.adapters.BaseTasksAdapter;
import com.cyberprophets.todaytodolist.adapters.DailyTasksAdapter;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.ModelListener;
import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;

/**
 * 
 * @author Mironov S.V.
 * @since 09.10.2012
 */
public class DailyTasksView extends LinearLayout implements ModelListener {
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	private final Model model;
	private final Calendar calendar;

	private final ListView tasksListView;
	private final EditText newTaskTitleEditText;
	private final TextView footerTextView;
	private final TextView dateTextView;

	private final TextView listEmptyTextView;

	private final ImageView nextDateButton;
	private final ImageView previousDateButton;

	public DailyTasksView(Activity context, java.util.Date date) {
		super(context);
		View view = inflate(getContext(), R.layout.daily_tasks_view_layout,
				null);
		addView(view);

		previousDateButton = (ImageView) findViewById(R.id.previous_date_button);
		nextDateButton = (ImageView) findViewById(R.id.next_date_button);
		tasksListView = (ListView) findViewById(android.R.id.list);
		tasksListView.setOnItemClickListener(new OnTaskItemClickListener());
		listEmptyTextView = (TextView) findViewById(android.R.id.empty);
		dateTextView = (TextView) findViewById(R.id.selected_date);
		footerTextView = (TextView) findViewById(R.id.tasks_list_footer);
		newTaskTitleEditText = (EditText) findViewById(R.id.add_task);
		newTaskTitleEditText.setInputType(InputType.TYPE_CLASS_TEXT);

		// date.setTime(new Date());

		this.model = new Model(getContext());
		getModel().activate();
		calendar = Calendar.getInstance();
		if (date == null) {
			date = new java.util.Date();
		}
		setDate(date);

		newTaskTitleEditText.setOnKeyListener(new AddNewTaskOnKeyListener());
		// ((Activity) getContext()).registerForContextMenu(tasksListView);

		previousDateButton
				.setOnClickListener(new PreviousDateButtonOnClickListener());
		nextDateButton.setOnClickListener(new NextDateButtonOnClickListener());
		getModel().addModelListener(this);
	}

	private void setFooterText(String text) {
		footerTextView.setText(text);
	}

	private void outputDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String text = simpleDateFormat.format(getCalendar().getTime());
		dateTextView.setText(text);
	}

	private void setDate(java.util.Date date) {
		getCalendar().setTime(date);
		fillData();
	}

	private java.util.Date getDate() {
		return getCalendar().getTime();
	}

	private Model getModel() {
		return model;
	}

	private Calendar getCalendar() {
		return calendar;
	}

	private ListView getTasksListView() {
		return tasksListView;
	}

	private ListAdapter getListAdapter() {
		return getTasksListView().getAdapter();
	}

	private void setListAdapter(ListAdapter listAdapter) {
		getTasksListView().setAdapter(listAdapter);
	}

	private void switchTaskListVisibility() {
		ListAdapter adapter = tasksListView.getAdapter();
		if (adapter == null || adapter.getCount() == 0) {
			tasksListView.setVisibility(GONE);
			listEmptyTextView.setVisibility(VISIBLE);
		} else {
			tasksListView.setVisibility(VISIBLE);
			listEmptyTextView.setVisibility(GONE);
		}
	}

	public void fillData() {
		if (getListAdapter() == null) {
			setListAdapter(new DailyTasksAdapter(getModel(),
					(Activity) getContext(), null));
		}
		DailyTasksAdapter adapter = (DailyTasksAdapter) getListAdapter();
		adapter.setDate(Date.valueOf(getDate()));
		switchTaskListVisibility();
		outputFooterData();
		outputDate();
	}

	private void outputFooterData() {
		DailyTasksAdapter adapter = (DailyTasksAdapter) getListAdapter();
		int doneTasksCount = 0;
		for (Task task : adapter.getTasks()) {
			if (task.isCompleted()) {
				doneTasksCount++;
			}
		}
		String text = getContext().getString(R.string.active_tasks) + ": "
				+ (adapter.getTasks().size() - doneTasksCount) + "; "
				+ getContext().getString(R.string.complete_tasks) + ": "
				+ doneTasksCount;
		setFooterText(text);
	}

	public void taskCreated(Task task) {
		// fillData();
		outputFooterData();
		switchTaskListVisibility();
	}

	public void taskDeleted(Task task) {
		// fillData();
		outputFooterData();
		switchTaskListVisibility();
	}

	public void taskChanged(Task oldTask, Task newTask) {
		outputFooterData();
	}

	public void taskSaved(Task newTask) {
		outputFooterData();
	}

	private java.util.Date getNextDate() {
		calendar.setTime(getDate());
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		return calendar.getTime();
	}

	private java.util.Date getPreviousDate() {
		calendar.setTime(getDate());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 09.10.2012
	 */
	private class OnTaskItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			((BaseTasksAdapter) getListAdapter()).toogleTask(position);
		}
	}

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 09.10.2012
	 */
	private class AddNewTaskOnKeyListener implements OnKeyListener {
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

	private class PreviousDateButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			Animation animation = android.view.animation.AnimationUtils
					.loadAnimation(getContext(), R.anim.push_left_out);
			startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					setDate(getPreviousDate());
					Animation inAnimation = android.view.animation.AnimationUtils
							.loadAnimation(getContext(), R.anim.push_right_in);
					startAnimation(inAnimation);
				}
			});
		}
	}

	private class NextDateButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			Animation outAnimation = android.view.animation.AnimationUtils
					.loadAnimation(getContext(), R.anim.push_right_out);
			startAnimation(outAnimation);
			outAnimation.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					setDate(getNextDate());
					Animation inAnimation = android.view.animation.AnimationUtils
							.loadAnimation(getContext(), R.anim.push_left_in);
					startAnimation(inAnimation);
				}
			});
		}
	}

}
