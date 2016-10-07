package com.example.androidsearch;

import android.graphics.Bitmap;

public class ImageData {
	private String link;
	private String thumbnailLink;
	private Bitmap bitmap;
	private int width;
	private int height;
	private String displayLink;
	
	public ImageData(String link, String thumbnailLink, Bitmap bitmap, int width,
			int height, String displayLink) {
		this.link = link;
		this.thumbnailLink = thumbnailLink;
		this.bitmap = bitmap;
		this.width = width;
		this.height = height;
		this.displayLink = displayLink;
	}
	
	public String getLink() {
		return link;
	}
	
	public String getThumbnailLink() {
		return thumbnailLink;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getDisplayLink() {
		return displayLink;
	}
}
