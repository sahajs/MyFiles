package com.trupt.myfiles.model;

public class NumberNSize {
	private double size;
	private long numFiles;
	
	public NumberNSize(double size, long numFiles) {
		super();
		this.size = size;
		this.numFiles = numFiles;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public long getNumFiles() {
		return numFiles;
	}
	public void setNumFiles(long numFiles) {
		this.numFiles = numFiles;
	}

}
