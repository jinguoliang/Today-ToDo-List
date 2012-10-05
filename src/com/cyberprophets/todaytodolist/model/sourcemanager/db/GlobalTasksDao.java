package com.cyberprophets.todaytodolist.model.sourcemanager.db;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberprophets.todaytodolist.model.dataobjects.tasks.GlobalTask;

/**
 * 
 * @author Mironov S.V.
 * @since 31.08.2012
 */
public class GlobalTasksDao extends AbstractDao<GlobalTask> {
	public static final String GLOBAL_TASKS_TABLE = "Global_Tasks";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_DONE = "done";
	public static final String KEY_PARENT_ID = "parent_id";

	public static final String CREATE_GLOBAL_TASKS_TABLE_SCRIPT = "create table "
			+ GLOBAL_TASKS_TABLE
			+ " ("
			+ KEY_ID
			+ " text primary key, "
			+ KEY_TITLE
			+ " text not null, "
			+ KEY_DESCRIPTION
			+ " text, "
			+ KEY_PARENT_ID + " text, " + KEY_DONE + " integer not null);";

	@Override
	protected String getObjectTableName() {
		return GLOBAL_TASKS_TABLE;
	}

	@Override
	protected String[] getObjectTableColumns() {
		return new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DONE,
				KEY_PARENT_ID };
	}

	@Override
	protected ContentValues getContentValues(GlobalTask object) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_ID, object.getId().toString());
		contentValues.put(KEY_TITLE, object.getTitle());
		contentValues.put(KEY_DESCRIPTION, object.getDescription());
		contentValues.put(KEY_DONE, object.isCompleted() ? 1 : 0);

		return contentValues;
	}

	@Override
	protected GlobalTask getObjectFromCursor(Cursor cursor)
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
		return new GlobalTask(UUID.fromString(uuid), title, description,
				completed);
	}

	@Override
	public boolean save(GlobalTask object) throws IllegalArgumentException {
		boolean result = super.save(object);
		if (!result) {
			return false;
		}
		for (GlobalTask subTask : object.getSubTasks()) {
			result = save(subTask);
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_PARENT_ID, object.getId().toString());
			result = getDatabase().update(getObjectTableName(), contentValues,
					KEY_ID + "=" + "\'" + subTask.getId() + "\'", null) > 0;
			if (!result) {
				return false;
			}
		}
		return true;
	}

	protected List<GlobalTask> getSubTasks(UUID parentTaskID)
			throws IllegalArgumentException {
		if (parentTaskID == null) {
			throw new IllegalArgumentException(
					"Incorrect argument for getting subtasks from DB");
		}
		Cursor cursor = getDatabase().query(getObjectTableName(),
				getObjectTableColumns(),
				KEY_PARENT_ID + "=" + "\'" + parentTaskID.toString() + "\'",
				null, null, null, null);

		List<GlobalTask> tasks = new LinkedList<GlobalTask>();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); ++i, cursor.moveToNext()) {
				GlobalTask task = getObjectFromCursor(cursor);
				task.setSubTasks(getSubTasks(task.getId()));
				tasks.add(task);
			}
		}

		List<GlobalTask> result = getObjectsFromCursor(cursor);
		cursor.close();
		return result;
	}

	@Override
	public GlobalTask get(UUID id) throws IllegalArgumentException {
		GlobalTask task = super.get(id);
		if (task != null) {
			task.setSubTasks(getSubTasks(id));
		}
		return task;
	}

	@Override
	public List<GlobalTask> getAll() {
		Cursor cursor = getDatabase().query(getObjectTableName(),
				getObjectTableColumns(), KEY_PARENT_ID + " is null", null,
				null, null, null);
		List<GlobalTask> tasks = getObjectsFromCursor(cursor);
		cursor.close();
		for (GlobalTask task : tasks) {
			task.setSubTasks(getSubTasks(task.getId()));
		}
		return tasks;
	}

	@Override
	public boolean delete(UUID objectID) throws IllegalArgumentException {
		Cursor cursor = getDatabase().query(getObjectTableName(),
				new String[] { KEY_ID, KEY_PARENT_ID },
				KEY_PARENT_ID + "=" + "\'" + objectID.toString() + "\'", null,
				null, null, null);
		// ������� ��� ���������
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); ++i, cursor.moveToNext()) {
				String uuid = cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_ID));
				delete(UUID.fromString(uuid));
			}
		}
		// ������� ���� ������
		boolean result = super.delete(objectID);
		cursor.close();
		return result;
	}

	public static void createTables(SQLiteDatabase database) {
		database.execSQL(CREATE_GLOBAL_TASKS_TABLE_SCRIPT);
	}

	public static void dropTables(SQLiteDatabase database) {
		database.execSQL("drop table " + GLOBAL_TASKS_TABLE + " ;");
	}

}
