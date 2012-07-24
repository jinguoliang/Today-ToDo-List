package com.cyberprophets.todaytodolist.model;

/**
 * 
 * @author Mironov S.V.
 * @since 24.07.2012
 */
public interface ModelListener {
	void taskCreated(Task task);

	void taskDeleted(Task task);
}
