package com.cyberprophets.todaytodolist.adapters;


///**
// * ������� ��� ����������� ����� �� ����������
// * 
// * @author Mironov S.V.
// * @since 20.08.2012
// */
//public class GlobalTasksAdapter extends BaseExpandableListAdapter implements
//		ModelListener {
//	private final Activity context;
//	private final Model model;
//	private List<GlobalTask> categories;
//
//	public GlobalTasksAdapter(Model model, Activity context) {
//		super();
//		this.model = model;
//		this.context = context;
//		getModel().addModelListener(this);
//		init();
//		categories = getCategoriesFromModel();
//	}
//
//	private Model getModel() {
//		return model;
//	}
//
//	protected void init() {
//	}
//
//	private Activity getContext() {
//		return context;
//	}
//
//	private List<GlobalTask> getCategories() {
//		return categories;
//	}
//
//	public void taskCreated(Task task) {
//		notifyDataSetChanged();
//	}
//
//	public void taskChanged(Task oldTask, Task newTask) {
//		notifyDataSetChanged();
//	}
//
//	public void taskDeleted(Task task) {
//		notifyDataSetChanged();
//	}
//
//	public List<GlobalTask> getCategoriesFromModel() {
//		return getModel().getAllCategories();
//	}
//
//	public Object getChild(int arg0, int arg1) {
//		return getCategories().get(arg0).getTasks().get(arg1);
//	}
//
//	public long getChildId(int groupPosition, int childPosition) {
//		return groupPosition * 100 + childPosition;
//	}
//
//	public View getChildView(int groupPosition, int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//		TaskView taskView = null;
//		if (convertView == null) {
//			GlobalTask category = (GlobalTask) getGroup(groupPosition);
//			Task task = category.getTasks().get(childPosition);
//			taskView = new TaskView(getContext(), task, getModel());
//		} else {
//			taskView = (TaskView) convertView;
//			GlobalTask category = (GlobalTask) getGroup(groupPosition);
//			Task task = category.getTasks().get(childPosition);
//			taskView.setTask(task);
//			// boolean isExpanded = taskView.showNote(getExpandedTasks().get(
//			// task.getId()));
//			// getExpandedTasks().put(task.getId(), isExpanded);
//			taskView.fillView();
//		}
//		return taskView;
//	}
//
//	public int getChildrenCount(int groupPosition) {
//		return 0;
//	}
//
//	public Object getGroup(int groupPosition) {
//		return getCategories().get(groupPosition);
//	}
//
//	public int getGroupCount() {
//		return getCategories().size();
//	}
//
//	public long getGroupId(int groupPosition) {
//		return groupPosition;
//	}
//
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//		GlobalTaskView categoryView = null;
//		if (convertView == null) {
//			categoryView = new GlobalTaskView(getContext(),
//					(GlobalTask) getGroup(groupPosition), getModel());
//			categoryView.fillView();
//		} else {
//			categoryView = (GlobalTaskView) convertView;
//			GlobalTask category = (GlobalTask) getGroup(groupPosition);
//			categoryView.setTask(category);
//			categoryView.fillView();
//		}
//		return categoryView;
//	}
//
//	public boolean hasStableIds() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		return true;
//	}
//
//	public void categoryCreated(GlobalTask category) {
//		categories = getCategoriesFromModel();
//		notifyDataSetChanged();
//	}
//
//	public void categoryChanged(GlobalTask oldCategory, GlobalTask newCategory) {
//		categories = getCategoriesFromModel();
//		notifyDataSetChanged();
//	}
//
//	public void categoryDeleted(GlobalTask category) {
//		categories = getCategoriesFromModel();
//		notifyDataSetChanged();
//	}
//
// }
