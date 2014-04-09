package com.trupt.myfiles.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.trupt.myfiles.R;
import com.trupt.myfiles.model.HomeItems;
import com.trupt.myfiles.model.enums.FragmentNameEnum;

public class StorageUtil {
	
	/*
	 * ************************************************* 
	 * I found more reliable way to get paths to all SD-CARDs in system. 
	 * This works on all Android versions and return paths to all storages (include emulated).
	 * Works correctly on all my devices. 
	 * P.S.: Based on source code of Environment class
	 */
	private static final Pattern DIR_SEPORATOR = Pattern.compile("/");
	/*
	 * Raturns all available SD-Cards in the system (include emulated)
	 *
	 * Warning: Hack! Based on Android source code of version 4.3 (API 18)
	 * Because there is no standart way to get it.
	 * TODO: Test on future Android versions 4.4+
	 *
	 * @return paths to all available SD-Cards in the system (include emulated)
	 */
	private static String[] getStorageDirectories() {
	    // Final set of paths
	    final Set<String> rv = new HashSet<String>();
	    // Primary physical SD-CARD (not emulated)
	    final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
	    // All Secondary SD-CARDs (all exclude primary) separated by ":"
	    final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
	    // Primary emulated SD-CARD
	    final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
	    if(TextUtils.isEmpty(rawEmulatedStorageTarget))
	    {
	        // Device has physical external storage; use plain paths.
	        if(TextUtils.isEmpty(rawExternalStorage))
	        {
	            // EXTERNAL_STORAGE undefined; falling back to default.
	            rv.add("/storage/sdcard0");
	        }
	        else
	        {
	            rv.add(rawExternalStorage);
	        }
	    }
	    else
	    {
	        // Device has emulated storage; external storage paths should have
	        // userId burned into them.
	        final String rawUserId;
	        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
	        {
	            rawUserId = "";
	        }
	        else
	        {
	            final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
	            final String[] folders = DIR_SEPORATOR.split(path);
	            final String lastFolder = folders[folders.length - 1];
	            boolean isDigit = false;
	            try
	            {
	                Integer.valueOf(lastFolder);
	                isDigit = true;
	            }
	            catch(NumberFormatException ignored)
	            {
	            }
	            rawUserId = isDigit ? lastFolder : "";
	        }
	        // /storage/emulated/0[1,2,...]
	        if(TextUtils.isEmpty(rawUserId))
	        {
	            rv.add(rawEmulatedStorageTarget);
	        }
	        else
	        {
	            rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
	        }
	    }
	    // Add all secondary storages
	    if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
	    {
	        // All Secondary SD-CARDs splited into array
	        final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
	        Collections.addAll(rv, rawSecondaryStorages);
	    }
	    return rv.toArray(new String[rv.size()]);
	}

	
	public static ArrayList<HomeItems> getLibraryHomeItems() {
		ArrayList<HomeItems> listItems = new ArrayList<HomeItems>();
		HomeItems item2 = new HomeItems("Favourites", 0, 0, "", FragmentNameEnum.FavouriteFilesFragment, R.drawable.ic_my_favorites);
		HomeItems item3 = new HomeItems("Pictures", 0, 0, "", FragmentNameEnum.PicturesFragment, R.drawable.ic_my_pictures);
		HomeItems item4 = new HomeItems("Videos", 0, 0, "", FragmentNameEnum.VideosFragment, R.drawable.ic_my_videos);
		HomeItems item5 = new HomeItems("Music", 0, 0, "", FragmentNameEnum.MusicFragment, R.drawable.ic_my_music);
		HomeItems item6 = new HomeItems("Documents", 0, 0, "", FragmentNameEnum.DocumentsFragment, R.drawable.ic_my_documents);
		HomeItems item7 = new HomeItems("Recent Files", 0, 0, "", FragmentNameEnum.RecentFilesFragment, R.drawable.ic_my_recent_files);		
		listItems.add(item2);
		listItems.add(item3);
		listItems.add(item4);
		listItems.add(item5);
		listItems.add(item6);
		listItems.add(item7);
		return listItems;
	}
	
	public static ArrayList<HomeItems> getStorageHomeItems() {
		ArrayList<HomeItems> listItems = new ArrayList<HomeItems>();
		String[] mountedStorage = StorageUtil.getStorageDirectories();
		for(String str : mountedStorage) {
			File file = new File(str);
			HomeItems homeItems = new HomeItems(file.getName(), 0, 0, file.getAbsolutePath(), FragmentNameEnum.StorageFilesFragment, R.drawable.ic_my_files);
			listItems.add(homeItems);
		}
		return listItems;
	}
}
