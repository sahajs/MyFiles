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
import com.trupt.myfiles.util.StorageUtil;

public class HomeItemsManager {

	private static final String FILE_NAME = "HomeItemsFiles";
	public static HomeItemsManager homeItemsManager;
	private ArrayList<HomeItems> listHomeItems;
	
	protected HomeItemsManager() {
		listHomeItems = loadMountedDevices();
		listHomeItems.addAll(loadHomeItems());
	}
	
	public static HomeItemsManager getInstance() {
		if(homeItemsManager == null) {
			homeItemsManager = new HomeItemsManager();
		}
		return homeItemsManager;
	}
	
	public void addFile(HomeItems homeItem) {
		int index = listHomeItems.indexOf(homeItem);
		if(index != -1) {
			listHomeItems.remove(homeItem);
		} 
		listHomeItems.add(homeItem);
		listHomeItems.removeAll(loadMountedDevices());
		storeHomeItems(listHomeItems);
	}
	
	public ArrayList<HomeItems> getHomeItemsList() {
		return listHomeItems;
	}
	
	private static ArrayList<HomeItems> loadMountedDevices() {
		ArrayList<HomeItems> mountedDevices = new ArrayList<HomeItems>();
		mountedDevices.addAll(StorageUtil.getStorageHomeItems());
		mountedDevices.addAll(StorageUtil.getLibraryHomeItems());
		//TODO: get from database user defined directories at home location
		return mountedDevices;
	}
	
	//TODO: Review Use database to store recent files
	private static ArrayList<HomeItems> loadHomeItems() {
		ArrayList<HomeItems> favFiles = new ArrayList<HomeItems>();
		ObjectInputStream oinStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			oinStream = new ObjectInputStream(new FileInputStream(file));
			favFiles.addAll((ArrayList<HomeItems>) oinStream.readObject());
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
	
	private static void storeHomeItems(ArrayList<HomeItems> homeItems) {
		ObjectOutputStream outStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			outStream = new ObjectOutputStream(new FileOutputStream(file));
			outStream.writeObject(homeItems);
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
