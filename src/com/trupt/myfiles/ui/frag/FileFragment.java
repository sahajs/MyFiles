package com.trupt.myfiles.ui.frag;

import java.io.File;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.trupt.myfiles.R;
import com.trupt.myfiles.adapter.FileListAdapter;
import com.trupt.myfiles.animation.ImageAnimation;
import com.trupt.myfiles.core.Global;
import com.trupt.myfiles.model.RecentFile;
import com.trupt.myfiles.model.RecentFileManager;
import com.trupt.myfiles.ui.FilePathHorizontalScrollView;
import com.trupt.myfiles.ui.OnFilePathHSVClickListener;
import com.trupt.myfiles.ui.view.ThumbnailImageView;
import com.trupt.myfiles.util.FileUtil;

public abstract class FileFragment extends BaseFragment implements OnItemClickListener, 
		OnItemLongClickListener, OnFilePathHSVClickListener {
	
	protected ListView listViewFileList;
	protected TextView textViewEmptyDirectory;
	protected LinearLayout linearLayoutDetails;
	
	protected FileListAdapter fileListAdapter;
	
	//protected static ActionMode actionMode;
	protected ArrayList<File> alFileList;
	protected ArrayList<Integer> alSelectedViewIndex;
	protected int selectedViewIndex;
	protected boolean isSelectEnable;	
	
	protected FilePathHorizontalScrollView horizontalScrollViewFilePath;
	
	protected String currentFilePath;
	protected String originFilePath;
	
	protected boolean isFirstTime = true;
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_file_browse_storage, container,
				false);
		setRetainInstance(true);
		initView(view);
		return view;
	}
	
	protected void initView(View view) {
		listViewFileList = (ListView) view.findViewById(R.id.listViewFileBrowse);
		listViewFileList.setOnItemClickListener(this);
		listViewFileList.setOnItemLongClickListener(this);

		textViewEmptyDirectory = (TextView) view.findViewById(R.id.textViewEmptyDirectory);
		
		linearLayoutDetails = (LinearLayout) view.findViewById(R.id.linearLayoutDetails);
		linearLayoutDetails.setVisibility(View.INVISIBLE);

		alSelectedViewIndex = new ArrayList<Integer>();
		alFileList = new ArrayList<File>();

		isSelectEnable = false;
		fileListAdapter = new FileListAdapter(activity, alFileList, isSelectEnable, alSelectedViewIndex);
		listViewFileList.setAdapter(fileListAdapter);
		
		//horizontalScrollViewFilePath = (FilePathHorizontalScrollView) view
		//		.findViewById(R.id.horizontalScrollViewFilePath);
		//horizontalScrollViewFilePath.setHorizontalScrollBarEnabled(false);
		//horizontalScrollViewFilePath.setOnFilePathHSVClickListener(this);
		//llFilePath = (LinearLayout) view.findViewById(R.id.llFilePath);
		if(horizontalScrollViewFilePath == null) {
			horizontalScrollViewFilePath = new FilePathHorizontalScrollView(activity);
			horizontalScrollViewFilePath.setHorizontalScrollBarEnabled(false);
			horizontalScrollViewFilePath.setOnFilePathHSVClickListener(this);
			horizontalScrollViewFilePath.setTag(getFragmentName());
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		restoreSavedInstanceState(savedInstanceState);
		if(isFirstTime == true) {
			activity.getActionBar().setCustomView(horizontalScrollViewFilePath);
			activity.getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		}
		setUpTitle();
		setUpViews();
		isFirstTime = false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onActivityBackPressed();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("IS_SELECT_ENABLE", isSelectEnable);
		outState.putSerializable("LIST_SELECTED_VIEW_INDEX", alSelectedViewIndex);
	}
	
	@SuppressWarnings("unchecked")
	protected void restoreSavedInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			isSelectEnable = savedInstanceState.getBoolean("IS_SELECT_ENABLE");
			alSelectedViewIndex = (ArrayList<Integer>)savedInstanceState.getSerializable("LIST_SELECTED_VIEW_INDEX");
			if(isSelectEnable) {
				activity.startActionMode(new SelectActionMode());
			}
		}
	}
	
	public void onItemClick(AdapterView<?> aView, View view, int index,
			long arg3) {
		Vibrator vibrator = (Vibrator) FileFragment.activity
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(15);
		selectedViewIndex = listViewFileList.getFirstVisiblePosition();
		final File file = (File) fileListAdapter.getItem(index);
		
		if (isSelectEnable == false) {
			openFile(file);
		} else {
			updateView(view, index);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int index,
			long arg3) {
		if (alSelectedViewIndex.size() == 0) {
			Vibrator vibrator = (Vibrator) FileFragment
					.activity.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(60);
			activity.startActionMode(new SelectActionMode());
			updateView(view, index);
			return true;
		}
		return false;
	}
	
	public void drawerClosed() {
		if(isSelectEnable) {
			//remove custom view
			activity.startActionMode(new SelectActionMode());
		}
	}
	
	public void drawerOpened() {
		TextView tv = new TextView(activity);
		activity.getActionBar().setCustomView(tv);
		//add custom view
	}
			
	public void onActivityBackPressed() {
		fileBrowseListener.onFileBrowseCancelled();
	}
	
	@Override
	public void dismissSelectionMode() {
		ActionMode actionMode = Global.getActionMode();
		if(actionMode != null) {
			actionMode.finish();
		}
	}
	
	protected void openFile(File file) {
		//TODO: Save filepath for recent docs
		RecentFile recentFile = new RecentFile(file, System.currentTimeMillis());
		RecentFileManager.getInstance().addFile(recentFile);
		//DataStorageUtil.saveToFile(activity, Constants.DataStoragePath.RECENT_FILE_STOREGE_PATH, file);
	}
	
	protected void setUpTitle() {
	}
	
	protected void setUpViews() {
		populateFileList();
		populateFilePathView();
	}

	protected void populateFileList() {
	}
	
	protected void populateFilePathView() {
		if(currentFilePath != null && originFilePath != null) {
			horizontalScrollViewFilePath.displayView(currentFilePath, originFilePath);
		}
	}
	
	protected void updateView(View view, int index) {
		ImageView ivFileTypeImage = (ImageView) view.findViewById(R.id.ivFileTypeImage);
		if (!alSelectedViewIndex.contains(index)) {
			alSelectedViewIndex.add(index);
			view.setBackgroundResource(R.drawable.longpressed_holo_light);
			ImageAnimation.flipToSelectedView(ivFileTypeImage);
		} else {
			//if (alSelectedViewIndex.size() == 1) {
				
			//	return;
			//}
			alSelectedViewIndex.remove(Integer.valueOf(index));
			view.setBackgroundColor(Color.TRANSPARENT);
			ImageAnimation.flipToOriginalView(ivFileTypeImage,
					FileUtil.getImageIconBitmap((File) fileListAdapter.getItem(index)));
			if(alSelectedViewIndex.isEmpty()) {
				Global.getActionMode().finish();
			}
		}
		updateSelectedTitle();
	}
	
	protected void updateSelectedTitle() {
		if (Global.getActionMode() != null) {
			Global.getActionMode().setTitle("" + alSelectedViewIndex.size() + " Selected");
		}
	}
	
	@Override
	public String getFragmentName() {
		return FileFragment.class.getName();
	}
	
	@Override
	public void updateActionBarItems() {
		//populateFilePathView();
		if(horizontalScrollViewFilePath != null) {
			activity.getActionBar().setCustomView(horizontalScrollViewFilePath);
		}
	}
	
	private class SelectActionMode implements ActionMode.Callback {
		
		private boolean isAllSelected;

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			Global.setActionMode(mode);
			isAllSelected = false;
			switch (item.getItemId()) {
				case R.id.ciShare:
					ArrayList<File> files = new ArrayList<File>();
					for(int index : alSelectedViewIndex) {
						File file = alFileList.get(index);
						files.add(file);
					}
					shareFiles(files);
					break;
				case R.id.ciSelectAll:
					alSelectedViewIndex.clear();
					for(int i = 0; i < alFileList.size(); i++) {
						alSelectedViewIndex.add(i);
					}
					fileListAdapter.notifyDataSetChanged();
					updateSelectedTitle();
					isAllSelected = true;
					mode.invalidate();
					break;
			}
			return true;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			Global.setActionMode(mode);
			isSelectEnable = true;
			fileListAdapter.setIsSelectEnable(isSelectEnable);
			MenuInflater menuInflater = activity.getMenuInflater();
			menuInflater.inflate(R.menu.co_file_browse_fragment, menu);
			updateSelectedTitle();
			return true;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			//Remove all selected views
			while(!alSelectedViewIndex.isEmpty()) {
				int index = alSelectedViewIndex.remove(0);
				if(listViewFileList.getFirstVisiblePosition() <= index && listViewFileList.getLastVisiblePosition() >= index) {
					View view = listViewFileList.getChildAt(index - listViewFileList.getFirstVisiblePosition());
					if(view != null) {
						ThumbnailImageView ivFileTypeImage = (ThumbnailImageView) view.findViewById(R.id.ivFileTypeImage);
						view.setBackgroundColor(Color.TRANSPARENT);
						ImageAnimation.flipToOriginalView(ivFileTypeImage,
								FileUtil.getImageIconBitmap((File) fileListAdapter.getItem(index)));
					}
				}
			}
			Global.setActionMode(null);
			isSelectEnable = false;
			fileListAdapter.setIsSelectEnable(isSelectEnable);
			//fileListAdapter.notifyDataSetChanged();
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			if(isAllSelected) {
				menu.getItem(4).setEnabled(false);
				//TODO set disabled icon
			}
			return true;
		}
		
		private void shareFiles(ArrayList<File> listFiles) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			intent.setType(FileUtil.getCommonMimeType(listFiles));

			ArrayList<Uri> files = new ArrayList<Uri>();

			for(File file : listFiles) {
				Uri uri = Uri.fromFile(file);
				files.add(uri);
			}
			
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
			startActivity(Intent.createChooser(intent, "Share with"));
		}

	}
	
	@Override
	public void onClick(String filePath) {
		if(!filePath.equalsIgnoreCase(currentFilePath)) {
			currentFilePath = filePath;
			setUpViews();
		}
	}
			
}
