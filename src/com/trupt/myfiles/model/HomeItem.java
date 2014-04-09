package com.trupt.myfiles.model;

import java.io.Serializable;

import com.trupt.myfiles.model.enums.FragmentNameEnum;


public class HomeItem implements Serializable {
	
	private static final long serialVersionUID = 3894694025565054293L;
	
	String title;
	double size;
	long noOfFiles;
	String originPath;
	FragmentNameEnum fragmentNameEnum;
	int imageResource;
	
	public HomeItem(String title, double size, long noOfFiles,
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
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
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
	
	@Override
	public boolean equals(Object obj) {
		return this.getOriginPath().equals(((HomeItem)obj).getOriginPath());
	}
}
