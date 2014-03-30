package com.trupt.myfiles.animation;

import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.trupt.myfiles.R;

public class ImageAnimation {

	public static void flipToOriginalView(final ImageView imageView, final Bitmap icon) {
		Rotate3dAnimation rotateAnim = new Rotate3dAnimation(0, 0, 360, 270, 0,
				0);
		rotateAnim.setDuration(200);
		rotateAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				Rotate3dAnimation rotateAnim = new Rotate3dAnimation(0, 0, 90,
						0, 0, 0);
				rotateAnim.setDuration(200);
				imageView.setImageBitmap(icon);
				imageView.startAnimation(rotateAnim);
			}
		});
		imageView.startAnimation(rotateAnim);
	}
	
	public static void flipToSelectedView(final ImageView imageView) {
		Rotate3dAnimation rotateAnim = new Rotate3dAnimation(0, 0, 0, 90, 0, 0);
		rotateAnim.setDuration(150);
		rotateAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				Rotate3dAnimation rotateAnim = new Rotate3dAnimation(0, 0, 270,
						360, 0, 0);
				rotateAnim.setDuration(150);
				imageView.setImageResource(R.drawable.ic_selected);
				imageView.setAnimation(rotateAnim);
			}
		});
		imageView.setAnimation(rotateAnim);
	}
}
