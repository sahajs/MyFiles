package com.trupt.myfiles.ui.frag.lib;

import java.io.File;
import java.io.FileFilter;

import android.net.Uri;
import android.provider.MediaStore;

import com.trupt.myfiles.model.enums.FileTypeEnum;
import com.trupt.myfiles.model.enums.MediaTypeEnum;
import com.trupt.myfiles.util.FileUtil;

public class MusicFragment extends LibraryFragment {
	
	@Override
	protected void setUpTitle() {
		currentFilePath = "My Music";
		originFilePath = currentFilePath;
	}
	
	public String getFragmentName() {
		return MusicFragment.class.getName();
	}
			
	public MediaTypeEnum getMediaType() {
		return MediaTypeEnum.AUDIO;
	}
	
	public Uri getMediaUri() {
		return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	}
	
	protected FileFilter getFileFilter() {
		return new MusicFileFilter();
	}
	
	private class MusicFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			boolean retValue = false;
			if(file.isDirectory()) {
				retValue = true;
			} else if(FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.MP3.name()) 
						|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.WAV.name())
							|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.OGG.name())
								|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.IMY.name())
									|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.OTA.name())
										|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.RTTL.name())
											|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.XMF.name())
												|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.MXMF.name())
													|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.AAC.name())
														|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.M4A.name())
															|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.GP.name())
																|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.FLAC.name())) {
				retValue = true;
			}
			return retValue;
		}
	}
}
