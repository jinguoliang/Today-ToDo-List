package com.cyberprophets.todaytodolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	private Button confirmButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_task_activity);

		title = (EditText) findViewById(R.id.title);
		description = (EditText) findViewById(R.id.description);
		date = (DatePicker) findViewById(R.id.date);
		confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new ConfirmButtonOnClickListener());
	}

	/**
	 * 
	 * @author Mironov
	 * @since 24.07.2012
	 */
	private class ConfirmButtonOnClickListener implements OnClickListener {

		public void onClick(View v) {
		}

	};
}
