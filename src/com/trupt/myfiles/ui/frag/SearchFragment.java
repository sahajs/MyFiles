package com.trupt.myfiles.ui.frag;

import java.io.File;
import java.util.LinkedHashSet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.trupt.myfiles.R;
import com.trupt.myfiles.adapter.SearchFileListAdapter;
import com.trupt.myfiles.model.FileSortType;
import com.trupt.myfiles.model.SearchCacheManager;
import com.trupt.myfiles.ui.OnFilePathHSVClickListener;
import com.trupt.myfiles.ui.view.LoadingView;
import com.trupt.myfiles.util.FileUtil;

public class SearchFragment extends FileFragment implements OnFilePathHSVClickListener {
		 
	private SearchThread searchThread;
	private SearchHandler searchHandler;
	private String query;
	private boolean isCaseSensitive;
	private LinkedHashSet<File> setSearchResults;
	private String effectivePath = "";
	//flag used to collapse and expand search action provider
	private boolean isSearchViewExpand;
	private LoadingView loadingView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, container,
				false);
		setRetainInstance(true);
		initView(view);
		return view;
	}
		
	@Override
	protected void initView(View view) {
		super.initView(view);
		fileListAdapter = new SearchFileListAdapter(activity, alFileList, isSelectEnable, alSelectedViewIndex);
		listViewFileList.setAdapter(fileListAdapter);
		LinearLayout loadView = (LinearLayout) view.findViewById(R.id.viewLoading);
		loadView.addView(getLoadingView());
		searchHandler = new SearchHandler();
		if(setSearchResults == null) {
			setSearchResults = new LinkedHashSet<File>();
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if(isSearchViewExpand) {
			menu.findItem(R.id.oiSearch).expandActionView();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.oiSearch) {
			isSearchViewExpand = true;
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> aView, View view, int index,
			long arg3) {
		this.setQueryStringToAdapter(null);
		Vibrator vibrator = (Vibrator) SearchFragment.activity
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
					if(currentFilePath.equals("Search \"" + query + "\"")) {
						effectivePath = file.getParent();
					}
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
		if (currentFilePath != null && originFilePath != null && !currentFilePath.equals(originFilePath)) {
			currentFilePath = new File(currentFilePath).getParent();
			if(currentFilePath.equals(effectivePath)) {
				currentFilePath = originFilePath;
			}
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
	
	//empty implementation since setting search title is different
	//use setUpSearchTitle instead.
	@Override
	protected void setUpTitle() {
		if(currentFilePath == null) {
			currentFilePath = "Search";
			originFilePath = currentFilePath;
		}
	}

	protected void setUpViews() {
		super.setUpViews();
	}

	@Override
	protected void populateFileList() {
		textViewEmptyDirectory.setVisibility(View.INVISIBLE);
		if(currentFilePath != null) {
			if(currentFilePath.startsWith("Search")) {
				alFileList.clear();
				alFileList.addAll(setSearchResults);
				this.setQueryStringToAdapter(query);
			} else {
				alFileList = FileUtil.getFilesList(alFileList, currentFilePath, true, FileSortType.NAME_ASC);
			}
			if (alFileList.size() == 0) {
				textViewEmptyDirectory.setVisibility(View.VISIBLE);
			} else {
				textViewEmptyDirectory.setVisibility(View.INVISIBLE);
			}
		
			fileListAdapter.notifyDataSetChanged();
			listViewFileList.setSelection(0);
		}
	}

	protected void populateFilePathView() {
		if(currentFilePath != null && originFilePath != null) {
			horizontalScrollViewFilePath.displaySearchView(currentFilePath.replace(effectivePath, ""), originFilePath);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(loadingView != null) {
			loadingView.stopAnimation();
		}
		stopPreviousSearch();
	}
	
	/**
	 * Handling intent data
	 */
	public void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			Log.e("QUERY", query);
			initSearch();
			if(loadingView != null) {
				loadingView.startAnimation();
			}
			searchThread = new SearchThread();
			searchThread.start();
			SearchCacheManager searchCache = SearchCacheManager.getInstance();
			LinkedHashSet<File> searchFiles = searchCache.getSearchCache(query);
			if(searchFiles != null && !searchFiles.isEmpty()) {
				alFileList.addAll(searchFiles);
				fileListAdapter.notifyDataSetChanged();
			}
		}
	}
		
	@Override
	public void onClick(String filePath) {
		if(!filePath.equals(originFilePath)) {
			filePath = effectivePath + filePath;
		}
		if(!filePath.equalsIgnoreCase(currentFilePath)) {
			currentFilePath = filePath;
			setUpViews();
		}
	}
	
	@Override
	public String getFragmentName() {
		return SearchFragment.class.getName();
	}
	
	private void stopPreviousSearch() {
		if(searchThread != null) {
			if(searchThread.isAlive() == true) {
				searchThread.interrupt();
			}
			searchThread = null;
		}
	}
	
	private void initSearch() {
		stopPreviousSearch();
		isSearchViewExpand = false;
		activity.invalidateOptionsMenu();
		alFileList.clear();
		setSearchResults.clear();
		setUpSearchTitle();
		textViewEmptyDirectory.setVisibility(View.INVISIBLE);
		populateFilePathView();
		this.setQueryStringToAdapter(query);
		fileListAdapter.notifyDataSetChanged();
	}
	
	//Setting up action bar custom view title. New method added since this is used to update whenever handle intent called.
	private void setUpSearchTitle() {
		if(query != null) {
			currentFilePath = "Search \"" + query + "\"";
			originFilePath = currentFilePath;
		}
	}
	
	private void startSearch(final File file) {
		//Log.e("FILE NAME", file.getName());
		String fileName = file.getName();
		String searchQuery = query;
		if(isCaseSensitive == false) {
			fileName = file.getName().toLowerCase();
			searchQuery = query.toLowerCase();
		}
		if(fileName.contains(searchQuery)) {
			Message message = new Message();
			//Bundle bundle = new Bundle();
			//bundle.putString("FILE_NAME", file.getAbsolutePath());
			//message.setData(bundle);
			message.obj = file;
			searchHandler.sendMessage(message);
		}
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			if(files != null) {
				for(File fl : files) {
					startSearch(fl);
				}
			}
		}
	}
	

	protected View getLoadingView() {
		if(loadingView == null) {
			loadingView = new LoadingView(activity);
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			layoutParams.gravity = Gravity.BOTTOM;
			loadingView.setLayoutParams(layoutParams);
		}  
		return loadingView;
	}
	
	
	private class SearchThread extends Thread {
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			Log.e("SEARCH", "search started");
			File file = Environment.getExternalStorageDirectory();
			setSearchResults.clear();
			startSearch(file);
			Log.e("SEARCH", "search finish");
			long endTime = System.currentTimeMillis();
			Log.e("SEARCH", "Time taken: " + (endTime - startTime)/1000 + " sec.");
			
			searchHandler.sendEmptyMessage(0);
		}
	}
	
	
	private class SearchHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.obj != null) {
				if(msg.obj instanceof File) {
					File file = (File) msg.obj;
					//alFileList.add(file);
					setSearchResults.add(file);
					//int size = alFileList.size();
					//if(listViewFileList.getFirstVisiblePosition() + listViewFileList.getChildCount() <= size) {
					//	fileListAdapter.notifyDataSetChanged();
					//}
				}
			} else {
				if(loadingView != null) {
					loadingView.stopAnimation();
				}
				if(!setSearchResults.isEmpty()) {
					SearchCacheManager searchCache = SearchCacheManager.getInstance();
					LinkedHashSet<File> hashSet = new LinkedHashSet<File>(setSearchResults);
					boolean isChange = searchCache.updateSearchCache(query, hashSet);
					if(isChange) {
						populateFileList();
					}
					hashSet = null;
				} else {
					populateFileList();
				}
				Toast.makeText(activity, "Search Completed.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void setQueryStringToAdapter(String queryStr) {
		if(fileListAdapter != null && fileListAdapter instanceof SearchFileListAdapter) {
			((SearchFileListAdapter)fileListAdapter).setQueryString(queryStr);
		}
	}
}
