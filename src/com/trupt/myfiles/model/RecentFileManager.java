package com.trupt.myfiles.model;

import java.io.File;
import java.util.ArrayList;

public class RecentFileManager {

	public static RecentFileManager recentFileManager;
	private ArrayList<RecentFile> listRecentFiles;
	
	protected RecentFileManager() {
		listRecentFiles = new ArrayList<RecentFile>();
	}
	
	public static RecentFileManager getInstance() {
		if(recentFileManager == null) {
			recentFileManager = new RecentFileManager();
		}
		return recentFileManager;
	}
	
	public void addFile(RecentFile file) {
		int index = listRecentFiles.indexOf(file);
		if(index != -1) {
			listRecentFiles.remove(file);
		} 
		listRecentFiles.add(file);
	}
	
	public ArrayList<File> getRecentFileList(ArrayList<File> list) {
		for(RecentFile recentFile : listRecentFiles) {
			list.add(recentFile.getFile());
		}
		return list;
	}
	
}
