package com.cyberprophets.todaytodolist.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyberprophets.todaytodolist.R;
import com.cyberprophets.todaytodolist.model.Model;
import com.cyberprophets.todaytodolist.model.task.Category;

public class CategoryView extends LinearLayout {
	private final TextView nameTextView;
	private Category category;
	private final Model model;

	public CategoryView(Context context, Category category, Model model) {
		super(context);
		View view = inflate(getContext(), R.layout.category_view, null);
		addView(view);

		this.model = model;
		this.category = category;

		nameTextView = (TextView) findViewById(R.id.category_name);
	}

	private Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	private TextView getNameTextView() {
		return nameTextView;
	}

	private Model getModel() {
		return model;
	}

	public void fillView() {
		if (getCategory() == null) {
			return;
		}
		getNameTextView().setText(getCategory().getName());
	}
}
