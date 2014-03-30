package com.trupt.myfiles.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.trupt.myfiles.ui.frag.BaseFragment;

/**
 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	
	ArrayList<BaseFragment> listFragments;
	
    public ScreenSlidePagerAdapter(FragmentManager fragmentManager, ArrayList<BaseFragment> listFragments) {
        super(fragmentManager);
        this.listFragments = listFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }
    
    @Override
    public int getItemPosition(Object object) {
    	return POSITION_NONE;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
    	return listFragments.get(position).getFragmentName();
    }

}
