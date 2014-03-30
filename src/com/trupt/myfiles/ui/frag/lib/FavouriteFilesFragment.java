package com.trupt.myfiles.ui.frag.lib;

import java.io.FileFilter;

import android.view.View;

import com.trupt.myfiles.model.FavouriteFileManager;
import com.trupt.myfiles.model.MyFragSingle;


public class FavouriteFilesFragment extends LibraryFragment {
	
	@Override
	protected void setUpTitle() {
		currentFilePath = "My Favourites";
		originFilePath = currentFilePath;
	}
	
	
	public String getFragmentName() {
		return FavouriteFilesFragment.class.getName();
	}
	
	/*@Override
	public void updateActionBarItems() {
		if(MyFragSingle.getInstance().getCurrentFragmentIndex() == 
				MyFragSingle.getInstance().getListFragmentNames().indexOf(getFragmentName())) {
			if(numFiles == 0) {
				activity.getActionBar().setTitle("Favourite Files");
			} else {
				activity.getActionBar().setTitle("Favourite Files (" + numFiles + ")");
			}
		}
	}*/
	
	@Override
	protected void populateFileList() {
		alFileList = FavouriteFileManager.getInstance().getFavouriteFileList(alFileList);

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
