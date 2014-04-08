package com.trupt.myfiles.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.trupt.myfiles.R;
import com.trupt.myfiles.app.MyFilesApplication;
import com.trupt.myfiles.model.FileSortType;
import com.trupt.myfiles.model.NumberNSize;
import com.trupt.myfiles.model.enums.FileTypeEnum;

public class FileUtil {

	public static ArrayList<String> getMountedStorageList() {
		ArrayList<String> listStorage = new ArrayList<String>(3);
		File file = new File("/mnt");
		File[] files = file.listFiles();
		for(File fl : files) {
			if(fl.canRead() && !fl.isHidden()) {
				listStorage.add(fl.getAbsolutePath());
			}
		}
		return listStorage;
	}

	public static FileTypeEnum getFileType(File file) {
		String fileExt = getFileExtension(file);
		FileTypeEnum fileTypeEnum = FileTypeEnum.UNKNOWN;
		try {
			fileTypeEnum = FileTypeEnum.valueOf(fileExt.toUpperCase());
		} catch (IllegalArgumentException exception) {
		}
		return fileTypeEnum;
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		String fileExt = "";
		int lastDot = fileName.lastIndexOf(".");
		if (lastDot != -1) {
			fileExt = fileName.substring(lastDot + 1, fileName.length());
		}
		return fileExt;
	}
	
	public static String getMimeType(File file) {
		String mimeType = null;
		String ext = FileUtil.getFileExtension(file);
		if(ext != null) {
			MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
			mimeType = mimeTypeMap.getMimeTypeFromExtension(ext.toLowerCase(Locale.US));
		}
		return mimeType;
	}

