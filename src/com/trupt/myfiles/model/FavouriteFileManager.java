package com.trupt.myfiles.model;

import java.io.File;
import java.util.ArrayList;

public class FavouriteFileManager {

	public static FavouriteFileManager favouriteFileManager;
	private ArrayList<File> listFavouriteFiles;
	
	protected FavouriteFileManager() {
		listFavouriteFiles = new ArrayList<File>();
	}
	
	public static FavouriteFileManager getInstance() {
		if(favouriteFileManager == null) {
			favouriteFileManager = new FavouriteFileManager();
		}
		return favouriteFileManager;
	}
	
	public void addFile(File file) {
		int index = listFavouriteFiles.indexOf(file);
		if(index != -1) {
			listFavouriteFiles.remove(file);
		} 
		listFavouriteFiles.add(file);
	}
	
	public ArrayList<File> getFavouriteFileList(ArrayList<File> list) {
		return listFavouriteFiles;
	}
	
}
