package com.trupt.myfiles.ui.act;

import java.text.ChoiceFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trupt.myfiles.R;
import com.trupt.myfiles.adapter.MenuExpandableListAdapter;
import com.trupt.myfiles.adapter.ScreenSlidePagerAdapter;
import com.trupt.myfiles.common.Constants;
import com.trupt.myfiles.listener.FileBrowseListener;
import com.trupt.myfiles.model.MyFragSingle;
import com.trupt.myfiles.model.enums.FragmentNameEnum;
import com.trupt.myfiles.ui.frag.BaseFragment;
import com.trupt.myfiles.ui.frag.FileFragment;
import com.trupt.myfiles.ui.frag.HomeFragment;
import com.trupt.myfiles.ui.frag.lib.DocumentsFragment;
import com.trupt.myfiles.ui.frag.lib.FavouriteFilesFragment;
import com.trupt.myfiles.ui.frag.lib.MusicFragment;
import com.trupt.myfiles.ui.frag.lib.PicturesFragment;
import com.trupt.myfiles.ui.frag.lib.RecentFilesFragment;
import com.trupt.myfiles.ui.frag.lib.VideosFragment;
import com.trupt.myfiles.ui.frag.storage.InternalStorageFilesFragment;
import com.trupt.myfiles.ui.frag.storage.RootFilesFragment;

