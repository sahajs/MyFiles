package com.trupt.myfiles.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trupt.myfiles.R;
import com.trupt.myfiles.model.enums.ThumbnailTypeEnum;
import com.trupt.myfiles.ui.view.ThumbnailImageView;
import com.trupt.myfiles.util.FileUtil;

public class SearchFileListAdapter extends FileListAdapter {

	private String queryString;
	 
	private View view; 
	
	public SearchFileListAdapter(Context context, ArrayList<File> fileList,
			boolean isSelectEnable, ArrayList<Integer> listSelectedViewIndex) {
		super(context, fileList, isSelectEnable, listSelectedViewIndex);
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(
					R.layout.listview_file_browse_fragment, null);
		}
		
		TextView tvFileName = (TextView) view
				.findViewById(R.id.tvFileName);
		TextView tvFileSize = (TextView) view
				.findViewById(R.id.tvFileSize);
		TextView tvFileCreationTime = (TextView) view
				.findViewById(R.id.tvFileCreationTime);
		ThumbnailImageView ivFileTypeImage = (ThumbnailImageView) view
				.findViewById(R.id.ivFileTypeImage);
		
		final File file = fileList.get(position);

		if(queryString == null || queryString.equals("")) {
			tvFileName.setText(file.getName());
		} else {
			String fileName = file.getName();
			int indexOfQeryStr = fileName.toLowerCase(Locale.US).indexOf(queryString.toLowerCase(Locale.US));
			String text = null;
			if(indexOfQeryStr < fileName.length() - queryString.length()) {
				text = fileName.substring(0, indexOfQeryStr) + "<font color=#cc0029>"+fileName.substring(indexOfQeryStr, indexOfQeryStr + queryString.length())+"</font>" + fileName.substring(indexOfQeryStr + queryString.length());
			} else {
				text = fileName.substring(0, indexOfQeryStr) + "<font color=#cc0029>"+fileName.substring(indexOfQeryStr, indexOfQeryStr + queryString.length())+"</font>";
			}
			tvFileName.setText(Html.fromHtml(text));
		}
	
		if(file.isDirectory()) {
			tvFileSize.setText("");
		} else {
			tvFileSize.setText(Formatter.formatFileSize(context, file.length()));
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"dd MMM yyyy hh:mm", Locale.US);
		tvFileCreationTime.setText(simpleDateFormat.format(new Date(
				file.lastModified())));

		if (!listSelectedViewIndex.contains(position)) {
			view.setBackgroundColor(Color.TRANSPARENT);
			ivFileTypeImage.setImageBitmap(FileUtil.getImageIcon(file));
			String mime = FileUtil.getMimeType(file);
			ThumbnailTypeEnum thumbnailTypeEnum = null;
			if(mime != null) {
				if(mime.startsWith("image/")) {
					thumbnailTypeEnum = ThumbnailTypeEnum.IMAGE;
				}
				else if(mime.startsWith("video")) {
					thumbnailTypeEnum = ThumbnailTypeEnum.VIDEO;
				}
				ivFileTypeImage.setThumbnail(file, position, thumbnailTypeEnum, null);
			}
				
		} else {
			view.setBackgroundColor(context.getResources().getColor(R.color.long_pressed));
			ivFileTypeImage.setImageResource(R.drawable.ic_selected);
		}

		/*if (isSelectEnable == false
				&& listSelectedViewIndex.contains(position)) {
			listSelectedViewIndex.remove(Integer.valueOf(position));
			view.setBackgroundColor(Color.TRANSPARENT);
			ImageAnimation.flipToOriginalView(ivFileTypeImage, FileUtil.getImageIcon(file));
			String mime = FileUtil.getMimeType(file);
			ThumbnailTypeEnum thumbnailTypeEnum = null;
			if(mime != null) {
				if(mime.startsWith("image/")) {
					thumbnailTypeEnum = ThumbnailTypeEnum.IMAGE;
				}
				else if(mime.startsWith("video")) {
					thumbnailTypeEnum = ThumbnailTypeEnum.VIDEO;
				}
				ivFileTypeImage.setThumbnail(file, position, thumbnailTypeEnum, null);
			}
		}*/

		// Add click listener to imageView
		/*holder.ivFileTypeImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(linearLayoutDetails.getVisibility() == View.INVISIBLE) {
					linearLayoutDetails.setVisibility(View.VISIBLE);
				} else {
					linearLayoutDetails.setVisibility(View.INVISIBLE);
				}
			}
		});*/
		
		/*holder.ivFileTypeImage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				float x = v.getX();
				float y = v.getY();
				int posX = (int)y - 80 ;
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 80);
				params.leftMargin = 64;
				params.rightMargin = 30;
				params.topMargin = posX;
				linearLayoutDetails.setLayoutParams(params);
				linearLayoutDetails.setVisibility(View.VISIBLE);
				return true;
			}
		});*/
		
		
		/*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			view.setOnHoverListener(new OnHoverListener() {
				boolean isHover;
				
				@Override
				public boolean onHover(View v, MotionEvent event) {
					switch(event.getAction()) {
					case MotionEvent.ACTION_HOVER_ENTER:
						if(!isHover) {
							linearLayoutDetails.setVisibility(View.VISIBLE);
							isHover = true;
						}
					case MotionEvent.ACTION_HOVER_EXIT:
						if(isHover) {
							linearLayoutDetails.setVisibility(View.INVISIBLE);
						}
					}
					return true;
				}
			});
		}*/
		
		return view;
	}
	
	public void setIsSelectEnable(Boolean isSelectEnable) {
		this.isSelectEnable = isSelectEnable;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
}
