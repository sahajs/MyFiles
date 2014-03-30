package com.trupt.myfiles.persist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class SaveToFileThread extends Thread {
	private Context context;
	private String fileName;
	private Object object;
	
	
	public SaveToFileThread(Context context, String fileName, Object object) {
		super();
		this.context = context;
		this.fileName = fileName;
		this.object = object;
	}

	@Override
	public void run() {
		FileOutputStream fOut = null;
		ObjectOutputStream oOut = null;
		try {
			File file = new File(context.getFilesDir(), fileName);
			fOut = new FileOutputStream(file);
			oOut = new ObjectOutputStream(fOut);
			oOut.writeObject(object);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(fOut != null) {
			try {
				fOut.close();
			} catch (Exception e) {
			}
		}
		if(oOut != null) {
			try {
				oOut.close();
			} catch (Exception e) {
			}
		}
	}
}
