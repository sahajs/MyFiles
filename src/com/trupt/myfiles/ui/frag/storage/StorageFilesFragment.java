package com.trupt.myfiles.ui.frag.storage;



public class StorageFilesFragment extends AllFilesFragment {
	
	public static StorageFilesFragment getInstance(String filePath) {
		StorageFilesFragment storageFilesFragment = new StorageFilesFragment();
		storageFilesFragment.currentFilePath = filePath;
		storageFilesFragment.originFilePath = storageFilesFragment.currentFilePath;
		storageFilesFragment.previousFilePath = storageFilesFragment.currentFilePath;
		return storageFilesFragment;
	}
	
	public String getFragmentName() {
		return StorageFilesFragment.class.getName() + originFilePath;
	}
	
	
}
