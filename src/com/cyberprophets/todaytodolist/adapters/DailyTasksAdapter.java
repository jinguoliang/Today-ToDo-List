package com.cyberprophets.todaytodolist.adapters;

import java.util.List;

import android.app.Activity;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.DailyTask;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;

/**
 * ������� ��� ����������� ����� �� ���������� ���� � ListView
 * 
 * @author Mironov S.V.
 * @since 16.08.2012
 */
public class DailyTasksAdapter extends BaseTasksAdapter {
	private Date date;
	private List<DailyTask> tasks;

	public DailyTasksAdapter(Model model, Activity context, Date date) {
		super(model, context);
		setDate(date);
	}

	public int getCount() {
		return getTasks().size();
	}

	public Date getDate() {
		return date;
	}

	public Object getItem(int position) {
		return getTasks().get(position);
	}

	public List<DailyTask> getTasks() {
		return tasks;
	}

	private List<DailyTask> getTasksFromModel(Date date) {
		return getModel().getDailyTasks(date);
	}

	public void setDate(Date date) {
		this.date = date;
		tasks = getTasksFromModel(date);
		init();
		notifyDataSetChanged();
	}

	@Override
	public void taskChanged(Task oldTask, Task newTask) {
		tasks = getTasksFromModel(getDate());
		super.taskChanged(oldTask, newTask);
	}

	@Override
	public void taskCreated(Task task) {
		tasks = getTasksFromModel(getDate());
		super.taskCreated(task);
	}

	@Override
	public void taskDeleted(Task task) {
		tasks = getTasksFromModel(getDate());
		super.taskCreated(task);
	}

	public void taskSaved(Task newTask) {
		// TODO Auto-generated method stub

	}

}
