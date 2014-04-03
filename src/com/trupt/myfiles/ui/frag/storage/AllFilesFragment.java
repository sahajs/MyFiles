package com.trupt.myfiles.ui.frag.storage;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Toast;

import com.trupt.myfiles.common.Constants;
import com.trupt.myfiles.model.FileSortType;
import com.trupt.myfiles.ui.OnFilePathHSVClickListener;
import com.trupt.myfiles.ui.frag.FileFragment;
import com.trupt.myfiles.util.FileUtil;
 
public class AllFilesFragment extends FileFragment implements OnFilePathHSVClickListener {

	private static final int PERIOD_AUTO_REFRESH = 500;
		
	private long lastAutoRefreshTime;
	
	/*private FilePathHorizontalScrollView horizontalScrollViewFilePath;
	
	protected String currentFilePath;
	protected String originFilePath;*/
	protected String previousFilePath;
	
	private FileStorageObserver fileStorageObserver;
	private FileObserverResponseHandler fileObserverResponseHandler;
	
	
	@Override
	protected void initView(View view) {
		super.initView(view);
		/*horizontalScrollViewFilePath = (FilePathHorizontalScrollView) view
				.findViewById(R.id.horizontalScrollViewFilePath);
		horizontalScrollViewFilePath.setHorizontalScrollBarEnabled(false);
		horizontalScrollViewFilePath.setOnFilePathHSVClickListener(this);
		//llFilePath = (LinearLayout) view.findViewById(R.id.llFilePath);*/
		fileObserverResponseHandler = new FileObserverResponseHandler();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("CURRENT_FILE_PATH", currentFilePath);
		outState.putString("ORIGIN_PATH", originFilePath);
		outState.putString("PREVIOUS_FILE_PATH", previousFilePath);
	}
	
