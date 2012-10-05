package com.cyberprophets.todaytodolist.activities;

//import java.util.Date;
//
//import android.app.ExpandableListActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//import com.cyberprophets.todaytodolist.R;
//import com.cyberprophets.todaytodolist.adapters.GlobalTasksAdapter;
//import com.cyberprophets.todaytodolist.model.Model;
//import com.cyberprophets.todaytodolist.model.ModelListener;
//import com.cyberprophets.todaytodolist.model.dataobjects.tasks.GlobalTask;
//import com.cyberprophets.todaytodolist.model.dataobjects.tasks.Task;
//
///**
// * 
// * @author Mironov S.V.
// * @since 21.08.2012
// */
//public class GlobalTasksActivity extends ExpandableListActivity implements
//		ModelListener {
//	private Model model;
//	private Button addCategoryButton;
//	private Button addTaskButton;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.global_tasks_activity_layout);
//
//		model = new Model(this);
//		getModel().activate();
//
//		addCategoryButton = (Button) findViewById(R.id.add_category_button);
//		addTaskButton = (Button) findViewById(R.id.add_task_button);
//
//		getAddCategoryButton().setOnClickListener(
//				new AddCategoryButtonOnClickListener());
//		getAddTaskButton().setOnClickListener(
//				new AddTaskButtonOnClickListener());
//		getModel().addModelListener(this);
//	}
//
//	private Model getModel() {
//		return model;
//	}
//
//	private Button getAddCategoryButton() {
//		return addCategoryButton;
//	}
//
//	private Button getAddTaskButton() {
//		return addTaskButton;
//	}
//
//	protected void fillData() {
//		GlobalTasksAdapter adapter = null;
//		if (getExpandableListAdapter() == null) {
//			adapter = new GlobalTasksAdapter(getModel(), this);
//			setListAdapter(adapter);
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		fillData();
//	};
//
//	public class AddCategoryButtonOnClickListener implements OnClickListener {
//
//		public void onClick(View v) {
//			getModel().createNewCategory("Category 1");
//		}
//	}
//
//	public class AddTaskButtonOnClickListener implements OnClickListener {
//
//		public void onClick(View v) {
//			getModel().createNewTask("Task 1", new Date());
//		}
//	}
//
//	public void taskCreated(Task task) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void taskDeleted(Task task) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void taskChanged(Task oldTask, Task newTask) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void categoryCreated(GlobalTask category) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void categoryChanged(GlobalTask oldCategory, GlobalTask newCategory) {
//		// TODO Auto-generated method stub
//	}
//
//	public void categoryDeleted(GlobalTask category) {
//		// TODO Auto-generated method stub
//
//	}
//
// }
