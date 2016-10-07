package com.example.androidsearch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageActivity extends Activity {
	private static final String TAG = ImageActivity.class.getSimpleName();
	private ImageView view;
	private ProgressBar progressBar;
	private AsyncHttpTask asyncHttpTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		view = (ImageView)findViewById(R.id.image);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		String url = this.getIntent().getStringExtra("url");
		asyncHttpTask = new AsyncHttpTask();
		asyncHttpTask.execute(url);
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if (asyncHttpTask != null && !asyncHttpTask.isCancelled()) {
	    	asyncHttpTask.cancel(true);
	    	Log.i(TAG, "onDestroy is called");
	    }
	}
	
	private class AsyncHttpTask extends AsyncTask<String, Void, Boolean> {
		Bitmap bmp;
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				URL url;
				url = new URL(params[0]);
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (MalformedURLException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
            return true;
        }

		@Override
		protected void onPostExecute(Boolean result) {
		    // Download complete. Let us update UI
			progressBar.setVisibility(View.GONE);
		    if (result) {
		        view.setImageBitmap(bmp);
		    } else {
		    	 new AlertDialog.Builder(ImageActivity.this)
		         .setTitle(R.string.error_title)
		         .setMessage(R.string.error_message)
		         .setCancelable(false)
		         .setPositiveButton(R.string.ok, new OnClickListener() {
		             @Override
		             public void onClick(DialogInterface dialog, int which) {
		            	 dialog.cancel();
		             }
		         }).create().show();
		    }
		}
	}
}
