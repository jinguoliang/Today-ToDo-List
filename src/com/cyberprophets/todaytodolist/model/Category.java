package com.cyberprophets.todaytodolist.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Класс, поисывающий категорию задач
 * 
 * @author Mironov S.V.
 * @since 13.08.2012
 */
public class Category {
	private UUID id;
	private String name;
	private List<Task> tasks;

	public Category() {
		this(UUID.randomUUID(), new String());
	}

	public Category(String name) {
		this(UUID.randomUUID(), name);
	}

	public Category(UUID id, String name) {
		this.id = id;
		setName(name);
		tasks = new LinkedList<Task>();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
