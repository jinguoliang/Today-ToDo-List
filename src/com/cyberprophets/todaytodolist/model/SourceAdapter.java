package com.cyberprophets.todaytodolist.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 
 * @author Mironov S.V.
 * @since 18.07.2009
 */
public interface SourceAdapter {

	public SourceAdapter open();

	public void close();

	public List<Task> getAllTasks();

	public boolean saveTask(Task task);

	public boolean deleteTask(Task task);

	public boolean deleteTask(UUID id);

	public Task getTask(UUID id);

	public List<Task> getTasksByDate(Date date);

	public List<Task> getNotCompleteTasks();
}
