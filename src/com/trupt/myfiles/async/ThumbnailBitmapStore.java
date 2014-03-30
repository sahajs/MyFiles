package com.trupt.myfiles.async;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.trupt.myfiles.model.enums.ThumbnailTypeEnum;
import com.trupt.myfiles.util.FileUtil;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

public class ThumbnailBitmapStore {
	
	private static ExecutorService executorService;
	private static ThumbnailBitmapStore thumbnailBitmapStore;
	
	private Vector<FileBitmap> inputList;
	private Vector<FileBitmap> outputList;
	private ThumbnailHandler thumbnailBitmapHandler;
	
	static{
		executorService = Executors.newFixedThreadPool(1);
	}
	
	private ThumbnailBitmapStore() {
		this.inputList = new Vector<ThumbnailBitmapStore.FileBitmap>();
		this.outputList = new Vector<ThumbnailBitmapStore.FileBitmap>();
		this.thumbnailBitmapHandler = new ThumbnailHandler();
	}
	
	public static ThumbnailBitmapStore getInstance() {
		if(thumbnailBitmapStore == null) {
			thumbnailBitmapStore = new ThumbnailBitmapStore();
		}
		return thumbnailBitmapStore;
	}
	
	public void loadThumbnail(File file, int tag, ThumbnailTypeEnum thumbEnum, ThumbnailBitmapStoreListener listener) {
		FileBitmap fileBitmap = new FileBitmap(file, tag, thumbEnum, listener);
		int index = inputList.indexOf(fileBitmap);
		if(index != -1) {
			inputList.remove(index);
		}
		inputList.add(fileBitmap);
		if(inputList.size() == 1) {
			executorService.submit(new ThumbnailRunnable());
			//new Thread(new ThumbnailRunnable()).start();
		}
	}
		
	private Bitmap getImageBitmap(File file) {
		return FileUtil.getImageBitmapThumbnail(file, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	}
	
	private Bitmap getVideoBitmap(File file) {
		return FileUtil.getVideoBitmapThumbnail(file, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
	}
		
	private class ThumbnailRunnable implements Runnable {
		@Override
		public void run() {
			while(inputList.size() > 0) {
				try {
					System.out.println("SIZE: " + inputList.size());
					FileBitmap fileBitmap = inputList.remove(0);
					Bitmap bitmap = null;
					ThumbnailTypeEnum thumbnailTypeEnum = fileBitmap.getThumbnailTypeEnum();
					if(Integer.valueOf(fileBitmap.getTag()).equals(fileBitmap.getThumbnailListener().getThumbnailTag())) {
						switch (thumbnailTypeEnum) {
						case IMAGE:
							bitmap = getImageBitmap(fileBitmap.getFile());
							break;
						case VIDEO:
							bitmap = getVideoBitmap(fileBitmap.getFile());
							break;
						}
					}
					if(bitmap != null) {	
						fileBitmap.setBitmap(bitmap);
						int index = outputList.indexOf(fileBitmap);
						if(index != -1) {
							outputList.remove(index);
						}
						outputList.add(fileBitmap);
						thumbnailBitmapHandler.sendEmptyMessage(0);
					}
				}catch(Exception e){
				}
			}
		}	
	}
	
	private class ThumbnailHandler extends Handler  {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			while(outputList.size() > 0) {
				FileBitmap fileBitmap = outputList.remove(0);
				fileBitmap.getThumbnailListener().onThumbnailLoaded(fileBitmap.getBitmap(), fileBitmap.getTag());
			}
		}
	}
	
	private class FileBitmap {
		private File file;
		private int tag;
		private Bitmap bitmap;
		private ThumbnailTypeEnum thumbnailTypeEnum;
		private ThumbnailBitmapStoreListener thumbnailListener;
		
		public FileBitmap(File file, int tag, ThumbnailTypeEnum thumbEnum, ThumbnailBitmapStoreListener thumbListener) {
			super();
			this.file = file;
			this.tag = tag;
			this.thumbnailTypeEnum = thumbEnum;
			this.thumbnailListener = thumbListener;
		}
		public File getFile() {
			return this.file;
		}
		public Bitmap getBitmap() {
			return this.bitmap;
		}
		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
		public int getTag() {
			return this.tag;
		}
		public ThumbnailBitmapStoreListener getThumbnailListener() {
			return this.thumbnailListener;
		}
		public ThumbnailTypeEnum getThumbnailTypeEnum() {
			return this.thumbnailTypeEnum;
		}
		
		@Override
		public boolean equals(Object obj) {
			return this.file.equals(((FileBitmap)obj).file);
		}
	}
	
	public interface ThumbnailBitmapStoreListener {
		public abstract void onThumbnailLoaded(Bitmap bitmap, int tag);
		public abstract void setThumbnailTag(int tag);
		public abstract int getThumbnailTag();
	}
	
}
