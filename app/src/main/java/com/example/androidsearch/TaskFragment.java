package com.example.androidsearch;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class  TaskFragment extends Fragment {
	private static final String TAG = TaskFragment.class.getSimpleName();
	private static final String URL = "https://www.googleapis.com/customsearch/v1?key" +
									"=AIzaSyDECDjyBYLl2KaPZU355DYDgdmoIAFjiWs&" +
									"cx=009825201819706529932:hh2x-cezeca&searchType=image&&alt=json";
	private MainActivity activity;
	private DownloadTask task;

	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    this.activity = (MainActivity)activity;
	}

	/**
	 * This method will only be called once when the retained
	 * Fragment is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    // Retain this fragment across configuration changes.
	    setRetainInstance(true);
	}

	/**
	 * Set the callback to null so we don't accidentally leak the 
	 * Activity instance.
	 */
	@Override
	public void onDetach() {
	    super.onDetach();
	    activity = null;
	}
	  
	public void startTask(ImageSearchCriterion criterion) {
		// Create and execute the background task.
		StringBuffer buffer = new StringBuffer();
		String fileSize = criterion.getFileSize();
		String fileType = criterion.getFileType();
		buffer.append(URL);
		if (!fileType.equals(MainActivity.ANY_TYPE)) {
			buffer.append("&fileType=").append(fileType);
		}
		if (!fileSize.equals(MainActivity.ANY_SIZE)) {
			buffer.append("&imgSize=").append(fileSize);
		}
		buffer.append("&num=").append(criterion.getNumLimit());
		try {
			Log.i(TAG, "url is " + buffer.toString());
			buffer.append("&q=").append(URLEncoder.encode(criterion.getQuery(), "UTF-8"));
			if (!criterion.getRelatedSite().isEmpty()) {
				buffer.append("&relatedSite=").append(URLEncoder.encode(criterion.getRelatedSite(), "UTF-8"));
			}
			Log.i(TAG, "url is " + buffer.toString());
			task = new DownloadTask();
			task.execute(buffer.toString());
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	private class DownloadTask extends AsyncTask <String, Void, Void> {
		ArrayList<ImageData> list = new ArrayList<ImageData>();
		ProgressDialog dialog;

	    @Override
	    protected void onPreExecute() {
	        dialog = new ProgressDialog(activity);
	        dialog.setTitle(R.string.uploading);
	        dialog.setCancelable(false);
	        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        dialog.show();
	        super.onPreExecute();
	    }
	    
	    @Override
		protected Void doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(params[0]);
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
				
				JSONObject parentObject = new JSONObject((text));
				JSONArray itemArray = parentObject.getJSONArray("items");
				for (int i = 0; i < itemArray.length(); i++) {
					String link = itemArray.getJSONObject(i).getString("link");
					String displayLink = itemArray.getJSONObject(i).getString("displayLink");
					String thumbnail = itemArray.getJSONObject(i).getJSONObject("image").getString("thumbnailLink");
					int width = itemArray.getJSONObject(i).getJSONObject("image").getInt("width");
					int height = itemArray.getJSONObject(i).getJSONObject("image").getInt("height");
					URL url = new URL(thumbnail);
					Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					list.add(new ImageData(link, thumbnail, bmp, width, height, displayLink)); 
					Log.i(TAG, "link " + link);
					Log.i(TAG, "thumbnail " + thumbnail);
				}
			} catch (Exception e) {
				Log.e(TAG, "error here " + e.toString());
			}
			return null;
	    }
	
		@Override
		protected void onPostExecute(Void a) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
	        }
			if (activity != null) {
				ArrayList<ImageData> aList = activity.getImageData();
				ImageAdapter adapter = activity.getImageAdapter();
				aList.clear();
				aList.addAll(list);
				adapter.notifyDataSetChanged();
			}
			
		}
		
		private String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			if (entity == null) {
				return "";
			}
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
		}
	}
}