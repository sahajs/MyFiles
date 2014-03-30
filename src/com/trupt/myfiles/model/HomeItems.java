package com.trupt.myfiles.model;

import com.trupt.myfiles.model.enums.FragmentNameEnum;


public class HomeItems {
	
	String title;
	long size;
	long noOfFiles;
	String originPath;
	FragmentNameEnum fragmentNameEnum;
	int imageResource;
	
	public HomeItems(String title, long size, long noOfFiles,
			String originPath, FragmentNameEnum fragmentNameEnum,
			int imageResource) {
		super();
		this.title = title;
		this.size = size;
		this.noOfFiles = noOfFiles;
		this.originPath = originPath;
		this.fragmentNameEnum = fragmentNameEnum;
		this.imageResource = imageResource;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getNoOfFiles() {
		return noOfFiles;
	}
	public void setNoOfFiles(long noOfFiles) {
		this.noOfFiles = noOfFiles;
	}
	public String getOriginPath() {
		return originPath;
	}
	public void setOriginPath(String originPath) {
		this.originPath = originPath;
	}
	public FragmentNameEnum getFragmentNameEnum() {
		return fragmentNameEnum;
	}
	public void setFragmentNameEnum(FragmentNameEnum fragmentNameEnum) {
		this.fragmentNameEnum = fragmentNameEnum;
	}
	public int getImageResource() {
		return imageResource;
	}
	public void setImageResource(int imageResource) {
		this.imageResource = imageResource;
	}
}
