package com.cyberprophets.todaytodolist.model;

import com.cyberprophets.todaytodolist.model.task.Category;
import com.cyberprophets.todaytodolist.model.task.Task;

/**
 * 
 * @author Mironov S.V.
 * @since 24.07.2012
 */
public interface ModelListener {
	void taskCreated(Task task);

	void taskDeleted(Task task);

	void taskChanged(Task oldTask, Task newTask);

	void categoryCreated(Category category);

	void categoryChanged(Category oldCategory, Category newCategory);

	void categoryDeleted(Category category);
}
