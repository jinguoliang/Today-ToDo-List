package com.cyberprophets.todaytodolist;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Activity по редактированию задачи
 * 
 * @author Mironov
 * @since 23.07.2012
 */
public class EditTaskActivity extends Activity {

	private EditText title;
	private EditText description;
	private DatePicker date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_task_activity);

		title = (EditText) findViewById(R.id.title);
		description = (EditText) findViewById(R.id.description);
		date = (DatePicker) findViewById(R.id.date);
	}

}
