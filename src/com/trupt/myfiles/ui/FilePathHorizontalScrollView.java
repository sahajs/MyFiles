package com.trupt.myfiles.ui;

import java.io.File;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trupt.myfiles.R;

public class FilePathHorizontalScrollView extends HorizontalScrollView {

	private LinearLayout linearLayout;
	private String currentFilePath;
	
	private OnFilePathHSVClickListener onFilePathHSVClickListener;

	public FilePathHorizontalScrollView(Context context) {
		super(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(layoutParams);
		init();
	}

	public FilePathHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public FilePathHorizontalScrollView(Context context, AttributeSet attrs, int fl) {
		super(context, attrs, fl);
		init();
	}
	
	private void init() {
		linearLayout = new LinearLayout(getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(params);
		this.addView(linearLayout);		
	}

	public void setOnFilePathHSVClickListener(OnFilePathHSVClickListener listener) {
		this.onFilePathHSVClickListener = listener;
	}
 
	public void displayView(String newFilePath, String originFilePath) {
		int newLength = 0;
		int currentLength = 0;
		String[] currentViews = null;
		if (currentFilePath != null) {
			currentViews = currentFilePath.split(File.separator);
			currentLength = currentViews.length;
			if (currentViews.length == 0 && currentFilePath.equals(File.separator)) {
				currentViews = new String[1];
				currentViews[0] = File.separator;
				currentLength = 1;
			}
		}

		String[] newViews = newFilePath.split(File.separator);
		newLength = newViews.length;
		if (newViews.length == 0 && newFilePath.equals(File.separator)) {
			newViews = new String[1];
			newViews[0] = File.separator;
			newLength = 1;
		}
		
		String[] originViews = originFilePath.split(File.separator);
		if (originViews.length == 0 && originFilePath.equals(File.separator)) {
			originViews = new String[1];
			originViews[0] = File.separator;
		}

		int diff = newLength - currentLength;
		if (diff != 0) {
			if (diff > 0) {
				for (int i = currentLength; i < newViews.length; i++) {
					String fileName = newViews[i];
					if (fileName.equals("")) {
						fileName = File.separator;
					}
					String filepath = newFilePath.substring(0, newFilePath.indexOf(fileName)
							+ fileName.length());
					boolean isClickEnable = true;
					if(originViews.length - 1 > i) {
						isClickEnable = false;
					}
					addViewToLayout(fileName, filepath, newFilePath, isClickEnable);
				}
				expandScrollView();
			} else {
				for (int i = currentViews.length - 1; i >= newViews.length; i--) {
					removeViewFromLayout(i);
				}
				reduceScrollView();
			}
			currentFilePath = newFilePath;
		}
	}

	private void removeViewFromLayout(int index) {
		final View view = linearLayout.getChildAt(index);
		Animation animation = new ScaleAnimation(1f, 0.1f, 1, 1);
		animation.setDuration(150);
		view.startAnimation(animation);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				linearLayout.removeView(view);
			}
		});
	}

	private void addViewToLayout(String fName, String filePath, String newFilePath, boolean isClickEnable) {
		View view = getFilePathView(fName, filePath, newFilePath, isClickEnable);
		linearLayout.addView(view);
	}

	//TODO: create adapter to get viewes
	private View getFilePathView(String fName, String filePath, String newFilePath, boolean isClickEnable) {
		final LinearLayout liLayout = new LinearLayout(getContext());
		liLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		liLayout.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams paramsSeparator = new LinearLayout.LayoutParams(
				1, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		paramsSeparator.setMargins(0, 8, 0, 8);
		View viewSeparator = new View(getContext());
		viewSeparator.setBackgroundColor(getResources().getColor(
				R.color.filepath_seperator_view));
		viewSeparator.setLayoutParams(paramsSeparator);
		viewSeparator.setAlpha(100);
		liLayout.addView(viewSeparator);

		final TextView textView = new TextView(getContext());
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		textView.setTextSize(16);
		textView.setBackgroundResource(R.drawable.selector_file_path_button);
		textView.setTextColor(getResources().getColor(
				R.color.filepath_text_color));

		textView.setTag(filePath);
		textView.setText(fName);
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(25, 0, 25, 0);

		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onFilePathHSVClickListener.onClick(textView.getTag().toString());
			}
		});
		
		if(!isClickEnable) {
			textView.setEnabled(false);
		}

		Animation animation = new ScaleAnimation(0.1f, 1, 1, 1);
		animation.setDuration(150);
		liLayout.startAnimation(animation);

		liLayout.addView(textView);		
		return liLayout;
	}
	
	private void expandScrollView() {
		ViewTreeObserver vto = linearLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
		    	//TODO write different code for different API level
		    	linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        int viewWidth = linearLayout.getMeasuredWidth();
		        int dx = 0;
		        if((dx = viewWidth - 1080) > 0) {
		        	smoothScrollTo(dx, 0);
		        }
		    }
		});
	}
	
	private void reduceScrollView() {
		ViewTreeObserver vto = linearLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
		    	//TODO write different code for different API level
		    	linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        int viewWidth = linearLayout.getMeasuredWidth();
		        int dx = 0;
		        if((dx = viewWidth - 1080) > 0) {
		        	smoothScrollTo(dx, 0);
		        }
		    }
		});
	}
	
	/*private synchronized void notifyThread() {
		notify();
	}

	private synchronized void waitThread() throws InterruptedException {
		wait();
	}
	
	private class RefreshThread extends Thread {
		@Override 
		public void run() { 
			try { 
				while(true) { 
					//while(isScrollReq) {
						refreshHandler.sendEmptyMessage(0); 
						Thread.sleep(10);
					//}
					//waitThread();
				} 
			} catch (InterruptedException e) { 
			} 
		}
	}
	  
	private class RefreshHandler extends Handler {
		@Override 
		public void handleMessage(Message msg) {
			super.handleMessage(msg); 
			int x = FilePathHorizontalScrollView.this.getScrollX();
			//System.out.println("X: " + x); 
			FilePathHorizontalScrollView.this.smoothScrollBy(5, 0); 
			if(x ==	FilePathHorizontalScrollView.this.getScrollX()) { 
				System.out.println("SCRoll " + getScrollX());
				//isScrollReq = false;
				refreshThread.interrupt();
				//System.out.println("X: " + x); 
			} 
		} 
	}*/
}
