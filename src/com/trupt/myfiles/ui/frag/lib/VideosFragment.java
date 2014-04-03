package com.trupt.myfiles.ui.frag.lib;

import java.io.File;
import java.io.FileFilter;

import android.net.Uri;
import android.provider.MediaStore;

import com.trupt.myfiles.model.enums.FileTypeEnum;
import com.trupt.myfiles.model.enums.MediaTypeEnum;
import com.trupt.myfiles.util.FileUtil;

public class VideosFragment extends LibraryFragment {
	
	@Override
	protected void setUpTitle() {
		currentFilePath = "My Videos";
		originFilePath = currentFilePath;
	}
	
	public String getFragmentName() {
		return VideosFragment.class.getName();
	}
		
	/*@Override
	protected void populateFileList() {
		
		String[] projection = new String[]{
	            MediaStore.Video.Media._ID,
	            MediaStore.Video.Media.TITLE,
	            MediaStore.Video.Media.SIZE,
	            MediaStore.Video.Media.DATE_TAKEN,
	            MediaStore.Video.Media.MIME_TYPE
	    };
		Uri videos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		Log.e("URI", videos.getPath());
		
		Cursor cur = MediaStore.Video.query(activity.getContentResolver(), videos, projection);
		
		Log.i("ListingImages", Arrays.toString(cur.getColumnNames()) );
		Log.i("ListingImages"," query count="+cur.getCount());
		
		if (cur.moveToFirst()) {
			long id;
	        String title;
	        long size;
	        Date date;
	        String mimeType;
	        
	        int idCol = cur.getColumnIndex(MediaStore.Video.Media._ID);
	        int titleCol = cur.getColumnIndex(MediaStore.Video.Media.TITLE);
	        int sizeCol = cur.getColumnIndex(MediaStore.Video.Media.SIZE);
	        int dateCol = cur.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
	        int mimeCol = cur.getColumnIndex(MediaStore.Video.Media.MIME_TYPE);

	        do {
	            // Get the field values
	            id = cur.getLong(idCol);
	            title = cur.getString(titleCol);
	            size = cur.getLong(sizeCol);
	            date = new Date(cur.getLong(dateCol));
	            mimeType = cur.getString(mimeCol);

	            Library lib = new Library(id, title, size, date, mimeType);
	            
	            alFileList.add(lib);
	            // Do something with the values.
	            //Log.i("ListingImages", id + ", " + title + ", " + size + ", " + date);
	        } while (cur.moveToNext());

	    }
		
		if (alFileList.size() == 0) {
			textViewEmptyDirectory.setVisibility(View.VISIBLE);
		} else {
			textViewEmptyDirectory.setVisibility(View.INVISIBLE);
		}
		mediaListAdapter.notifyDataSetChanged();
	}*/
	
	
	public MediaTypeEnum getMediaType() {
		return MediaTypeEnum.VIDEO;
	}
	
	public Uri getMediaUri() {
		return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	}
	
	protected FileFilter getFileFilter() {
		return new VideoFileFilter();
	}
	
	private class VideoFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			boolean retValue = false;
			if(file.isDirectory()) {
				retValue = true;
			} else if(FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.MP4.toString()) 
						|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.WEBM.toString())
							|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.FLV.toString())
								|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.MKV.toString())
									|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.AVI.toString())
										|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.GP.toString())
											|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.WMV.toString())) {
				retValue = true;
			}
			return retValue;
		}
	}
}
