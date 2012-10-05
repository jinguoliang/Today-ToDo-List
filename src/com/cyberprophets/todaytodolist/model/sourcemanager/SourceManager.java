package com.cyberprophets.todaytodolist.model.sourcemanager;

import java.util.List;
import java.util.UUID;

import com.cyberprophets.todaytodolist.model.dataobjects.Date;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.DailyTask;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.GlobalTask;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;

/**
 * Интерфейс описывающий функциональность менеджера по управлению данными
 * 
 * @author Mironov S.V.
 * @since 18.07.2009
 */
public interface SourceManager {
	public SourceManager open();

	public void close();

	public GlobalTask getGlobalTask(UUID id);

	public DailyTask getDailyTask(UUID id);

	public List<GlobalTask> getAllGlobalTasks();

	public List<DailyTask> getDailyTasks(Date date);

	public boolean delete(Task task);

	public boolean save(Task task);
}
