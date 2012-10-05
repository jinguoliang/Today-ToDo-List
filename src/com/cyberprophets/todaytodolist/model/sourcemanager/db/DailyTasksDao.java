package com.cyberprophets.todaytodolist.model.sourcemanager.db;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.DailyTask;

/**
 * �����, ������������ ������ � ����������� �������� � ��
 * 
 * @author Mironov S.V.
 * @since 29.08.2012
 */
class DailyTasksDao extends AbstractDao<DailyTask> {
	public static final String DAILY_TASKS_TABLE = "Daily_Tasks";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_DATE = "date";
	// public static final String KEY_ORDER = "order";
	public static final String KEY_DONE = "done";

	public static final String CREATE_DAILY_TASKS_TABLE_SCRIPT = "create table "
			+ DAILY_TASKS_TABLE
			+ " ("
			+ KEY_ID
			+ " text primary key, "
			+ KEY_TITLE
			+ " text not null, "
			+ KEY_DESCRIPTION
			+ " text, "
			+ KEY_DATE + " text not null, " + KEY_DONE + " integer not null );";

	public DailyTasksDao() {
	}

	@Override
	protected String getObjectTableName() {
		return DAILY_TASKS_TABLE;
	}

	@Override
	protected String[] getObjectTableColumns() {
		return new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE,
				KEY_DONE };
	}

	@Override
	protected ContentValues getContentValues(DailyTask object) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_ID, object.getId().toString());
		contentValues.put(KEY_TITLE, object.getTitle());
		contentValues.put(KEY_DESCRIPTION, object.getDescription());
		contentValues.put(KEY_DATE, object.getDate().toString());
		contentValues.put(KEY_DONE, object.isCompleted() ? 1 : 0);

		return contentValues;
	}

	@Override
	protected DailyTask getObjectFromCursor(Cursor cursor)
			throws IllegalArgumentException {
		if (cursor == null || cursor.isClosed()) {
			throw new IllegalArgumentException(
					"Incorrect cursor for getting DailyTask!");
		}
		if (cursor.getCount() == 0) {
			return null;
		}
		String uuid = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID));
		String title = cursor
				.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
		String description = cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_DESCRIPTION));
		boolean completed = cursor.getInt(cursor
				.getColumnIndexOrThrow(KEY_DONE)) == 1 ? true : false;
		String date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE));
		try {
			return new DailyTask(UUID.fromString(uuid), title, description,
					completed, Date.valueOf(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<DailyTask> getTasksByDate(Date date)
			throws IllegalArgumentException {
		if (date == null) {
			throw new IllegalArgumentException(
					"Incorrect date for getting DailyTasks by date");
		}

		Cursor cursor = getDatabase().query(getObjectTableName(),
				getObjectTableColumns(),
				KEY_DATE + "=\'" + date.toString() + "\'", null, null, null,
				null);
		List<DailyTask> dailyTasks = getObjectsFromCursor(cursor);
		cursor.close();
		return dailyTasks;
	}

	public static void createTables(SQLiteDatabase database) {
		database.execSQL(CREATE_DAILY_TASKS_TABLE_SCRIPT);
	}

	public static void dropTables(SQLiteDatabase database) {
		database.execSQL("drop table " + DAILY_TASKS_TABLE + " ;");
	}

}
