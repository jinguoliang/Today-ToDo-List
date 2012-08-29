package com.cyberprophets.todaytodolist.model.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyberprophets.todaytodolist.model.SourceAdapter;
import com.cyberprophets.todaytodolist.model.task.Category;
import com.cyberprophets.todaytodolist.model.task.Task;

/**
 * Класс реализующий работу с БД
 * 
 * @author Mironov S.V.
 * @since 18.07.2012
 */
public class DatabaseAdapter implements SourceAdapter {
	private static final String DATABASE_NAME = "Data";
	private static final int DATABASE_VERSION = 7;

	private DatabaseOpenHelper databaseOpenHelper;

	private final Context context;
	private SQLiteDatabase database;
	private final CategoriesDBAdapter categoriesDBAdapter = new CategoriesDBAdapter();
	private final TasksDBAdapter tasksDBAdapter = new TasksDBAdapter();

	public DatabaseAdapter(Context context) {
		this.context = context;
	}

	public DatabaseAdapter open() {
		databaseOpenHelper = new DatabaseOpenHelper(getContext());
		database = getDatabaseOpenHelper().getWritableDatabase();
		getCategoriesDBAdapter().setDatabase(database);
		getTasksDBAdapter().setDatabase(database);
		return this;
	}

	private CategoriesDBAdapter getCategoriesDBAdapter() {
		return categoriesDBAdapter;
	}

	private TasksDBAdapter getTasksDBAdapter() {
		return tasksDBAdapter;
	}

	public void close() {
		getDatabaseOpenHelper().close();
		database.close();
	}

	private DatabaseOpenHelper getDatabaseOpenHelper() {
		return databaseOpenHelper;
	}

	private SQLiteDatabase getDatabase() {
		return database;
	}

	private Context getContext() {
		return context;
	}

	public List<Task> getAllTasks() {
		return getTasksDBAdapter().getAllTasks();
	}

	public boolean saveTask(Task task) {
		return getTasksDBAdapter().saveTask(task);
	}

	public boolean deleteTask(Task task) {
		return getTasksDBAdapter().deleteTask(task);
	}

	public boolean deleteTask(UUID id) {
		return getTasksDBAdapter().deleteTask(id);
	}

	public Task getTask(UUID id) {
		return getTasksDBAdapter().getTask(id);
	}

	public List<Task> getTasksByDate(Date date) {
		return getTasksDBAdapter().getTasksByDate(date);
	}

	public List<Task> getNotCompleteTasks() {
		return getTasksDBAdapter().getNotCompleteTasks();
	}

	public List<Category> getAllCategories() {
		return getCategoriesDBAdapter().getAllCategories();
	}

	public Category getCategory(UUID id) {
		return getCategoriesDBAdapter().getCategory(id);
	}

	public Category getCategory(String name) {
		return getCategoriesDBAdapter().getCategory(name);
	}

	public List<Task> getTasksForCategory(Category category) {
		// TODO
		return null;
	}

	public boolean saveCategory(Category category) {
		return getCategoriesDBAdapter().saveCategory(category);
	}

	public boolean deleteCategory(UUID id) {
		return getCategoriesDBAdapter().deleteCategory(id);
	}

	public boolean deleteCategory(Category category) {
		return getCategoriesDBAdapter().deleteCategory(category);
	}

	/**
	 * 
	 * @author Mironov S.V.
	 * @since 18.07.2012
	 */
	private static class DatabaseOpenHelper extends SQLiteOpenHelper {

		public DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			CategoriesDBAdapter.createTables(db);
			TasksDBAdapter.createTables(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			CategoriesDBAdapter.dropTables(db);
			TasksDBAdapter.dropTables(db);
			onCreate(db);
		}
	}

}
