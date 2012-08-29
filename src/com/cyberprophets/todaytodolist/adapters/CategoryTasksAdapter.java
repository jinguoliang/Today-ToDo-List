package com.cyberprophets.todaytodolist.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.ModelListener;
import com.cyberprophets.todaytodolist.model.task.Category;
import com.cyberprophets.todaytodolist.model.task.Task;
import com.cyberprophets.todaytodolist.views.CategoryView;
import com.cyberprophets.todaytodolist.views.TaskView;

/**
 * Адаптер для отображения задач по категориям
 * 
 * @author Mironov S.V.
 * @since 20.08.2012
 */
public class CategoryTasksAdapter extends BaseExpandableListAdapter implements
		ModelListener {
	private final Activity context;
	private final Model model;
	private List<Category> categories;

	public CategoryTasksAdapter(Model model, Activity context) {
		super();
		this.model = model;
		this.context = context;
		getModel().addModelListener(this);
		init();
		categories = getCategoriesFromModel();
	}

	private Model getModel() {
		return model;
	}

	protected void init() {
	}

	private Activity getContext() {
		return context;
	}

	private List<Category> getCategories() {
		return categories;
	}

	public void taskCreated(Task task) {
		notifyDataSetChanged();
	}

	public void taskChanged(Task oldTask, Task newTask) {
		notifyDataSetChanged();
	}

	public void taskDeleted(Task task) {
		notifyDataSetChanged();
	}

	public List<Category> getCategoriesFromModel() {
		return getModel().getAllCategories();
	}

	public Object getChild(int arg0, int arg1) {
		return getCategories().get(arg0).getTasks().get(arg1);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * 100 + childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TaskView taskView = null;
		if (convertView == null) {
			Category category = (Category) getGroup(groupPosition);
			Task task = category.getTasks().get(childPosition);
			taskView = new TaskView(getContext(), task, getModel());
		} else {
			taskView = (TaskView) convertView;
			Category category = (Category) getGroup(groupPosition);
			Task task = category.getTasks().get(childPosition);
			taskView.setTask(task);
			// boolean isExpanded = taskView.showNote(getExpandedTasks().get(
			// task.getId()));
			// getExpandedTasks().put(task.getId(), isExpanded);
			taskView.fillView();
		}
		return taskView;
	}

	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	public Object getGroup(int groupPosition) {
		return getCategories().get(groupPosition);
	}

	public int getGroupCount() {
		return getCategories().size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		CategoryView categoryView = null;
		if (convertView == null) {
			categoryView = new CategoryView(getContext(),
					(Category) getGroup(groupPosition), getModel());
			categoryView.fillView();
		} else {
			categoryView = (CategoryView) convertView;
			Category category = (Category) getGroup(groupPosition);
			categoryView.setCategory(category);
			categoryView.fillView();
		}
		return categoryView;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void categoryCreated(Category category) {
		categories = getCategoriesFromModel();
		notifyDataSetChanged();
	}

	public void categoryChanged(Category oldCategory, Category newCategory) {
		categories = getCategoriesFromModel();
		notifyDataSetChanged();
	}

	public void categoryDeleted(Category category) {
		categories = getCategoriesFromModel();
		notifyDataSetChanged();
	}

}
