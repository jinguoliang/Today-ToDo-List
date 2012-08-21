package com.cyberprophets.todaytodolist.model.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyberprophets.todaytodolist.model.Category;
import com.cyberprophets.todaytodolist.model.SourceAdapter;
import com.cyberprophets.todaytodolist.model.Task;

/**
 * Класс реализующий работу с БД
 * 
 * @author Mironov S.V.
 * @since 18.07.2012
 */
public class DatabaseAdapter implements SourceAdapter {
	private static final String DATABASE_NAME = "data";
	private static final String TASKS_TABLE = "tasks";
	private static final String CATEGORY_TABLE = "category";
	private static final int DATABASE_VERSION = 6;

	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_DATE = "date";
	private static final String KEY_DONE = "done";
	private static final String KEY_CATEGORY_ID = "category_id";
	private static final String KEY_NAME = "name";

	private static final String SIMPLE_DATE_FROMAT = "yyyy-MM-dd";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase database;
	private final Context context;

	private static final String CREATE_TASKS_TABLE = "create table "
			+ TASKS_TABLE + " (" + KEY_ID + " text primary key, " + KEY_TITLE
			+ " text not null, " + KEY_DESCRIPTION + " text, " + KEY_DATE
			+ " text not null, " + KEY_DONE + " integer not null, "
			+ KEY_CATEGORY_ID + " text, foreign key (" + KEY_CATEGORY_ID
			+ ") references " + CATEGORY_TABLE + " (" + KEY_ID + "));";

	private static final String CREATE_CATEGORY_TABLE = "create table "
			+ CATEGORY_TABLE + " (" + KEY_ID + " text primary key, " + KEY_NAME
			+ " text not null, unique ( " + KEY_NAME + "));";

	public DatabaseAdapter(Context context) {
		this.context = context;
	}

