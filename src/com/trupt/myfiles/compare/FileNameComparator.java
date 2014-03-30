package com.trupt.myfiles.compare;

import java.io.File;
import java.util.Comparator;

public class FileNameComparator implements Comparator<File> {
	boolean isReverse;
	
	public FileNameComparator(boolean isReverse) {
		this.isReverse = isReverse;
	}
	
	@Override
	public int compare(File lhs, File rhs) {
		int retValue = 0;
		if(!isReverse) {
			retValue = lhs.getName().compareToIgnoreCase(rhs.getName());
		} else {
			retValue = rhs.getName().compareToIgnoreCase(lhs.getName());
		}
		return retValue;
	}
}
