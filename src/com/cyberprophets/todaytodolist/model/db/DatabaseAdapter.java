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
	private static final String DATABASE_TABLE = "tasks";
	private static final int DATABASE_VERSION = 3;

	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_DATE = "date";
	private static final String KEY_DONE = "done";

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase database;
	private final Context context;

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ID + " text primary key, "
			+ KEY_TITLE + " text not null, " + KEY_DESCRIPTION + " text, "
			+ KEY_DATE + " text not null, " + KEY_DONE + " integer not null);";

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
				DATABASE_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE }, null, null, null, null, null);
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

		if (getTask(task.getId()) == null) {
			return getDatabase().insert(DATABASE_TABLE, null, contentValues) != -1;
		} else {
			return getDatabase().update(DATABASE_TABLE, contentValues,
					KEY_ID + "=" + task.getId(), null) > 0;
		}
	}

	public boolean deleteTask(Task task) {
		return deleteTask(task.getId());
	}

	public boolean deleteTask(UUID id) {
		return getDatabase().delete(DATABASE_TABLE,
				KEY_ID + "=" + id.toString(), null) > 0;
	}

	public Task getTask(UUID id) {
		Cursor cursor = getDatabase().query(
				true,
				DATABASE_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE }, KEY_ID + "=" + "\'" + id.toString() + "\'",
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Task task = getTaskFromCursor(cursor);
		cursor.close();
		return task;
	}

	public List<Task> getTasksByDate(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String tmp = simpleDateFormat.format(date);

		Cursor cursor = getDatabase().query(
				DATABASE_TABLE,
				new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
						KEY_DONE }, KEY_DATE + "=" + "\'" + tmp + "\'", null,
				null, null, null);
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

		Task task = null;
		try {
			task = new Task(UUID.fromString(uuid), title, description,
					dateFormat.parse(date), done);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return task;
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
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// db.execSQL("drop table " + DATABASE_TABLE + " ;");
			// db.execSQL(DATABASE_CREATE);
		}

	}

}
