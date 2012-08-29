package com.cyberprophets.todaytodolist.model.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberprophets.todaytodolist.model.task.Category;

/**
 * Класс, организующий работу с данными категорий задач в БД.
 * 
 * @author Mironov S.V.
 * @since 29.08.2012
 */
class CategoriesDBAdapter {
	private static final String CATEGORY_TABLE = "Category";
	private static final String KEY_ID = "category_id";
	private static final String KEY_NAME = "name";

	private static final String CREATE_CATEGORY_TABLE_SCRIPT = "create table "
			+ CATEGORY_TABLE + " (" + KEY_ID + " text primary key, " + KEY_NAME
			+ " text not null, unique ( " + KEY_NAME + "));";

	private SQLiteDatabase database;
	private final TasksDBAdapter tasksDBAdapter = new TasksDBAdapter();

	public CategoriesDBAdapter() {
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	private TasksDBAdapter getTasksDBAdapter() {
		return tasksDBAdapter;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
		getTasksDBAdapter().setDatabase(database);
	}

	public Category getCategory(UUID id) throws IllegalArgumentException {
		if (id == null) {
			throw new IllegalArgumentException(
					"Illegal id for getting category from DB");
		}
		Cursor cursor = getDatabase().query(true, CATEGORY_TABLE,
				new String[] { KEY_ID, KEY_NAME },
				KEY_ID + "=" + "\'" + id.toString() + "\'", null, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			Category category = getCategoryFromCursor(cursor);
			cursor.close();
			return category;
		}

		return null;
	}

	public Category getCategory(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException(
					"Illegal name for getting category from DB");
		}
		Cursor cursor = getDatabase().query(true, CATEGORY_TABLE,
				new String[] { KEY_ID, KEY_NAME },
				KEY_NAME + "=" + "\'" + name + "\'", null, null, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			Category category = getCategoryFromCursor(cursor);
			cursor.close();
			return category;
		}
		return null;
	}

	public boolean saveCategory(Category category)
			throws IllegalArgumentException {
		if (category == null || category.getId() == null) {
			throw new IllegalArgumentException(
					"Illegal argument for saving category in DB");
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_ID, category.getId().toString());
		contentValues.put(KEY_NAME, category.getName());

		// Category not exists in DB yet
		if (getCategory(category.getId()) == null) {
			// TODO Добавить проверку существования категоии с таким именем
			return getDatabase().insert(CATEGORY_TABLE, null, contentValues) != -1;
		}
		// Category already exists in DB
		else {
			return getDatabase().update(CATEGORY_TABLE, contentValues,
					KEY_ID + "=" + "\'" + category.getId() + "\'", null) > 0;
		}
	}

	public boolean deleteCategory(Category category)
			throws IllegalArgumentException {
		if (category == null || category.getId() == null) {
			throw new IllegalArgumentException(
					"Incorrect argument for deleting category from DB");
		}
		// List<Task> tasks = getTasksForCategory(category);
		// for (Task task : tasks) {
		// deleteTask(task);
		// }
		return deleteCategory(category.getId());
	}

	public boolean deleteCategory(UUID id) throws IllegalArgumentException {
		if (id == null) {
			throw new IllegalArgumentException(
					"Incorrect argument for deleting category from DB");
		}
		return getDatabase().delete(CATEGORY_TABLE,
				KEY_ID + "=" + "\'" + id.toString() + "\'", null) > 0;
	}

	public List<Category> getAllCategories() {
		Cursor cursor = getDatabase()
				.query(CATEGORY_TABLE, new String[] { KEY_ID, KEY_NAME }, null,
						null, null, null, null);
		List<Category> categories = getCategoriesFromCursor(cursor);
		cursor.close();

		// TODO реализовать добавление задач относящихся к категории
		// for (Category category : categories) {
		// category.setTasks(getTasksForCategory(category));
		// }

		return categories;
	}

	private Category getCategoryFromCursor(Cursor cursor)
			throws IllegalArgumentException {
		if (cursor == null || cursor.isClosed()) {
			throw new IllegalArgumentException(
					"Illegal cursor for getting category");
		}
		String uuid = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID));
		String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));

		Category category = new Category(UUID.fromString(uuid), name);
		return category;
	}

	private List<Category> getCategoriesFromCursor(Cursor cursor)
			throws IllegalArgumentException {
		List<Category> categories = new ArrayList<Category>();
		if (cursor == null || cursor.isClosed()) {
			throw new IllegalArgumentException(
					"Illegal cursor for getting categories");
		}
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); ++i) {
			categories.add(getCategoryFromCursor(cursor));
			cursor.moveToNext();
		}
		return categories;
	}

	// public List<Task> getTasksForCategory(Category category) {
	// if (category == null) {
	// throw new NullPointerException();
	// }
	// Cursor cursor = getDatabase().query(
	// TASKS_TABLE,
	// new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
	// KEY_DONE, KEY_CATEGORY_ID },
	// KEY_CATEGORY_ID + "=" + "\'" + category.getId().toString()
	// + "\'", null, null, null, KEY_TITLE);
	// List<Task> tasks = getTasksFromCursor(cursor);
	// cursor.close();
	// return tasks;
	// }

	public static void createTables(SQLiteDatabase database) {
		database.execSQL(CREATE_CATEGORY_TABLE_SCRIPT);
	}

	public static void dropTables(SQLiteDatabase database) {
		database.execSQL("drop table " + CATEGORY_TABLE + " ;");
	}

}
