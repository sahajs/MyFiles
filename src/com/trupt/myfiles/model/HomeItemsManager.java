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
import com.trupt.myfiles.model.enums.HomeItemEnum;
import com.trupt.myfiles.util.HomeItemsUtil;

public class HomeItemsManager {

	private static final String FILE_NAME = "HomeItemsFiles";
	public static HomeItemsManager homeItemsManager;
	private ArrayList<HomeItem> listHomeItems;
	
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
	
	public void addFile(HomeItem homeItem) {
		int index = listHomeItems.indexOf(homeItem);
		if(index != -1) {
			listHomeItems.remove(homeItem);
		} 
		listHomeItems.add(homeItem);
		listHomeItems.removeAll(loadMountedDevices());
		storeHomeItems(listHomeItems);
	}
	
	public ArrayList<HomeItem> getHomeItemsList() {
		return listHomeItems;
	}
	
	private static ArrayList<HomeItem> loadMountedDevices() {
		ArrayList<HomeItem> mountedDevices = new ArrayList<HomeItem>();
		mountedDevices.addAll(HomeItemsUtil.getHomeItems(HomeItemEnum.STORAGE));
		mountedDevices.addAll(HomeItemsUtil.getHomeItems(HomeItemEnum.LIBRARY));
		mountedDevices.addAll(HomeItemsUtil.getHomeItems(HomeItemEnum.FAVOURITE));
		mountedDevices.addAll(HomeItemsUtil.getHomeItems(HomeItemEnum.RECENT));
		//TODO: get from database user defined directories at home location
		return mountedDevices;
	}
	
	//TODO: Review Use database to store recent files
	private static ArrayList<HomeItem> loadHomeItems() {
		ArrayList<HomeItem> favFiles = new ArrayList<HomeItem>();
		ObjectInputStream oinStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			oinStream = new ObjectInputStream(new FileInputStream(file));
			favFiles.addAll((ArrayList<HomeItem>) oinStream.readObject());
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
	
	private static void storeHomeItems(ArrayList<HomeItem> homeItems) {
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
