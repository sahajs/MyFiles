package com.trupt.myfiles.model;

import java.io.File;
import java.io.Serializable;

public class RecentFile implements Serializable {

	private File file;
	private long time;
	public RecentFile(File file, long time) {
		super();
		this.file = file;
		this.time = time;
	}
	public File getFile() {
		return this.file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public long getTime() {
		return this.time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.file.equals(((RecentFile)o).file);
	}
}
