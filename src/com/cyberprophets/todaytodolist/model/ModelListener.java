package com.cyberprophets.todaytodolist.model;

import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;

/**
 * Интерфейс слушателей модели менеджера задач
 * 
 * @author Mironov S.V.
 * @since 24.07.2012
 */
public interface ModelListener {
	void taskCreated(Task task);

	void taskDeleted(Task task);

	void taskSaved(Task newTask);
}
