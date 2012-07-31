package com.cyberprophets.todaytodolist;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

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

		TabSpec allTasks = tabHost.newTabSpec("tab1");
		allTasks.setIndicator("All tasks");
		Intent allTasksIntent = new Intent(this, AllTasksActivity.class);
		allTasks.setContent(allTasksIntent);
		tabHost.addTab(allTasks);

		TabSpec tasksByDate = tabHost.newTabSpec("tab2");
		tasksByDate.setIndicator("Tasks by date");
		Intent tasksByDateIntent = new Intent(this, TasksByDateActivity.class);
		tasksByDate.setContent(tasksByDateIntent);
		tabHost.addTab(tasksByDate);
	}
}
