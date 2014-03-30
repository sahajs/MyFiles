package com.trupt.myfiles.model;

import java.util.ArrayList;

import com.trupt.myfiles.ui.frag.FileFragment;

public class ArrayListSingleton<T> extends ArrayList<T> {

	private static final long serialVersionUID = 8059203348624279838L;
		
	private static ArrayListSingleton<? extends FileFragment> arrayListSingle;
	
	private ArrayListSingleton(){
		
	}
	
	public static ArrayListSingleton<? extends FileFragment> getInstance() {
		if(arrayListSingle == null) {
			arrayListSingle = new ArrayListSingleton<FileFragment>();
		}
		return arrayListSingle;
	}
	
}
