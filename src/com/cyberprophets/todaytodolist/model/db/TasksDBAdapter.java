package com.cyberprophets.todaytodolist.model.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberprophets.todaytodolist.model.task.Task;

/**
 * Класс, организующий работу с данными задач в БД.
 * 
 * @author Mironov S.V.
 * @since 29.08.2012
 */
class TasksDBAdapter {
	private static final String TASKS_TABLE = "Tasks";
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_DATE = "date";
	private static final String KEY_DONE = "done";

	private static final String SIMPLE_DATE_FROMAT = "yyyy-MM-dd";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final String CREATE_TASKS_TABLE_SCRIPT = "create table "
			+ TASKS_TABLE + " (" + KEY_ID + " text primary key, " + KEY_TITLE
			+ " text not null, " + KEY_DESCRIPTION + " text, " + KEY_DATE
			+ " text not null, " + KEY_DONE + " integer not null);";

	private SQLiteDatabase database;

	public TasksDBAdapter() {
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * Сохранить задачу в БД
	 * 
	 * @param task
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean saveTask(Task task) throws IllegalArgumentException {
		if (task == null || task.getId() == null) {
			throw new IllegalArgumentException(
					"Incorrect task for saving in DB");
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

		// Task not exists in DB yet
		if (getTask(task.getId()) == null) {
			return getDatabase().insert(TASKS_TABLE, null, contentValues) != -1;

		}
		// Tasks already exists in DB
		else {
			return getDatabase().update(TASKS_TABLE, contentValues,
					KEY_ID + "=" + "\'" + task.getId() + "\'", null) > 0;
		}
	}

	public boolean deleteTask(Task task) throws IllegalArgumentException {
		if (task == null || task.getId() == null) {
			throw new IllegalArgumentException(
					"Illegal argument for deleting task");
		}
		return deleteTask(task.getId());
	}

	/**
	 * Удалить задачу из БД, по id
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean deleteTask(UUID id) throws IllegalArgumentException {
		if (id == null) {
			throw new IllegalArgumentException(
					"Illegal argument for deleting task");
		}
		return getDatabase().delete(TASKS_TABLE,
				KEY_ID + "=" + "\'" + id.toString() + "\'", null) > 0;
	}

	/**
	 * Получить задачу из БД по id
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Task getTask(UUID id) throws IllegalArgumentException {
		if (id == null) {
			throw new IllegalArgumentException(
					"Illegal argument for getting task");
		}
		Cursor cursor = getDatabase().query(
				true,
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE }, KEY_ID + "=" + "\'" + id.toString() + "\'",
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			Task task = getTaskFromCursor(cursor);
			cursor.close();
			return task;
		}
		return null;
	}

	/**
	 * Получить все задачи, хранящиеся в БД
	 * 
	 * @return
	 */
	public List<Task> getAllTasks() {
		Cursor cursor = getDatabase().query(
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE }, null, null, null, null, null);
		List<Task> tasks = getTasksFromCursor(cursor);
		cursor.close();
		return tasks;
	}

	/**
	 * На выбор задач оказывает влияние только год, месяц и день.
	 */
	public List<Task> getTasksByDate(java.util.Date date)
			throws IllegalArgumentException {
		if (date == null) {
			throw new IllegalArgumentException(
					"Incorrect date for getting tasks by date");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				SIMPLE_DATE_FROMAT);
		String tmp = simpleDateFormat.format(date);

		Cursor cursor = getDatabase().query(
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE }, KEY_DATE + " like '%" + tmp + "%'", null,
				null, null, null);
		List<Task> tasks = getTasksFromCursor(cursor);
		cursor.close();
		return tasks;
	}

	/**
	 * Получить все незавершенные задачи
	 * 
	 * @return
	 */
	public List<Task> getNotCompleteTasks() {
		Cursor cursor = getDatabase().query(
				TASKS_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE }, KEY_DONE + "=" + "\'0\'", null, null, null,
				null);
		List<Task> tasks = getTasksFromCursor(cursor);
		cursor.close();
		return tasks;
	}

	/**
	 * Получение данных задачи из курсора
	 * 
	 * @param cursor
	 * @return считанная из текущей позиции курсора задача
	 */
	private Task getTaskFromCursor(Cursor cursor)
			throws IllegalArgumentException {
		if (cursor == null || cursor.isClosed()) {
			throw new IllegalArgumentException(
					"Incorrect cursor for getting task!");
		}
		if (cursor.getCount() == 0) {
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
		try {
			return new Task(UUID.fromString(uuid), title, description,
					dateFormat.parse(date), done);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Получение списка задач из курсора
	 * 
	 * @param cursor
	 * @return считанный из курсора список задач
	 */
	private List<Task> getTasksFromCursor(Cursor cursor)
			throws IllegalArgumentException {
		List<Task> tasks = new LinkedList<Task>();
		if (cursor == null || cursor.isClosed()) {
			throw new IllegalArgumentException(
					"Incorrect cursor for getting tasks!");
		}
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); ++i) {
			tasks.add(getTaskFromCursor(cursor));
			cursor.moveToNext();
		}
		return tasks;
	}

	public static void createTables(SQLiteDatabase database) {
		database.execSQL(CREATE_TASKS_TABLE_SCRIPT);
	}

	public static void dropTables(SQLiteDatabase database) {
		database.execSQL("drop table " + TASKS_TABLE + " ;");
	}

}
