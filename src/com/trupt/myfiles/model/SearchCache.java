package com.trupt.myfiles.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;

import com.trupt.myfiles.app.MyFilesApplication;

public class SearchCache {
	
	private static final String FILE_NAME = "SearchCache";

	private static SearchCache searchCache;
	private HashMap<String, LinkedHashSet<File>> mapSearchFiles;
	
	private SearchCache() {
		mapSearchFiles = loadSearchCache();
	}
	
	public static SearchCache getInstance() {
		if(searchCache == null) {
			searchCache = new SearchCache();
		}
		return searchCache;
	}
	
	public boolean updateSearchCache(String keyWord, LinkedHashSet<File> searchResults) {
		boolean isChange = false;
		LinkedHashSet<File> olderResults = mapSearchFiles.get(keyWord);
		if(olderResults == null) {
			olderResults = new LinkedHashSet<File>();
		}
		isChange = olderResults.addAll(searchResults);
		if(isChange) {
			mapSearchFiles.put(keyWord, searchResults);
			storeSearchCache(mapSearchFiles);
		}
		return isChange;
	}
	
	public LinkedHashSet<File> getSearchCache(String keyWord) {
		return mapSearchFiles.get(keyWord);
	}
	
	//TODO: add database to store these values
	//includes keyword, storage/path, 
	private static HashMap<String, LinkedHashSet<File>> loadSearchCache() {
		HashMap<String, LinkedHashSet<File>> mapFiles = new HashMap<String, LinkedHashSet<File>>();
		ObjectInputStream oinStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			oinStream = new ObjectInputStream(new FileInputStream(file));
			mapFiles.putAll((HashMap<String, LinkedHashSet<File>>) oinStream.readObject());
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
		return mapFiles;
	}
	
	private static void storeSearchCache(HashMap<String, LinkedHashSet<File>> mapFiles) {
		ObjectOutputStream outStream = null;
		try {
			File file = new File(MyFilesApplication.getAppContext().getFilesDir(), FILE_NAME);
			outStream = new ObjectOutputStream(new FileOutputStream(file));
			outStream.writeObject(mapFiles);
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
