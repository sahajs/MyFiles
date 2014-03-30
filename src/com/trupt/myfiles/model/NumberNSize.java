package com.trupt.myfiles.model;

public class NumberNSize {
	private long size;
	private long numFiles;
	
	public NumberNSize(long size, long numFiles) {
		super();
		this.size = size;
		this.numFiles = numFiles;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getNumFiles() {
		return numFiles;
	}
	public void setNumFiles(long numFiles) {
		this.numFiles = numFiles;
	}

}
