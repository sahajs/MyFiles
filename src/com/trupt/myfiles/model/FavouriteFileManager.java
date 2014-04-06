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

public class FavouriteFileManager {

	private static final String FILE_NAME = "FavouriteFiles";
	public static FavouriteFileManager favouriteFileManager;
	private ArrayList<File> listFavouriteFiles;
	
	protected FavouriteFileManager() {
		listFavouriteFiles = loadFavouriteFiles();
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
		storeFavouriteFiles(listFavouriteFiles);
	}
	
	public ArrayList<File> getFavouriteFileList(ArrayList<File> list) {
		return listFavouriteFiles;
	}
	
	public NumberNSize getNumberNSize() {
		long numFiles = listFavouriteFiles.size();
		long size = this.getFavouriteFilesSize();
		return new NumberNSize(size, numFiles);
	}
	
	private long getFavouriteFilesSize() {
		long size = 0;
		for(File file : listFavouriteFiles) {
			size += file.length();
		}
		return size;
	}
	
	//TODO: Review Use database to store recent files
	private static ArrayList<File> loadFavouriteFiles() {
		ArrayList<File> favFiles = new ArrayList<File>();
		ObjectInputStream oinStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			oinStream = new ObjectInputStream(new FileInputStream(file));
			favFiles.addAll((ArrayList<File>) oinStream.readObject());
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
		return favFiles;
	}
	
	private static void storeFavouriteFiles(ArrayList<File> favFiles) {
		ObjectOutputStream outStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			outStream = new ObjectOutputStream(new FileOutputStream(file));
			outStream.writeObject(favFiles);
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
