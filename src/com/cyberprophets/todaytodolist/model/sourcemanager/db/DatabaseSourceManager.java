package com.cyberprophets.todaytodolist.model.sourcemanager.db;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.DailyTask;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.GlobalTask;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;
import com.cyberprophets.todaytodolist.model.sourcemanager.SourceManager;

/**
 * ����� ����������� ������ � ��
 * 
 * @author Mironov S.V.
 * @since 18.07.2012
 */
public class DatabaseSourceManager implements SourceManager {
	private static final String DATABASE_NAME = "Data";
	private static final int DATABASE_VERSION = 13;

	private DatabaseOpenHelper databaseOpenHelper;

	private final Context context;
	private SQLiteDatabase database;
	private final DailyTasksDao dailyTasksDao = new DailyTasksDao();
	private final GlobalTasksDao globalTaskDao = new GlobalTasksDao();

	public DatabaseSourceManager(Context context) {
		this.context = context;
	}

	public DatabaseSourceManager open() {
		databaseOpenHelper = new DatabaseOpenHelper(getContext());
		database = getDatabaseOpenHelper().getWritableDatabase();
		getDailyTasksDao().setDatabase(database);
		getGlobalTaskDao().setDatabase(database);
		return this;
	}

	public void close() {
		getDatabaseOpenHelper().close();
		database.close();
	}

	private DatabaseOpenHelper getDatabaseOpenHelper() {
		return databaseOpenHelper;
	}

	private Context getContext() {
		return context;
	}

	private DailyTasksDao getDailyTasksDao() {
		return dailyTasksDao;
	}

	private GlobalTasksDao getGlobalTaskDao() {
		return globalTaskDao;
	}

	public GlobalTask getGlobalTask(UUID id) {
		return getGlobalTaskDao().get(id);
	}

	public DailyTask getDailyTask(UUID id) {
		return getDailyTasksDao().get(id);
	}

	public List<GlobalTask> getAllGlobalTasks() {
		return getGlobalTaskDao().getAll();
	}

	public List<DailyTask> getDailyTasks(Date date) {
		if (date == null) {
			return new LinkedList<DailyTask>();
		}
		return getDailyTasksDao().getTasksByDate(date);
	}

	public boolean delete(Task task) {
		if (task instanceof GlobalTask) {
			return getGlobalTaskDao().delete(task.getId());
		} else if (task instanceof DailyTask) {
			return getDailyTasksDao().delete(task.getId());
		}
		return false;
	}

	public boolean save(Task task) {
		if (task instanceof GlobalTask) {
			return getGlobalTaskDao().save((GlobalTask) task);
		} else if (task instanceof DailyTask) {
			return getDailyTasksDao().save((DailyTask) task);
		}
		return false;
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
			GlobalTasksDao.createTables(db);
			DailyTasksDao.createTables(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			GlobalTasksDao.dropTables(db);
			DailyTasksDao.dropTables(db);
			onCreate(db);
		}
	}

}