	public DatabaseAdapter open() {
		databaseOpenHelper = new DatabaseOpenHelper(getContext());
		database = getDatabaseOpenHelper().getWritableDatabase();
		return this;
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
		Cursor cursor = getDatabase().query(
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE, KEY_CATEGORY_ID }, null, null, null, null,
				null);
		List<Task> tasks = getTasksFromCursor(cursor);
		cursor.close();
		return tasks;
	}

	public boolean saveTask(Task task) {
		if (task == null || task.getId() == null) {
			return false;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_ID, task.getId().toString());
		contentValues.put(KEY_TITLE, task.getTitle());
		contentValues.put(KEY_DESCRIPTION, task.getDescription());
		Integer done = task.isDone() ? 1 : 0;
		contentValues.put(KEY_DONE, done);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(task.getDate());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String date = simpleDateFormat.format(task.getDate());
		contentValues.put(KEY_DATE, date);
		String categoryID = task.getCategory() != null ? task.getCategory()
				.getId().toString() : new String();
		contentValues.put(KEY_CATEGORY_ID, categoryID);

		if (getTask(task.getId()) == null) {
			return getDatabase().insert(TASKS_TABLE, null, contentValues) != -1;
		} else {
			return getDatabase().update(TASKS_TABLE, contentValues,
					KEY_ID + "=" + "\'" + task.getId() + "\'", null) > 0;
		}
	}

	public boolean deleteTask(Task task) {
		return deleteTask(task.getId());
	}

	public boolean deleteTask(UUID id) {
		return getDatabase().delete(TASKS_TABLE,
				KEY_ID + "=" + "\'" + id.toString() + "\'", null) > 0;
	}

	public Task getTask(UUID id) {
		Cursor cursor = getDatabase().query(
				true,
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE, KEY_CATEGORY_ID },
				KEY_ID + "=" + "\'" + id.toString() + "\'", null, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			Task task = getTaskFromCursor(cursor);
			cursor.close();
			return task;
		}

		return null;
	}

	/**
	 * На выбор задач оказывает влияние только год, месяц и день.
	 */
	public List<Task> getTasksByDate(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				SIMPLE_DATE_FROMAT);
		String tmp = simpleDateFormat.format(date);

		Cursor cursor = getDatabase().query(
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE, KEY_CATEGORY_ID },
				KEY_DATE + " like '%" + tmp + "%'", null, null, null, null);
		List<Task> tasks = getTasksFromCursor(cursor);
		cursor.close();
		return tasks;
	}

	private List<Task> getTasksFromCursor(Cursor cursor) {
		List<Task> tasks = new ArrayList<Task>();
		if (cursor == null || cursor.isClosed()) {
			return tasks;
		}
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); ++i) {
			tasks.add(getTaskFromCursor(cursor));
			cursor.moveToNext();
		}
		return tasks;
	}

	private Task getTaskFromCursor(Cursor cursor) {
		if (cursor == null || cursor.isClosed() || cursor.isBeforeFirst()) {
			return null;
		}
		String uuid = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID));
		String title = cursor
				.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
		String description = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_DESCRIPTION));
		boolean done = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DONE)) == 1 ? true
				: false;
		String date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE));
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String categoryID = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_CATEGORY_ID));
		Task task = null;
		try {
			if (categoryID.length() != 0) {
				task = new Task(UUID.fromString(uuid), title, description,
						dateFormat.parse(date), done,
						getCategory(UUID.fromString(categoryID)));
			} else {
				task = new Task(UUID.fromString(uuid), title, description,
						dateFormat.parse(date), done, null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return task;
	}

	public List<Task> getNotCompleteTasks() {
		Cursor cursor = getDatabase().query(
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE, KEY_CATEGORY_ID }, KEY_DONE + "=" + "\'0\'",
				null, null, null, null);
		List<Task> tasks = getTasksFromCursor(cursor);
		cursor.close();
		return tasks;
	}

	public List<Category> getAllCategories() {
		Cursor cursor = getDatabase()
				.query(CATEGORY_TABLE, new String[] { KEY_ID, KEY_NAME }, null,
						null, null, null, null);
		List<Category> categories = getCategoriesFromCursor(cursor);
		cursor.close();

		for (Category category : categories) {
			category.setTasks(getTasksForCategory(category));
		}

		return categories;
	}

	public Category getCategory(String name) {
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

	public List<Task> getTasksForCategory(Category category) {
		if (category == null) {
			throw new NullPointerException();
		}
		Cursor cursor = getDatabase().query(
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE, KEY_CATEGORY_ID },
				KEY_CATEGORY_ID + "=" + "\'" + category.getId().toString()
						+ "\'", null, null, null, KEY_TITLE);
		List<Task> tasks = getTasksFromCursor(cursor);
		cursor.close();
		return tasks;
	}

	public boolean saveCategory(Category category) {
		if (category == null || category.getId() == null) {
			return false;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_ID, category.getId().toString());
		contentValues.put(KEY_NAME, category.getName());

		if (getCategory(category.getId()) == null) {
			return getDatabase().insert(CATEGORY_TABLE, null, contentValues) != -1;
		} else {
			return getDatabase().update(CATEGORY_TABLE, contentValues,
					KEY_ID + "=" + "\'" + category.getId() + "\'", null) > 0;
		}
	}

	public boolean deleteCategory(UUID id) {
		return getDatabase().delete(CATEGORY_TABLE,
				KEY_ID + "=" + "\'" + id.toString() + "\'", null) > 0;
	}

	public boolean deleteCategory(Category category) {
		List<Task> tasks = getTasksForCategory(category);
		for (Task task : tasks) {
			deleteTask(task);
		}
		return deleteCategory(category.getId());
	}

	public Category getCategory(UUID id) {
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

	private Category getCategoryFromCursor(Cursor cursor) {
		if (cursor == null || cursor.isClosed() || cursor.isBeforeFirst()) {
			return null;
		}
		String uuid = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID));
		String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));

		Category category = null;
		category = new Category(UUID.fromString(uuid), name);
		return category;
	}

	private List<Category> getCategoriesFromCursor(Cursor cursor) {
		List<Category> categories = new ArrayList<Category>();
		if (cursor == null || cursor.isClosed()) {
			return categories;
		}
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); ++i) {
			categories.add(getCategoryFromCursor(cursor));
			cursor.moveToNext();
		}
		return categories;
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
			db.execSQL(CREATE_TASKS_TABLE);
			db.execSQL(CREATE_CATEGORY_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table " + TASKS_TABLE + " ;");
			db.execSQL("drop table " + CATEGORY_TABLE + " ;");
			onCreate(db);
			// db.execSQL(DATABASE_CREATE);
		}
	}

}
