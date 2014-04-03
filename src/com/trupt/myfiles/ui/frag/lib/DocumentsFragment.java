package com.trupt.myfiles.ui.frag.lib;

import java.io.File;
import java.io.FileFilter;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.trupt.myfiles.model.enums.FileTypeEnum;
import com.trupt.myfiles.model.enums.MediaTypeEnum;
import com.trupt.myfiles.util.FileUtil;

public class DocumentsFragment extends LibraryFragment {
	
	@Override
	protected void setUpTitle() {
		currentFilePath = "My Documents";
		originFilePath = currentFilePath;
	}
	
	public String getFragmentName() {
		return DocumentsFragment.class.getName();
	}
		
	@Override
	protected void populateFileList() {
		String[] projection = new String[]{ MediaStore.Images.Media.DATA };
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String[] mimeTypes = new String[]{ mimeTypeMap.getMimeTypeFromExtension("pdf"),
				mimeTypeMap.getMimeTypeFromExtension("doc"),
				mimeTypeMap.getMimeTypeFromExtension("docx"),
				mimeTypeMap.getMimeTypeFromExtension("ppt"),
				mimeTypeMap.getMimeTypeFromExtension("pptx"),
				mimeTypeMap.getMimeTypeFromExtension("xls"),
				mimeTypeMap.getMimeTypeFromExtension("xslx"),
				mimeTypeMap.getMimeTypeFromExtension("rtf"),
				mimeTypeMap.getMimeTypeFromExtension("txt"),
				mimeTypeMap.getMimeTypeFromExtension("html"),
				mimeTypeMap.getMimeTypeFromExtension("odt"),
				mimeTypeMap.getMimeTypeFromExtension("odp"),
				mimeTypeMap.getMimeTypeFromExtension("ods"),
				mimeTypeMap.getMimeTypeFromExtension("zip"),
				mimeTypeMap.getMimeTypeFromExtension("rar")};
		StringBuilder whereArgs = new StringBuilder();
		for(String mimeType : mimeTypes) {
			whereArgs.append( MediaStore.Images.Media.MIME_TYPE + "='" + mimeType + "' OR ");			
		}
		whereArgs.delete(whereArgs.length() - 4, whereArgs.length());
		Uri uri = getMediaUri();
		
		Cursor cur = MediaStore.Images.Media.query(activity.getContentResolver(), uri, projection, whereArgs.toString(), MediaStore.Images.Media.TITLE);

		if (cur.moveToFirst()) {	        
	        int dataCol = cur.getColumnIndex(MediaStore.Images.Media.DATA);
	        do {
	            String data = cur.getString(dataCol);
	            alFileList.add(new File(data));
	        } while (cur.moveToNext());
	    }
		
		if (alFileList.size() == 0) {
			textViewEmptyDirectory.setVisibility(View.VISIBLE);
		} else {
			textViewEmptyDirectory.setVisibility(View.INVISIBLE);
		}
		fileListAdapter.notifyDataSetChanged();
	}
	
	protected FileFilter getFileFilter() {
		return new DocumentFileFilter();
	}
	
	public MediaTypeEnum getMediaType() {
		return MediaTypeEnum.DOCUMENT;
	}
	
	public Uri getMediaUri() {
		return MediaStore.Files.getContentUri("external");
	}
	
	private class DocumentFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			boolean retValue = false;
			if(file.isDirectory()) {
				retValue = true;
			} else if(FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.PDF.toString()) 
						|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.DOC.toString())
							|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.DOCX.toString())
								|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.ODT.toString())
									|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.XLS.toString())
										|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.XLSX.toString())
											|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.ODS.toString())
												|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.PPT.toString())
													|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.PPTX.toString())
														|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.HTML.toString())
															|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.RTF.toString())
																|| FileUtil.getFileExtension(file).equalsIgnoreCase(FileTypeEnum.TXT.toString())) {
				retValue = true;
			}
			return retValue;
		}
	}
}
