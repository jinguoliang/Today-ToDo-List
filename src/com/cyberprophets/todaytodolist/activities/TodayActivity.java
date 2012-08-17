package com.cyberprophets.todaytodolist.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.cyberprophets.todaytodolist.R;

/**
 * 
 * @author Mironov S.V.
 * @since 24.07.2012
 */
public class TodayActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.today_activity);
		TabHost tabHost = getTabHost();

		TabSpec tasksByDate = tabHost.newTabSpec("tab1");
		tasksByDate.setIndicator("Tasks by date");
		Intent tasksByDateIntent = new Intent(this, TasksByDateActivity.class);
		tasksByDate.setContent(tasksByDateIntent);
		tabHost.addTab(tasksByDate);

		// TabSpec noteCompleteTasks = tabHost.newTabSpec("tab2");
		// noteCompleteTasks.setIndicator("Global tasks");
		// Intent noteCompleteTasksIntent = new Intent(this,
		// GlobalTasksActivity.class);
		// noteCompleteTasks.setContent(noteCompleteTasksIntent);
		// tabHost.addTab(noteCompleteTasks);
	}
}
