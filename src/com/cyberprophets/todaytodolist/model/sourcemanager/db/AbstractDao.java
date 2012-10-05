package com.cyberprophets.todaytodolist.model.sourcemanager.db;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberprophets.todaytodolist.model.dataobjects.DataObject;

/**
 * 
 * @author Mironov S.V.
 * @since 31.08.2012
 * @param <T>
 */
public abstract class AbstractDao<T extends DataObject> {
	public static final String KEY_ID = "id";

	protected SQLiteDatabase database;

	public AbstractDao() {
	}

	protected SQLiteDatabase getDatabase() {
		return database;
	}

	protected void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * ����� ���������� ��� ������� � ������� �������� �������
	 * 
	 * @return
	 */
	protected abstract String getObjectTableName();

	/**
	 * ����� ���������� ��� ����� ������� ������� � ������� �������� �������
	 * 
	 * @return
	 */
	protected abstract String[] getObjectTableColumns();

	/**
	 * ����� ���������� ������ ������� ���������� � �������. ����� �������� �
	 * ContentValues ������ ��������� � ��������� ������� �������, � �������
	 * �������� �������.
	 * 
	 * @param object
	 * @return
	 */
	protected abstract ContentValues getContentValues(T object);

	/**
	 * ��������� ������� �� �������
	 * 
	 * @param cursor
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected abstract T getObjectFromCursor(Cursor cursor)
			throws IllegalArgumentException;

	/**
	 * ��������� �������� �� �������
	 * 
	 * @param cursor
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected List<T> getObjectsFromCursor(Cursor cursor)
			throws IllegalArgumentException {
		List<T> objects = new LinkedList<T>();
		if (cursor == null || cursor.isClosed()) {
			throw new IllegalArgumentException(
					"Incorrect cursor for getting objects from DB!");
		}
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); ++i) {
			objects.add(getObjectFromCursor(cursor));
			cursor.moveToNext();
		}
		return objects;
	}

	/**
	 * �������� ������ �� �� �� id
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException
	 */
	public T get(UUID id) throws IllegalArgumentException {
		if (id == null) {
			throw new IllegalArgumentException(
					"Illegal argument for getting task");
		}

		Cursor cursor = getDatabase().query(true, getObjectTableName(),
				getObjectTableColumns(),
				KEY_ID + "=" + "'" + id.toString() + "'", null, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			T object = getObjectFromCursor(cursor);
			cursor.close();
			return object;
		}
		return null;
	}

	/**
	 * �������� ��� ������, ���������� � ��
	 * 
	 * @return
	 */
	public List<T> getAll() {
		Cursor cursor = getDatabase().query(getObjectTableName(),
				getObjectTableColumns(), null, null, null, null, null);
		List<T> objects = getObjectsFromCursor(cursor);
		cursor.close();
		return objects;
	}

	public boolean save(T object) throws IllegalArgumentException {
		if (object == null || object.getId() == null) {
			throw new IllegalArgumentException(
					"Illegal object for saving in DB");
		}
		ContentValues contentValues = getContentValues(object);
		if (get(object.getId()) == null) {
			return getDatabase().insert(getObjectTableName(), null,
					contentValues) != -1;
		} else {
			return getDatabase().update(getObjectTableName(), contentValues,
					KEY_ID + "=" + "\'" + object.getId() + "\'", null) > 0;
		}
	}

	/**
	 * ������� ������ �� ��, �� id
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean delete(UUID objectID) throws IllegalArgumentException {
		if (objectID == null) {
			throw new IllegalArgumentException(
					"Illegal argument for deleting task");
		}
		return getDatabase().delete(getObjectTableName(),
				KEY_ID + "=" + "\'" + objectID.toString() + "\'", null) > 0;
	}

	/**
	 * ������� ������ �� ��
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException
	 */
	public boolean delete(T object) throws IllegalArgumentException {
		if (object == null || object.getId() == null) {
			throw new IllegalArgumentException(
					"Illegal argument for deleting task");
		}
		return delete(object.getId());
	}
}
