package com.trupt.myfiles.app;

import android.app.Application;
import android.content.Context;

public class MyFilesApplication extends Application {

	private static MyFilesApplication application;
	
	@Override
	public void onCreate() {
		super.onCreate();
		MyFilesApplication.application = this;
	}
	
	public static Context getAppContext() {
		return application.getApplicationContext();
	}
	
}
