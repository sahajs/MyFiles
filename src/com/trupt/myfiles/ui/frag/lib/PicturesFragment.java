package com.trupt.myfiles.ui.frag.lib;

import java.io.File;
import java.io.FileFilter;

import android.net.Uri;
import android.provider.MediaStore;

import com.trupt.myfiles.model.enums.FileTypeEnum;
import com.trupt.myfiles.model.enums.MediaTypeEnum;
import com.trupt.myfiles.util.FileUtil;

public class PicturesFragment extends LibraryFragment {
	
	@Override
	protected void setUpTitle() {
		currentFilePath = "My Pictures";
		originFilePath = currentFilePath;
	}
	
	public String getFragmentName() {
		return PicturesFragment.class.getName();
	}
		
	public MediaTypeEnum getMediaType() {
		return MediaTypeEnum.IMAGE;
	}
	
	public Uri getMediaUri() {
		return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}
	
	
	protected FileFilter getFileFilter() {
		return new PictureFileFilter();
	}
	
	private class PictureFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			boolean retValue = false;
			if(file.isDirectory()) {
				retValue = true;
			} else if(FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.JPG.toString()) 
						|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.JPEG.toString())
							|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.GIF.toString())
								|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.PNG.toString())) {
				retValue = true;
			}
			return retValue;
		}
	}
}