public class MainActivity extends FragmentActivity implements FileBrowseListener, 
			OnPageChangeListener, OnChildClickListener, OnGroupClickListener {

	private static final int PERIOD_BACK_PRESSED = 2000;
	
	private long lastPressedTime;
	
	private ExpandableListView exListViewMainMenu;
	private DrawerLayout drawerLayoutMainMenu;
	private MenuDrawableToggle menuDrawableToggle;
	private ViewPager viewPagerMain;
	private LinearLayout linearLayoutMainBottom;
	
	private LinkedHashMap<String, ArrayList<String>> mapMenuItems;
	private ArrayList<String> listMenuHeaders;
	
	private PagerAdapter adapterPager;
	private MenuExpandableListAdapter adapterMenuExpandableListAdapter;
	
	private boolean isDrawerOpen;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawerLayoutMainMenu = (DrawerLayout) findViewById(R.id.drawerLayoutMainMenu);
		//drawerLayoutMainMenu.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		menuDrawableToggle = new MenuDrawableToggle(this, /* host Activity */
				drawerLayoutMainMenu, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open, /* "open drawer" description for accessibility */
				R.string.drawer_close /* "close drawer" description for accessibility */
				);
		
		drawerLayoutMainMenu.setDrawerListener(menuDrawableToggle);
		
		exListViewMainMenu = (ExpandableListView) findViewById(R.id.exListViewMainMenu);
		exListViewMainMenu.setOnChildClickListener(this);
		exListViewMainMenu.setOnGroupClickListener(this);
		mapMenuItems = new LinkedHashMap<String, ArrayList<String>>();
		listMenuHeaders = new ArrayList<String>();
		adapterMenuExpandableListAdapter = new MenuExpandableListAdapter(this, mapMenuItems, listMenuHeaders);
		
		viewPagerMain = (ViewPager) findViewById(R.id.viewPagerMain);
		
		adapterPager = new ScreenSlidePagerAdapter(getSupportFragmentManager(), MyFragSingle.getInstance().getListFragments());
		viewPagerMain.setAdapter(adapterPager);
		viewPagerMain.setOnPageChangeListener(this);
		
		linearLayoutMainBottom = (LinearLayout) findViewById(R.id.linearLayoutSliderBottom);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//System.out.println(this);
		setupViews();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.op_main_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(isDrawerOpen)
			return false;
		else
			return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (menuDrawableToggle.onOptionsItemSelected(item)) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		menuDrawableToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		menuDrawableToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putSerializable("LIST_FRAGMENTS", listFragments);
		outState.putSerializable("LIST_FRAGMENT_NAMES", MyFragSingle.getInstance().getListFragmentNames());
		outState.putInt("CURRENT_FRAGMENT_INDEX", MyFragSingle.getInstance().getCurrentFragmentIndex());
		//outState.putParcelable("VIEW_PAGER_MAIN", viewPagerMain.onSaveInstanceState());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println(this);
		if (savedInstanceState != null) {
			MyFragSingle.getInstance().getListFragmentNames().addAll((ArrayList<String>) savedInstanceState.getSerializable("LIST_FRAGMENT_NAMES"));
			updateMainBottomView();
			MyFragSingle.getInstance().setCurrentFragmentIndex(savedInstanceState.getInt("CURRENT_FRAGMENT_INDEX"));
			viewPagerMain.setCurrentItem(MyFragSingle.getInstance().getCurrentFragmentIndex());
			//viewPagerMain.onRestoreInstanceState(savedInstanceState.getParcelable("VIEW_PAGER_MAIN"));
		}
	}
	
	@Override
	public void onBackPressed() {
		getCurrentFileFragment().onActivityBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
		
	@Override
	public void onFileBrowseCompleted() {
		quitActivity();
	}

	@Override
	public void onFileBrowseCancelled() {
		MyFragSingle myFragSingle = MyFragSingle.getInstance();
		if(myFragSingle.getListFragments().size() > 1) {
			myFragSingle.getListFragments().remove(myFragSingle.getCurrentFragmentIndex());
			myFragSingle.getListFragmentNames().remove(myFragSingle.getCurrentFragmentIndex());
			adapterPager = new ScreenSlidePagerAdapter(getSupportFragmentManager(), myFragSingle.getListFragments());
			viewPagerMain.setAdapter(adapterPager);
			if(myFragSingle.getCurrentFragmentIndex() == myFragSingle.getListFragments().size()) {
				myFragSingle.setCurrentFragmentIndex(myFragSingle.getCurrentFragmentIndex() - 1);
			}
			viewPagerMain.setCurrentItem(myFragSingle.getCurrentFragmentIndex(), true);
			getCurrentFileFragment().updateActionBarItems();
			updateMainBottomView();
		} else {
			quitActivity();
		}
	}
	
	@Override
	public void onNewFileBrowseStart(FragmentNameEnum fragmentNameEnum, String originPath) {
		FileFragment fragment = null;
		switch (fragmentNameEnum) {
			case AllFilesFragment: {
				fragment = new InternalStorageFilesFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.BundleKey.CURRENT_FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
				fragment.setArguments(bundle);
			}
			break;
			case MusicFragment: {
				fragment = new MusicFragment();
			}
			break;
			case VideosFragment: {
				fragment = new VideosFragment();
			}
			break;
			case PicturesFragment: {
				fragment = new PicturesFragment();
			}
			break;
			case DocumentsFragment: {
				fragment = new DocumentsFragment();
			}
			break;
			case RecentFilesFragment: {
				fragment = new RecentFilesFragment();
			}
			break;
			case FavouriteFilesFragment: {
				fragment = new FavouriteFilesFragment();
			}
			break;
		}
		if(fragment != null) {
			fragment.setFileBrowseListener(this);
			updateFragmentListAndDrawer(fragment, 0);
		}
	}
	
	private void quitActivity() {
		if (System.currentTimeMillis() - lastPressedTime < PERIOD_BACK_PRESSED) {
			viewPagerMain = null;
			MyFragSingle myFragSingle = MyFragSingle.getInstance();
			myFragSingle.getListFragments().clear();
			myFragSingle.getListFragmentNames().clear();
			this.finish();
        } else {
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
	        lastPressedTime = System.currentTimeMillis();
        }
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int arg0) {
		MyFragSingle.getInstance().setCurrentFragmentIndex(arg0);
		BaseFragment fragment = getCurrentFileFragment();
		fragment.dismissSelectionMode();
		fragment.updateActionBarItems();
		invalidateOptionsMenu();
		updateMainBottomView();
	}
	
	private void selectItem(int groupPosition, int childPosition) {
		BaseFragment fragment = null;
		switch (groupPosition) {
			case 0: {
				fragment = new HomeFragment();
			}
			break;
			case 1: {
				switch (childPosition) {
					case 0:	{
						fragment = new InternalStorageFilesFragment();
						Bundle bundle = new Bundle();
						bundle.putString(Constants.BundleKey.CURRENT_FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
						fragment.setArguments(bundle);
					}
					break;
					case 1:	{
						fragment = new RootFilesFragment();
						Bundle bundle = new Bundle();
						bundle.putString(Constants.BundleKey.CURRENT_FILE_PATH, "/");
						fragment.setArguments(bundle);
					}
					break;
				}
			}
			break;
			case 2: {
				switch (childPosition) {
					case 0:	{
						fragment = new PicturesFragment();
					}
					break;
					case 1:	{
						fragment = new VideosFragment();
					}
					break;
					case 2: {
						fragment = new MusicFragment();
					}
					break;
					case 3: {
						fragment = new DocumentsFragment();
					}
					break;
				}
			}
			break;
			case 3: {
				fragment = new FavouriteFilesFragment();
			}
			break;
			case 4: {
				fragment = new RecentFilesFragment();
			}
			break;
		}
		
		fragment.setFileBrowseListener(this);
		updateFragmentListAndDrawer(fragment, childPosition);
	}
	
	private void updateFragmentListAndDrawer(BaseFragment fragment, int childPosition) {
		MyFragSingle frag = MyFragSingle.getInstance();
		if(!frag.getListFragmentNames().contains(fragment.getFragmentName())) {
			frag.getListFragments().add(fragment);
			frag.getListFragmentNames().add(fragment.getFragmentName());
			adapterPager.notifyDataSetChanged();
			updateMainBottomView();
			fragment.updateActionBarItems();
		}
		// update selected item and title, then close the drawer
		//exListViewMainMenu.setItemChecked(childPosition, true);
		drawerLayoutMainMenu.closeDrawer(exListViewMainMenu);
		frag.setCurrentFragmentIndex(frag.getListFragmentNames().indexOf(fragment.getFragmentName()));
		viewPagerMain.setCurrentItem(frag.getCurrentFragmentIndex(), true);
	}
	
	private void setupViews() {
		populateMenuItems();
		updateMainBottomView();
		launchHome();
	}
	
	private void populateMenuItems() {
		listMenuHeaders.add("HOME");
		mapMenuItems.put(listMenuHeaders.get(0), new ArrayList<String>(0));
		
		listMenuHeaders.add("MY FILES");
		ArrayList<String> listMyFiles = new ArrayList<String>();
		listMyFiles.add("Internal Storage");
		listMyFiles.add("/");
		mapMenuItems.put(listMenuHeaders.get(1), listMyFiles);
		
		listMenuHeaders.add("LIBRARY");
		ArrayList<String> listLibrary = new ArrayList<String>();
		listLibrary.add("Pictures");
		listLibrary.add("Videos");
		listLibrary.add("Music");
		listLibrary.add("Documents");
		mapMenuItems.put(listMenuHeaders.get(2), listLibrary);
		
		listMenuHeaders.add("FAVOURITES");
		mapMenuItems.put(listMenuHeaders.get(3), new ArrayList<String>(0));
		
		listMenuHeaders.add("RECENT FILES");
		mapMenuItems.put(listMenuHeaders.get(4), new ArrayList<String>(0));
		
		exListViewMainMenu.setAdapter(adapterMenuExpandableListAdapter);
		exListViewMainMenu.expandGroup(0);
	}

	private void updateMainBottomView() {
		linearLayoutMainBottom.removeAllViews();
		for(int i = 0; i < MyFragSingle.getInstance().getListFragments().size(); i++) {
			TextView view = new TextView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
			view.setLayoutParams(params);
			if(MyFragSingle.getInstance().getCurrentFragmentIndex() != i) {
				view.setBackgroundColor(Color.TRANSPARENT);
			} else {
				view.setBackgroundColor(getResources().getColor(R.color.blue));
			}
			linearLayoutMainBottom.addView(view);
		}
	}
	
	private BaseFragment getCurrentFileFragment() {
		return MyFragSingle.getInstance().getListFragments().get(MyFragSingle.getInstance().getCurrentFragmentIndex());
	}
	
	private void launchHome() {
		selectItem(0, 0);
	}
	
	private class MenuDrawableToggle extends ActionBarDrawerToggle {

		public MenuDrawableToggle(Activity activity, DrawerLayout drawerLayout,
				int drawerImageRes, int openDrawerContentDescRes,
				int closeDrawerContentDescRes) {
			super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
					closeDrawerContentDescRes);
			
		}
		
		@Override
		public void onDrawerOpened(View drawerView) {
			super.onDrawerOpened(drawerView);
			getActionBar().setTitle("My Files");
			isDrawerOpen = true;
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			/*MyFragSingle fragments = MyFragSingle.getInstance();
			int index = fragments.getCurrentFragmentIndex();
			exListViewMainMenu.setSelectedChild(2, 0, true);
			//exListViewMainMenu.getChildAt(0).setBackgroundColor(Color.RED);
			exListViewMainMenu.setSelector(R.drawable.selector_list);
			exListViewMainMenu.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
			exListViewMainMenu.setItemChecked(1, true);
			int count = exListViewMainMenu.getCheckedItemCount();
			View view = exListViewMainMenu.getChildAt(3);*/
			
			//View view = new View(getApplicationContext());
			//view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			//view.setBackgroundColor(Color.BLUE);
			//ActionBar actionBar = getActionBar();
			//actionBar.setCustomView(view);
			//actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
		}
		
		@Override
		public void onDrawerClosed(View drawerView) {
			super.onDrawerClosed(drawerView);
			getCurrentFileFragment().updateActionBarItems();
			isDrawerOpen = false;
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			//ActionBar actionBar = getActionBar();
			//actionBar.setCustomView(null);
			//actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
		}
		
		@Override
		public void onDrawerStateChanged(int newState) {
			super.onDrawerStateChanged(newState);
		}
	}


	@Override
	public boolean onChildClick(ExpandableListView listView, View view, int groupPosition,
			int childPosition, long id) {
		selectItem(groupPosition, childPosition);
		return true;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View view,
			int groupPosition, long id) {
			if(groupPosition == 0 || groupPosition == 3 || groupPosition == 4) {
				selectItem(groupPosition, 0);
				return true;
			}
		return false;
	}
	
}