	//TODO remove if else with switch case
	public static Bitmap getImageIconBitmap(File file) {
		Bitmap imageDrawable = getImageIcon(file);
		FileTypeEnum fileTypeEnum = FileUtil.getFileType(file);
		switch (fileTypeEnum) {
			case JPG:
			case JPEG:
			case PNG:
			case GIF:
				imageDrawable = getImageBitmapThumbnail(file, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				if(imageDrawable == null) {
					Resources resources = MyFilesApplication.getAppContext().getResources();
					imageDrawable = BitmapFactory.decodeResource(resources, R.drawable.ic_file_image);
				}
				break;
			case WEBM:
			case MKV:
			case FLV:
			case AVI:
			case WMV:
			case MP4:
			case GP:
				imageDrawable = getVideoBitmapThumbnail(file, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
				if(imageDrawable == null) {
					Resources resources = MyFilesApplication.getAppContext().getResources();
					imageDrawable = BitmapFactory.decodeResource(resources, R.drawable.ic_file_video);
				}
				break;
		}
		return imageDrawable;
	}
	
	public static Bitmap getImageIcon(File file) {
		int resId = R.drawable.ic_file_unknown;
		FileTypeEnum fileTypeEnum = FileUtil.getFileType(file);
		Resources resources = MyFilesApplication.getAppContext().getResources();
		if (file.isDirectory() == true) {
			resId = R.drawable.ic_directory;
		} else {
			switch (fileTypeEnum) {
				case PDF:
					resId = R.drawable.ic_file_pdf;
					break;
				case TXT:
				case RTF:
					resId = R.drawable.ic_file_text;
					break;
				case HTML:
				case XML:
					resId = R.drawable.ic_file_html;
					break;
				case DOC:
				case DOCX:
				case ODF:
					resId = R.drawable.ic_file_word;
					break;
				case XLS:
				case XLSX:
				case ODS:
					resId = R.drawable.ic_file_spreadsheet;
					break;
				case PPT:
				case PPTX:
				case ODT:
					resId = R.drawable.ic_file_presentation;
					break;
				case ZIP:
				case RAR:
					resId = R.drawable.ic_file_zip;
					break;
				case JPG:
				case JPEG:
				case PNG:
				case GIF:
					resId = R.drawable.ic_file_image;
					break;
				case WEBM:
				case MKV:
				case FLV:
				case AVI:
				case WMV:
				case MP4:
				case GP:
					resId = R.drawable.ic_file_video;
					break;
				case WAV:
				case OGG:
				case OTA:
				case IMY:
				case RTTL:
				case XMF:
				case MXMF:
				case MP3:
				case FLAC:
				case M4A:
				case AAC:
					resId = R.drawable.ic_file_music;
					break;
				case APK:
					resId = R.drawable.ic_file_apk;
					break;
				default:
					resId = R.drawable.ic_file_unknown;
					break;	
			}
		}
		Bitmap imageDrawable = BitmapFactory.decodeResource(resources, resId);
		return imageDrawable;
	}
	
	//TODO: move these db call to database util
	public static Bitmap getImageBitmapThumbnail(File file, Uri uri) {
		Bitmap image = null;
		try {
		String[] projection = new String[]{ MediaStore.Images.Media._ID };
		String whereArgs = new String( MediaStore.Images.Media.DATA + "='" + file.getAbsolutePath() + "'" );
		
		Cursor cur = MediaStore.Images.Media.query(MyFilesApplication.getAppContext().getContentResolver(), uri, projection, whereArgs, MediaStore.Images.Media.TITLE);
		if (cur.moveToFirst()) {
			int idCol = cur.getColumnIndex(MediaStore.Images.Media._ID);
	        long id = cur.getLong(idCol);
	        image = MediaStore.Images.Thumbnails.getThumbnail(MyFilesApplication.getAppContext().getContentResolver(), 
				id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
		}
		cur.close();
		} catch(SQLiteException exception) {
		}
		return image;
	}
	
	//TODO: move these db call to database util
		public static Bitmap getVideoBitmapThumbnail(File file, Uri uri) {
			Bitmap image = null;
			try {
				String[] projection = new String[]{ MediaStore.Video.Media._ID };
				String whereArgs = new String( MediaStore.Video.Media.DATA + "='" + file.getAbsolutePath() + "'" );
				
				Cursor cur = MediaStore.Images.Media.query(MyFilesApplication.getAppContext().getContentResolver(), uri, projection, whereArgs, MediaStore.Images.Media.TITLE);
				if (cur.moveToFirst()) {
					int idCol = cur.getColumnIndex(MediaStore.Video.Media._ID);
			        long id = cur.getLong(idCol);
			        image = MediaStore.Video.Thumbnails.getThumbnail(MyFilesApplication.getAppContext().getContentResolver(), 
						id, MediaStore.Video.Thumbnails.MICRO_KIND, null);
				}
				cur.close();
			} catch(SQLiteException exception) {
			}
			return image;
		}
	
	public static NumberNSize getNumberNSize(Uri uri) {
		String[] projection = new String[]{MediaStore.Images.Media.SIZE};
		String whereArgs = null;
		if(uri.equals(MediaStore.Files.getContentUri("external"))) {
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
			StringBuilder args = new StringBuilder();
			for(String mimeType : mimeTypes) {
				args.append( MediaStore.Images.Media.MIME_TYPE + "='" + mimeType + "' OR ");			
			}
			args.delete(args.length() - 4, args.length());
			whereArgs = args.toString();
		}
		Cursor cur = MediaStore.Images.Media.query(MyFilesApplication.getAppContext().getContentResolver(), uri, projection, whereArgs, MediaStore.Images.Media.TITLE);
		long numFiles = cur.getCount();
		long size = 0;
		if (cur.moveToFirst()) {
			do {
	            size = size + cur.getLong(0);
	        } while (cur.moveToNext());
		}
		return new NumberNSize(size, numFiles);
	}
	
	/*public static NumberNSize getNumberNSize(File file) {
		long size = file.length();
		long numFiles = 1;
		NumberNSize numberNSize = new NumberNSize(size, numFiles);
		File[] files = file.listFiles();
		if(files != null) {
			for(File fl : files) {
				NumberNSize n = getNumberNSize(fl);
				numberNSize.setNumFiles(n.getNumFiles() + numFiles);
				numberNSize.setSize(n.getSize() + size);
			}
		}
		return numberNSize;
	}*/
	
	/*public static String getFileSize(File file) {
		String sizeString = null;
		int value = 1024;
		DecimalFormat decimalFormat = new DecimalFormat();
	    decimalFormat.setMinimumFractionDigits(0);
	    decimalFormat.setMaximumFractionDigits(2);
		if (file.isDirectory()) {
			sizeString = "";
		} else {
			float bytes = file.length();
			if ((long)bytes < value) {
				sizeString = decimalFormat.format(bytes) + " B";
			} else if ((long)bytes / value < value) {
				float kb = 0;
				while ((long)bytes / value > 0) {
					kb += bytes / value;
					bytes = bytes / value;
				}
				sizeString = decimalFormat.format(kb) + " KB";
			} else if ((long)bytes / (value * value) < (value)) {
				float mb = 0;
				while ((long)bytes / (value * value) > 0) {
					mb += bytes / (value * value);
					bytes = bytes / (value * value);
				}
				sizeString = decimalFormat.format(mb) + " MB";
			} else {
				float gb = 0;
				while ((long)bytes / (value * value * value) > 0) {
					gb += bytes / (value * value * value);
					bytes = bytes / (value * value * value);
				}
				sizeString = decimalFormat.format(gb) + " GB";
			}
		}

		return sizeString;
	}*/
	
	public static String formatSize(double size) {
		String retVal = "";
		String suffix = "KMGTPE";
		int unit = 1024;
		if (size < unit) return size + " B";

		int exp = (int) (Math.log(size) / Math.log(unit));
		char ch = suffix.charAt(exp - 1);
		retVal = String.format("%.2f %sB", size / Math.pow(unit, exp), ch);
		return retVal;
		/*String sizeString = null;
		int value = 1024;
		DecimalFormat decimalFormat = new DecimalFormat();
	    decimalFormat.setMinimumFractionDigits(0);
	    decimalFormat.setMaximumFractionDigits(2);
		
			float bytes = size;
			if ((long)bytes < value) {
				sizeString = decimalFormat.format(bytes) + " B";
			} else if ((long)bytes / value < value) {
				float kb = 0;
				while ((long)bytes / value > 0) {
					kb += bytes / value;
					bytes = bytes / value;
				}
				sizeString = decimalFormat.format(kb) + " KB";
			} else if ((long)bytes / (value * value) < (value)) {
				float mb = 0;
				while ((long)bytes / (value * value) > 0) {
					mb += bytes / (value * value);
					bytes = bytes / (value * value);
				}
				sizeString = decimalFormat.format(mb) + " MB";
			} else {
				float gb = 0;
				while ((long)bytes / (value * value * value) > 0) {
					gb += bytes / (value * value * value);
					bytes = bytes / (value * value * value);
				}
				sizeString = decimalFormat.format(gb) + " GB";
			}
		return sizeString;*/
	}
	
	public static ArrayList<File> getFilesList(
			ArrayList<File> listAllFiles, String path, boolean isHiddenAlso, FileSortType sortType) {
		
		listAllFiles.clear();
		ArrayList<File> listFolder = null;
		ArrayList<File> listFile = null;
		
		if (path != null) {		
			File startFile = new File(path);
			
			if(startFile != null) {
				ArrayList<File> listContents = new ArrayList<File>();
				listContents.addAll(Arrays.asList(startFile.listFiles()));
		
				if (listContents.size() != 0) {
					for(File file : listContents) {
						if(isHiddenAlso || !file.isHidden()) {
							if(file.isDirectory()) {
								if(listFolder == null) {
									listFolder = new ArrayList<File>();
								}
								listFolder.add(file);
							} else {
								if(listFile == null) {
									listFile = new ArrayList<File>();
								}
								listFile.add(file);
							}
						}
					}
					if(listFolder != null) {
						Collections.sort(listFolder, ComparatorUtil.getComparator(sortType));
						listAllFiles.addAll(listFolder);
					}
					if(listFile != null) {
						Collections.sort(listFile, ComparatorUtil.getComparator(sortType));
						listAllFiles.addAll(listFile);
					}
				}	
			}
		}
		return listAllFiles;
	}	

	public static LinkedHashMap<String, ArrayList<File>> getFilesMap(
			LinkedHashMap<String, ArrayList<File>> mapFiles, String path, boolean isHiddenAlso, FileSortType sortType) {
		
		mapFiles.clear();
		ArrayList<File> listFolder = null;
		ArrayList<File> listFile = null;
		
		if (path == null) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}

		File startFile = new File(path);

		ArrayList<File> listContents = new ArrayList<File>();
		listContents.addAll(Arrays.asList(startFile.listFiles()));

		if (listContents.size() != 0) {
			for(File file : listContents) {
				if(isHiddenAlso || !file.isHidden()) {
					if(file.isDirectory()) {
						if(listFolder == null) {
							listFolder = new ArrayList<File>();
						}
						listFolder.add(file);
					} else {
						if(listFile == null) {
							listFile = new ArrayList<File>();
						}
						listFile.add(file);
					}
				}
			}
			if(listFolder != null) {
				Collections.sort(listFolder, ComparatorUtil.getComparator(sortType));
				mapFiles.put("Folder", listFolder);
			}
			if(listFile != null) {
				Collections.sort(listFile, ComparatorUtil.getComparator(sortType));
				mapFiles.put("File", listFile);
			}
		}	
		return mapFiles;
	}	
	
	public static String getCommonMimeType(ArrayList<File> files) {
		return "*/*";
	}
}
