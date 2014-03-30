
package com.trupt.myfiles.listener;

import com.trupt.myfiles.model.enums.FragmentNameEnum;

public interface FileBrowseListener {

	public void onFileBrowseCancelled();
	public void onFileBrowseCompleted();
	public void onNewFileBrowseStart(FragmentNameEnum fragmentNameEnum, String originPath);
	
}
