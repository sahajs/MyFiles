package com.trupt.myfiles.model;

import java.util.ArrayList;

import com.trupt.myfiles.ui.frag.BaseFragment;

public class MyFragSingle {
	
	private static MyFragSingle myFragSingle;
	
	private ArrayList<BaseFragment> listFragments;
	private ArrayList<String> listFragmentNames;
	private int currentFragmentIndex;
	
	private MyFragSingle() {
		listFragments = new ArrayList<BaseFragment>();
		listFragmentNames = new ArrayList<String>();
		currentFragmentIndex = 0;
	}
	
	public static MyFragSingle getInstance() {
		if(myFragSingle == null) {
			myFragSingle = new MyFragSingle();
		}
		return myFragSingle;
	}

	public ArrayList<BaseFragment> getListFragments() {
		return listFragments;
	}

	public void setListFragments(ArrayList<BaseFragment> listFragments) {
		this.listFragments = listFragments;
	}

	public ArrayList<String> getListFragmentNames() {
		return listFragmentNames;
	}

	public void setListFragmentNames(ArrayList<String> listFragmentNames) {
		this.listFragmentNames = listFragmentNames;
	}

	public int getCurrentFragmentIndex() {
		return currentFragmentIndex;
	}

	public void setCurrentFragmentIndex(int currentFragmentIndex) {
		this.currentFragmentIndex = currentFragmentIndex;
	}
}
