package com.trupt.myfiles.ui.frag.lib;

import java.io.FileFilter;

import android.view.View;

import com.trupt.myfiles.model.MyFragSingle;
import com.trupt.myfiles.model.RecentFileManager;


public class RecentFilesFragment extends LibraryFragment {
	
	@Override
	protected void setUpTitle() {
		currentFilePath = "Recent Files";
		originFilePath = currentFilePath;
	}
	
	public String getFragmentName() {
		return RecentFilesFragment.class.getName();
	}
	
	@Override
	protected void populateFileList() {
		alFileList = RecentFileManager.getInstance().getRecentFileList(alFileList);

		if (alFileList.size() == 0) {
			textViewEmptyDirectory.setVisibility(View.VISIBLE);
		} else {
			textViewEmptyDirectory.setVisibility(View.INVISIBLE);
		}
	
		fileListAdapter.notifyDataSetChanged();
		listViewFileList.setSelection(0);
	}
	
	protected FileFilter getFileFilter() {
		return null;
	}
	
}
