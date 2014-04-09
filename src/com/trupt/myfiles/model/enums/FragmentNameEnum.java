package com.trupt.myfiles.model.enums;

public enum FragmentNameEnum {
	HomeFragment("HomeFragment"),
	AllFilesFragment("AllFilesFragment"), MusicFragment("MusicFragment"), VideosFragment("VideosFragment"),
	PicturesFragment("PicturesFragment"), DocumentsFragment("DocumentsFragment"), 
	RecentFilesFragment("RecentFilesFragment"), FavouriteFilesFragment("FavouriteFilesFragment"), 
	StorageFilesFragment("StorageFilesFragment");
	
	String fragmentName;
	
	private FragmentNameEnum() {
	}
	
	private FragmentNameEnum(String name) {
		fragmentName = name;
	}
	
}
