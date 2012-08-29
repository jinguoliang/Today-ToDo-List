package com.cyberprophets.todaytodolist.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cyberprophets.todaytodolist.model.task.Category;
import com.cyberprophets.todaytodolist.model.task.Task;

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

	public List<Category> getAllCategories();

	public Category getCategory(UUID id);

	public Category getCategory(String name);

	public List<Task> getTasksForCategory(Category category);

	public boolean saveCategory(Category category);

	public boolean deleteCategory(UUID id);

	public boolean deleteCategory(Category category);
}
