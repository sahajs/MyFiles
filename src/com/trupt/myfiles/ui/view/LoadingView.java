package com.trupt.myfiles.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

public class LoadingView extends FrameLayout {

	private LoadingThread loadingThread;
	private View views[];
	private RefreshHandler refreshHandler;
	private int width;
	private int sleep;
	private int index;
	private boolean isLoad;
	
	public LoadingView(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.getViewTreeObserver().addOnGlobalLayoutListener(new 
				ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				LoadingView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				width = LoadingView.this.getWidth();
				Log.e("DEBUG", "WIDTH: " + width);
				int height = width;
				sleep = (int)(width / 2.6);
				if(width > height) {
					sleep = (int)(height / 2.6);
				}
			}
		});
		
		System.out.println("Density: " + getResources().getDisplayMetrics().density);
								
		Log.e("DEBUG", "WIDTH: "+ width);
		
		refreshHandler = new RefreshHandler();
		
		views = new View[8];
		for(int i = 0; i < views.length; i++) {
			View vWhite = new View(getContext());
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(8, 10);
			params.gravity = Gravity.BOTTOM;
			//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 10);
			vWhite.setLayoutParams(params);
			vWhite.setBackgroundColor(Color.BLUE);
			ObjectAnimator anim = ObjectAnimator.ofFloat(vWhite, "translationX", 0, -8);
			anim.setDuration(0);
			anim.start();
			views[i] = vWhite;
			this.addView(views[i]);
		}
		
	}
	
	public void startAnimation() {
		Log.e("DEBUG", "WIDTH ANIM: " + width);
		if(loadingThread == null) {
			loadingThread = new LoadingThread();
			isLoad = true;
			loadingThread.start();
		}
	}
	
	public void stopAnimation() {
		isLoad = false;
		if(loadingThread != null) {
			if(loadingThread.isAlive()) {
				loadingThread.interrupt();
			}
			loadingThread = null;
		}
	}
	
	private class LoadingThread extends Thread {
		@Override
		public void run() {
			try {
				while(isLoad) {
					Thread.sleep(sleep);
					refreshHandler.sendEmptyMessage(0);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			startAnimation(views[index]);
			index = ++index % 8;
		}
	}
	
	
	private void startAnimation(final View view) {
		float slowWidth = 50 * getResources().getDisplayMetrics().density;
		float fastWidth = (width - slowWidth) / 2;
		//AnimatorSet animatorSet = new AnimatorSet();
		ObjectAnimator oAStart = ObjectAnimator.ofFloat(view, "translationX", 0, fastWidth);
		oAStart.setDuration(600).setInterpolator(new DecelerateInterpolator());
		ObjectAnimator oAMiddle = ObjectAnimator.ofFloat(view, "translationX", fastWidth, fastWidth + slowWidth);
		oAMiddle.setDuration(1100).setInterpolator(new LinearInterpolator());
		oAMiddle.setStartDelay(550);
		ObjectAnimator oAEnd = ObjectAnimator.ofFloat(view, "translationX", fastWidth + slowWidth, width);
		oAEnd.setDuration(600).setInterpolator(new AccelerateInterpolator());
		oAEnd.setStartDelay(1650);
		
		oAStart.start();
		oAMiddle.start();
		oAEnd.start();
	}
	
}
