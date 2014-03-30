package com.trupt.myfiles.ui.frag.lib;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.trupt.myfiles.common.Constants;
import com.trupt.myfiles.model.enums.MediaTypeEnum;
import com.trupt.myfiles.ui.frag.FileFragment;
import com.trupt.myfiles.util.FileUtil;


public class LibraryFragment extends FileFragment implements OnItemClickListener, OnItemLongClickListener {
			
		protected int numFiles;		
											
		protected void openFile(File file) {
			super.openFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri data = Uri.parse("file://");
			Log.e("LIB FRAG", data.getPath());
			data = Uri.withAppendedPath(data, file.getAbsolutePath());
			intent.setDataAndType(data, FileUtil.getMimeType(file));
			try {
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Application not available.",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void populateFileList() {
			String[] projection = new String[]{ MediaStore.Images.Media.DATA };
			Uri uri = getMediaUri();
			
			Cursor cur = MediaStore.Images.Media.query(getActivity().getContentResolver(), uri, projection, null, MediaStore.Images.Media.TITLE);
			if (cur.moveToFirst()) {
				int dataCol = cur.getColumnIndex(MediaStore.Images.Media.DATA);
		        do {
		        	String data = cur.getString(dataCol);		            
		            alFileList.add(new File(data));
		        } while (cur.moveToNext());
		    }
			
			if (alFileList.size() == 0) {
				textViewEmptyDirectory.setVisibility(View.VISIBLE);
			} else {
				textViewEmptyDirectory.setVisibility(View.INVISIBLE);
			}
			fileListAdapter.notifyDataSetChanged();
		}
		
		@Override
		public String getFragmentName() {
			return LibraryFragment.class.getName();
		}

		public MediaTypeEnum getMediaType() {
			return null;
		}
		
		public Uri getMediaUri() {
			return null;
		}
			
		
}
