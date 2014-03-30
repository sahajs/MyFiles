package com.trupt.myfiles.ui.view;

import java.io.File;

import com.trupt.myfiles.animation.ImageAnimation;
import com.trupt.myfiles.async.ThumbnailBitmapStore;
import com.trupt.myfiles.async.ThumbnailBitmapStore.ThumbnailBitmapStoreListener;
import com.trupt.myfiles.model.enums.ThumbnailTypeEnum;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;

public class ThumbnailImageView extends ImageView implements ThumbnailBitmapStoreListener {

	private Animation animation;
	
	public ThumbnailImageView(Context context) {
		super(context);
	}
	
	public ThumbnailImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ThumbnailImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onThumbnailLoaded(Bitmap bitmap, int tag) {
		int thisTag = (Integer)this.getTag();
		if(thisTag == tag) {
			if(animation != null) {
				this.setAnimation(animation);
			}
			this.setImageBitmap(bitmap);	
		}
	}
	
	@Override
	public void setThumbnailTag(int tag) {
		this.setTag(tag);
	}
	
	@Override
	public int getThumbnailTag() {
		return (Integer)this.getTag();
	}
	
	public void setThumbnail(File file, int position, ThumbnailTypeEnum thumbnailTypeEnum, Animation animation) {
		this.setTag(position);
		this.animation = animation;
		ThumbnailBitmapStore imageBit = ThumbnailBitmapStore.getInstance();
		imageBit.loadThumbnail(file, position, thumbnailTypeEnum, this);
	}
	
}
