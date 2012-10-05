package com.cyberprophets.todaytodolist.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.dataobjects.tasks.GlobalTask;

public class GlobalTaskView extends LinearLayout {
	private final TextView nameTextView;
	private GlobalTask task;
	private final Model model;

	public GlobalTaskView(Context context, GlobalTask category, Model model) {
		super(context);
		View view = inflate(getContext(), R.layout.category_view, null);
		addView(view);

		this.model = model;
		this.task = category;

		nameTextView = (TextView) findViewById(R.id.category_name);
	}

	private GlobalTask getTask() {
		return task;
	}

	public void setTask(GlobalTask category) {
		this.task = category;
	}

	private TextView getNameTextView() {
		return nameTextView;
	}

	private Model getModel() {
		return model;
	}

	public void fillView() {
		if (getTask() == null) {
			return;
		}
		getNameTextView().setText(getTask().getTitle());
	}
}