	@Override
	protected void restoreSavedInstanceState(Bundle savedInstanceState) {
		super.restoreSavedInstanceState(savedInstanceState);
		if(savedInstanceState != null) {
			currentFilePath = savedInstanceState.getString("CURRENT_FILE_PATH");
			originFilePath = savedInstanceState.getString("ORIGIN_PATH");
			previousFilePath = savedInstanceState.getString("PREVIOUS_FILE_PATH");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> aView, View view, int index,
			long arg3) {
		Vibrator vibrator = (Vibrator) AllFilesFragment.activity
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(15);
		selectedViewIndex = listViewFileList.getFirstVisiblePosition();
		final File file = (File) fileListAdapter.getItem(index);
		
		if (isSelectEnable == false) {
			if (file.isDirectory()) {
				if (!file.canRead() || !file.exists()) {
					Toast.makeText(activity, "Unable to open.",
							Toast.LENGTH_SHORT).show();
				} else {
					fileStorageObserver.stopWatching();
					previousFilePath = currentFilePath;
					currentFilePath = file.getAbsolutePath();
					setUpViews();
				}
			} else {
				openFile(file);
			}
		} else {
			updateView(view, index);
		}
	}

	@Override
	public void onActivityBackPressed() {
		if (!currentFilePath.equals(originFilePath)) {
			currentFilePath = new File(currentFilePath).getParent();
			setUpViews();
			listViewFileList.setSelection(selectedViewIndex);
		} else {
			fileBrowseListener.onFileBrowseCancelled();
		}
	}
	
	@Override
	protected void openFile(File file) {
		super.openFile(file);
		String ext = FileUtil.getFileExtension(file);

		MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
		String mimeType = mimeMap.getMimeTypeFromExtension(ext);

		if (mimeType == null || mimeType.equals("")) {
			mimeType = "*/*";
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.fromFile(file);
		intent.setDataAndType(data, mimeType);
		try {
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(activity, "Application not available.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void setUpTitle() {
		Bundle bundle = getArguments();
		if(currentFilePath == null) {
			currentFilePath = bundle.getString(Constants.BundleKey.CURRENT_FILE_PATH);
			originFilePath = currentFilePath;
			previousFilePath = currentFilePath;
		}
	}

	protected void setUpViews() {
		super.setUpViews();
		lastAutoRefreshTime = 0;
		if(fileStorageObserver != null) {
			fileStorageObserver.stopWatching();
		}
		fileStorageObserver = new FileStorageObserver(currentFilePath);
		fileStorageObserver.startWatching();
	}

	@Override
	protected void populateFileList() {
		alFileList = FileUtil.getFilesList(alFileList, currentFilePath, true, FileSortType.NAME_ASC);

		if (alFileList.size() == 0) {
			textViewEmptyDirectory.setVisibility(View.VISIBLE);
		} else {
			textViewEmptyDirectory.setVisibility(View.INVISIBLE);
		}
	
		fileListAdapter.notifyDataSetChanged();
		listViewFileList.setSelection(0);
	}

	/*private void populateFilePathView() {
		horizontalScrollViewFilePath.displayView(currentFilePath, originFilePath);
	}*/
	
	@Override
	public String getFragmentName() {
		return AllFilesFragment.class.getName();
	}
		
	private class FileObserverResponseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.getData().getString("RESPONSE").equals("DIR_MODIFY")) {
				fileListAdapter.notifyDataSetChanged();
			}
			else if (msg.getData().getString("RESPONSE")
					.equals("DIR_DELETE_SELF")) {
				setUpViews();
			}
		}
	}

	
	private class FileStorageObserver extends FileObserver {

		public FileStorageObserver(String path) {
			super(path, FileObserver.CREATE | FileObserver.MOVED_TO | FileObserver.DELETE | 
					FileObserver.MOVED_FROM | FileObserver.MODIFY | FileObserver.DELETE_SELF |
					FileObserver.MOVE_SELF);
		}

		@Override
		public void onEvent(int event, String path) {
			
			//System.out.println("PATH: " + path + " event: " + event);
			Message message = new Message();
			Bundle bundle = new Bundle();
			switch (event) {
			case FileObserver.CREATE:
			case FileObserver.MOVED_TO:
			{
				Log.e("FO", "create");
				File newFile = new File(currentFilePath + File.separator + path);
				if(newFile.exists()) {
					alFileList.add(newFile);
					bundle.putString("RESPONSE", "DIR_MODIFY");
					message.setData(bundle);
					fileObserverResponseHandler.sendMessage(message);
				}
			}
			break;
			case FileObserver.DELETE:
			case FileObserver.MOVED_FROM:
			{
				Log.e("FO", "delete");
				File newFile = new File(currentFilePath + File.separator + path);
				alFileList.remove(newFile);
				//fileListAdapter.notifyDataSetChanged();
				bundle.putString("RESPONSE", "DIR_MODIFY");
				message.setData(bundle);
				fileObserverResponseHandler.sendMessage(message);
			}
			break;
			case FileObserver.MODIFY:
			{
				if(lastAutoRefreshTime == 0) {
					lastAutoRefreshTime = System.currentTimeMillis();
				}
				File newFile = new File(currentFilePath + File.separator + path);
				
				if (System.currentTimeMillis() - lastAutoRefreshTime < PERIOD_AUTO_REFRESH) {
					for (int i = 0; i < alFileList.size(); i++) {
						if (alFileList.get(i).getAbsolutePath()
								.equals(newFile.getAbsolutePath())) {
							alFileList.set(i, newFile);
							lastAutoRefreshTime = System.currentTimeMillis();
							// fileListAdapter.notifyDataSetChanged();
							bundle.putString("RESPONSE", "DIR_MODIFY");
							message.setData(bundle);
							fileObserverResponseHandler.sendMessage(message);
						}
					}
				}
			}
			break;
			case FileObserver.DELETE_SELF:
			case FileObserver.MOVE_SELF:
			{
				if (!currentFilePath.equals(originFilePath)) {
					currentFilePath = previousFilePath;
					previousFilePath = new File(currentFilePath).getParent();
					bundle.putString("RESPONSE", "DIR_DELETE_SELF");
					message.setData(bundle);
					fileObserverResponseHandler.sendMessage(message);
				} else {
					fileBrowseListener.onFileBrowseCancelled();
				}
			}
			break;
			default:
				break;
			}
		}
	}

	/*@Override
	public void updateActionBarItems() {
		populateFilePathView();
	}*/

	@Override
	public void onClick(String filePath) {
		if(!filePath.equalsIgnoreCase(currentFilePath)) {
			currentFilePath = filePath;
			setUpViews();
		}
	}

}
