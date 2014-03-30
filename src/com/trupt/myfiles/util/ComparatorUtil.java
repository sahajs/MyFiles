package com.trupt.myfiles.util;

import java.io.File;
import java.util.Comparator;

import com.trupt.myfiles.compare.FileNameComparator;
import com.trupt.myfiles.model.FileSortType;

public class ComparatorUtil {

	public static Comparator<? super File> getComparator(FileSortType type) {
		Comparator<? super File> comparator = null;
		boolean isReverse = false;
		switch (type) {
			case NAME_DEC: {
				isReverse = true;
			}
			case NAME_ASC: {
				comparator = new FileNameComparator(isReverse);
			}
			default:
				break;
		}
		return comparator;
	}
}
