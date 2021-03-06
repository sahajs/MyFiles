package com.trupt.myfiles.ui.frag;

import java.io.File;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.trupt.myfiles.R;
import com.trupt.myfiles.core.Global;
import com.trupt.myfiles.model.FavouriteFileManager;
import com.trupt.myfiles.model.HomeItem;
import com.trupt.myfiles.model.HomeItemsManager;
import com.trupt.myfiles.model.NumberNSize;
import com.trupt.myfiles.model.RecentFileManager;
import com.trupt.myfiles.model.enums.FragmentNameEnum;
import com.trupt.myfiles.ui.FilePathHorizontalScrollView;
import com.trupt.myfiles.util.FileUtil;

public class HomeFragment extends BaseFragment implements
		OnItemClickListener {
	 
	private GridView gridViewHomeItems;
	private HomeItemsAdapter adapterhomeItems;
	private ArrayList<HomeItem> listHomeItems;
	protected FilePathHorizontalScrollView horizontalScrollViewFilePath;
	
	protected String currentFilePath;
	protected String originFilePath;
	
	protected boolean isFirstTime = true;
	
	public static HomeFragment getInstance() {
		HomeFragment homeFragment = new HomeFragment();
		homeFragment.currentFilePath = "Home";
		homeFragment.originFilePath = homeFragment.currentFilePath;
		return homeFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container,
				false);
		
		setRetainInstance(true);

		gridViewHomeItems = (GridView) view.findViewById(R.id.gridViewHomeItems);
		gridViewHomeItems.setOnItemClickListener(this);

		listHomeItems = new ArrayList<HomeItem>();
		adapterhomeItems = new HomeItemsAdapter(listHomeItems, activity);
		gridViewHomeItems.setAdapter(adapterhomeItems);
		
		if(horizontalScrollViewFilePath == null) {
			horizontalScrollViewFilePath = new FilePathHorizontalScrollView(activity);
			horizontalScrollViewFilePath.setHorizontalScrollBarEnabled(false);
			horizontalScrollViewFilePath.setTag(getFragmentName());
		}
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
				
		restoreSavedInstanceState(savedInstanceState);
		if(isFirstTime == true) {
			updateActionBarItems();
		}
		
		setUpTitle();
		setUpViews();
		isFirstTime = false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onActivityBackPressed();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	protected void restoreSavedInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
	
		}
	}

	@Override
	public void onItemClick(AdapterView<?> aView, View view, int index,
			long arg3) {
		Vibrator vibrator = (Vibrator) HomeFragment.activity
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(15);
		HomeItem homeItems = (HomeItem) gridViewHomeItems.getItemAtPosition(index);
		fileBrowseListener.onNewFileBrowseStart(homeItems.getFragmentNameEnum(), homeItems.getOriginPath());
	}

	@Override
	public void onActivityBackPressed() {
		fileBrowseListener.onFileBrowseCompleted();
	}
		
	@Override
	public void dismissSelectionMode() {	
		ActionMode actionMode = Global.getActionMode();
		if(actionMode != null) {
			actionMode.finish();
		}
	}
	
	protected void setUpTitle() {
		currentFilePath = "Home";
		originFilePath = currentFilePath;
	}

	protected void setUpViews() {
		populateFileList();
		populateFilePathView();
	}

	private void populateFileList() {
		/*HomeItems item1 = new HomeItems("All Files", 0, 0, "/storage/emulated/0", FragmentNameEnum.AllFilesFragment, R.drawable.ic_my_files);
		HomeItems item2 = new HomeItems("Favourites", 0, 0, "", FragmentNameEnum.FavouriteFilesFragment, R.drawable.ic_my_favorites);
		HomeItems item3 = new HomeItems("Pictures", 0, 0, "", FragmentNameEnum.PicturesFragment, R.drawable.ic_my_pictures);
		HomeItems item4 = new HomeItems("Videos", 0, 0, "", FragmentNameEnum.VideosFragment, R.drawable.ic_my_videos);
		HomeItems item5 = new HomeItems("Music", 0, 0, "", FragmentNameEnum.MusicFragment, R.drawable.ic_my_music);
		HomeItems item6 = new HomeItems("Documents", 0, 0, "", FragmentNameEnum.DocumentsFragment, R.drawable.ic_my_documents);
		HomeItems item7 = new HomeItems("Downloaded Apps", 0, 0, "", FragmentNameEnum.AllFilesFragment, R.drawable.ic_my_downloaded_apps);
		HomeItems item8 = new HomeItems("Recent Files", 0, 0, "", FragmentNameEnum.RecentFilesFragment, R.drawable.ic_my_recent_files);
		listHomeItems.add(item1);
		listHomeItems.add(item2);
		listHomeItems.add(item3);
		listHomeItems.add(item4);
		listHomeItems.add(item5);
		listHomeItems.add(item6);
		listHomeItems.add(item7);
		listHomeItems.add(item8);*/
		HomeItemsManager homeItemsManager = HomeItemsManager.getInstance();
		listHomeItems.addAll(homeItemsManager.getHomeItemsList());
		adapterhomeItems.notifyDataSetChanged();
		gridViewHomeItems.setSelection(0);
		/*for(HomeItems item : listHomeItems) {
			switch(item.getFragmentNameEnum()) {
				case AllFilesFragment: {
					item.setSize(getDirectorySize());
				}
				break;
				default: {
					item.setSize(getMediaSize());
				}
				break;
			}
		}*/
		new HomeItemsSizeAsyncTask().execute();
	}
	
	protected void populateFilePathView() {
		if(currentFilePath != null && originFilePath != null) {
			horizontalScrollViewFilePath.displayView(currentFilePath, originFilePath);
		}
	}
	
	@Override
	public String getFragmentName() {
		return HomeFragment.class.getName();
	}
	
	@Override
	public void updateActionBarItems() {
		if(horizontalScrollViewFilePath != null) {
			ActionBar actionBar = activity.getActionBar();
			if(actionBar.getCustomView() != horizontalScrollViewFilePath) {
				Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
				horizontalScrollViewFilePath.setAnimation(animation);
				activity.getActionBar().setCustomView(horizontalScrollViewFilePath);
				activity.getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
			}
		}
	}
	
	private class HomeItemsAdapter extends BaseAdapter {

		ArrayList<HomeItem> listItems;
		Context context;
		View view;

		public HomeItemsAdapter(ArrayList<HomeItem> homeItems, Context context) {
			super();
			this.listItems = homeItems;
			this.context = context;
		}

		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			view = convertView;
			if (view == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(
						R.layout.gridview_home_items_fragment, null);
			}
			
			ImageView imageViewImage = (ImageView) view.findViewById(R.id.ivItemImage);
			TextView textViewTitle = (TextView) view.findViewById(R.id.tvTitle);
			TextView textViewNumFiles = (TextView) view.findViewById(R.id.tvNoOfFiles);
			TextView textViewSize = (TextView) view.findViewById(R.id.tvSize);
			
			final HomeItem item = listItems.get(position);
			imageViewImage.setImageResource(item.getImageResource());
			textViewTitle.setText(item.getTitle());
			if(item.getNoOfFiles() != 0) {
				String str = " Files";
				if(item.getNoOfFiles() == 1) {
					str = " File";
				}
				textViewNumFiles.setText(item.getNoOfFiles() + str);
			}
			textViewSize.setText(FileUtil.formatSize(item.getSize()));
			
			return view;
		}
	}
	
	private class HomeItemsSizeAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			for(int i = 0; i < listHomeItems.size(); i++) {
				HomeItem item = listHomeItems.get(i);
				FragmentNameEnum fEnum = item.getFragmentNameEnum();
				NumberNSize numberNSize = null;
				switch (fEnum) {
					case StorageFilesFragment: {
						if(item.getOriginPath() != null) {
							File file = new File(item.getOriginPath());
							if(file != null && file.canRead()) {
								StatFs statFs = new StatFs(item.getOriginPath());
								double size = 0;
								if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
									long blkCnt = statFs.getBlockCount();
									long blkFree = statFs.getFreeBlocks();
									size = (blkCnt - blkFree) * statFs.getBlockSize();
								} else {
									long blkCnt = statFs.getBlockCountLong(); 
									long blkFree = statFs.getFreeBlocksLong();
									size = (blkCnt - blkFree) * statFs.getBlockSizeLong();
								}
								numberNSize = new NumberNSize(size, 0);
							}
						}
					}
					break;
					
					case MusicFragment: {
						Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
						numberNSize = FileUtil.getNumberNSize(uri);
					}
					break;
					case PicturesFragment: {
						Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
						numberNSize = FileUtil.getNumberNSize(uri);
					}
					break;
					case VideosFragment: {
						Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
						numberNSize = FileUtil.getNumberNSize(uri);
					}
					break;
					case DocumentsFragment: {
						Uri uri = MediaStore.Files.getContentUri("external");
						numberNSize = FileUtil.getNumberNSize(uri);
					}
					break;
					case RecentFilesFragment: {
						numberNSize = RecentFileManager.getInstance().getNumberNSize();
					}
					break;
					case FavouriteFilesFragment: {
						numberNSize = FavouriteFileManager.getInstance().getNumberNSize();
					}
					break;
					
				}
				if(numberNSize != null) {
					item.setSize(numberNSize.getSize());
					item.setNoOfFiles(numberNSize.getNumFiles());
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapterhomeItems.notifyDataSetChanged();
		}
		
	}
}
