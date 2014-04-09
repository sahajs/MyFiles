package com.trupt.myfiles.util;

import java.io.File;
import java.util.ArrayList;

import com.trupt.myfiles.R;
import com.trupt.myfiles.model.HomeItem;
import com.trupt.myfiles.model.enums.FragmentNameEnum;
import com.trupt.myfiles.model.enums.HomeItemEnum;

public class HomeItemsUtil {

	private static ArrayList<HomeItem> getLibraryHomeItems() {
		ArrayList<HomeItem> listItems = new ArrayList<HomeItem>(4);
		HomeItem item3 = new HomeItem("Pictures", 0, 0, "Pictures", FragmentNameEnum.PicturesFragment, R.drawable.ic_my_pictures);
		HomeItem item4 = new HomeItem("Videos", 0, 0, "Videos", FragmentNameEnum.VideosFragment, R.drawable.ic_my_videos);
		HomeItem item5 = new HomeItem("Music", 0, 0, "Music", FragmentNameEnum.MusicFragment, R.drawable.ic_my_music);
		HomeItem item6 = new HomeItem("Documents", 0, 0, "Documents", FragmentNameEnum.DocumentsFragment, R.drawable.ic_my_documents);
		listItems.add(item3);
		listItems.add(item4);
		listItems.add(item5);
		listItems.add(item6);
		return listItems;
	}
		
	private static ArrayList<HomeItem> getStorageHomeItems() {
		ArrayList<HomeItem> listItems = new ArrayList<HomeItem>(4);
		String[] mountedStorage = StorageUtil.getStorageDirectories();
		for(String str : mountedStorage) {
			File file = new File(str);
			HomeItem homeItems = new HomeItem(file.getName(), 0, 0, file.getAbsolutePath(), FragmentNameEnum.StorageFilesFragment, R.drawable.ic_my_files);
			listItems.add(homeItems);
		}
		return listItems;
	}
	
	public static ArrayList<HomeItem> getHomeItems(HomeItemEnum homeItemEnum) {
		ArrayList<HomeItem> listItems = new ArrayList<HomeItem>();
		switch (homeItemEnum) {
			case HOME: {
				HomeItem item = new HomeItem("Home", 0, 0, "Home", FragmentNameEnum.HomeFragment, R.drawable.ic_my_files);		
				listItems.add(item);
			}
			break;
			case ROOT: {
				HomeItem homeItem = new HomeItem("/ Device", 0, 0, "/", FragmentNameEnum.StorageFilesFragment, R.drawable.ic_my_files);
				listItems.add(homeItem);
			}
			break;
			case STORAGE: {
				listItems.addAll(getStorageHomeItems());
			}
			break;
			case LIBRARY: {
				listItems.addAll(getLibraryHomeItems());
			}
			break;
			case FAVOURITE: {
				HomeItem item = new HomeItem("Favourites", 0, 0, "Favourites", FragmentNameEnum.FavouriteFilesFragment, R.drawable.ic_my_favorites);
				listItems.add(item);
			}
			break;
			case RECENT: {
				HomeItem item = new HomeItem("Recent Files", 0, 0, "Recent Files", FragmentNameEnum.RecentFilesFragment, R.drawable.ic_my_recent_files);
				listItems.add(item);
			}
			break;
		}
		return listItems;
	}

}
