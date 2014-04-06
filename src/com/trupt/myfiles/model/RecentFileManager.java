package com.trupt.myfiles.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.trupt.myfiles.app.MyFilesApplication;

public class RecentFileManager {

	private static final String FILE_NAME = "RecentFiles";
	private static RecentFileManager recentFileManager;
	private ArrayList<RecentFile> listRecentFiles;
	
	protected RecentFileManager() {
		listRecentFiles = loadRecentFiles();
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
		storeRecentFiles(listRecentFiles);
	}
	
	public ArrayList<File> getRecentFileList(ArrayList<File> list) {
		for(RecentFile recentFile : listRecentFiles) {
			list.add(recentFile.getFile());
		}
		return list;
	}
	
	public NumberNSize getNumberNSize() {
		long numFiles = listRecentFiles.size();
		long size = this.getRecentFilesSize();
		return new NumberNSize(size, numFiles);
	}
	
	private long getRecentFilesSize() {
		long size = 0;
		for(RecentFile recentFile : listRecentFiles) {
			size += recentFile.getFile().length();
		}
		return size;
	}
	
	//TODO: Review Use database to store recent files
	private static ArrayList<RecentFile> loadRecentFiles() {
		ArrayList<RecentFile> recentFiles = new ArrayList<RecentFile>();
		ObjectInputStream oinStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			oinStream = new ObjectInputStream(new FileInputStream(file));
			recentFiles.addAll((ArrayList<RecentFile>) oinStream.readObject());
		} catch(FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		if(oinStream != null) {
			try {
				oinStream.close();
			} catch (IOException e) {
			}
		}
		return recentFiles;
	}
	
	private static void storeRecentFiles(ArrayList<RecentFile> recentFiles) {
		ObjectOutputStream outStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			outStream = new ObjectOutputStream(new FileOutputStream(file));
			outStream.writeObject(recentFiles);
		} catch(FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		if(outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
			}
		}
	}
	
}
