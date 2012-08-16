package com.cyberprophets.todaytodolist.model;

import java.util.Date;
import java.util.UUID;

/**
 * Класс, описывающий задачу
 * 
 * @author Mironov S.V.
 * @since 13.08.2012
 */
public class Task {
	private UUID id;
	private String title;
	private String description;
	private Date date;
	private boolean done;
	private Category category;

	public Task() {
		id = UUID.randomUUID();
		setDone(false);
	}

	public Task(String title, Date date) {
		this();
		setTitle(title);
		setDate(date);
	}

	public Task(UUID id, String title, String description, Date date,
			boolean done) {
		setId(id);
		setTitle(title);
		setDescription(description);
		setDate(date);
		setDone(done);
	}

	public Task(UUID id, String title, String description, Date date,
			boolean done, Category category) {
		this(id, title, description, date, done);
		setCategory(category);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Task)) {
			return false;
		}
		Task task = (Task) o;
		return getId().equals(task.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}
