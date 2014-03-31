package com.trupt.myfiles.ui.frag;

import android.app.Activity;
import android.app.Fragment;

import com.trupt.myfiles.listener.FileBrowseListener;

public abstract class BaseFragment extends android.support.v4.app.Fragment {
	
	protected FileBrowseListener fileBrowseListener;
	protected static Activity activity;
	
	public void setFileBrowseListener(FileBrowseListener listener) {
		fileBrowseListener = listener;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BaseFragment.activity = activity;
    }
		
	public void onActivityBackPressed() {
		fileBrowseListener.onFileBrowseCancelled();
	}
		
	public abstract String getFragmentName();
	public abstract void updateActionBarItems();
	//Method to finish Contextual Action bar when slides to another fragment
	public abstract void dismissSelectionMode();
		
}
