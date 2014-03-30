package com.trupt.myfiles.persist;

import android.content.Context;

public class DataStorageUtil {
	
	public static void saveToFile(Context context, String fileName, Object objSave) {
		new SaveToFileThread(context, fileName, objSave).start();
	}
	
}
