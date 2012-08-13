package com.cyberprophets.todaytodolist.model;

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

	public Category() {
		id = UUID.randomUUID();
		name = new String();
	}

	public Category(UUID id, String name) {
		this.id = id;
		setName(name);
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

}
