package com.trupt.myfiles.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.trupt.myfiles.R;

public class MenuExpandableListAdapter implements ExpandableListAdapter {
	
	private Context context;
	private LinkedHashMap<String, ArrayList<String>> mapMenuItems;
	private ArrayList<String> listMenuHeaders;

	public MenuExpandableListAdapter(Context context,
			LinkedHashMap<String, ArrayList<String>> mapMenuItems,
			ArrayList<String> listMenuHeaders) {
		super();
		this.context = context;
		this.mapMenuItems = mapMenuItems;
		this.listMenuHeaders = listMenuHeaders;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mapMenuItems.get(listMenuHeaders.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.expand_listview_main_menu_child, null);
		}
		TextView textView = (TextView) view.findViewById(R.id.tvMenuName);
		textView.setPadding(10, 0, 0, 0);
		textView.setText(mapMenuItems.get(listMenuHeaders.get(groupPosition)).get(childPosition));
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mapMenuItems.get(listMenuHeaders.get(groupPosition)).size();
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mapMenuItems.get(listMenuHeaders.get(groupPosition));
	}

	@Override
	public int getGroupCount() {
		return listMenuHeaders.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.expand_listview_main_menu_group, null);
		}
		TextView textView = (TextView) view.findViewById(R.id.tvMenuName);
		textView.setText(listMenuHeaders.get(groupPosition));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return mapMenuItems.isEmpty();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}
	
}