package com.example.androidsearch;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String TAG_TASK_FRAGMENT = "task_fragment";
	static final String ANY_SIZE = "any size";
	static final String ANY_TYPE = "any type";
	
	private ArrayList<ImageData> list = new ArrayList<ImageData>();
	private ImageAdapter adapter;
	private TaskFragment taskFragment;
	private String imgSize;
	private String imgFileType;
	private String searchLimit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = getFragmentManager();
		taskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
	    // If the Fragment is non-null, then it is currently being
	    // retained across a configuration change.
	    if (taskFragment == null) {
	    	taskFragment = new TaskFragment();
	      fm.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
	    }
		
		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText text = (EditText)findViewById(R.id.search);
				String searchName = text.getText().toString();
				text = (EditText)findViewById(R.id.related_site);
				String relatedSite = text.getText().toString();
				if (searchName.isEmpty()) {
					new AlertDialog.Builder(MainActivity.this)
	                 .setTitle(R.string.error_title)
	                 .setMessage(R.string.no_name_message)
	                 .setCancelable(false)
	                 .setPositiveButton(R.string.ok, 
	                		 new android.content.DialogInterface.OnClickListener() {
	                     @Override
	                     public void onClick(DialogInterface dialog, int which) {
	                    	 dialog.cancel();
	                     }
	                 }).create().show();
				} else {
					ImageSearchCriterion criterion = new ImageSearchCriterion(searchName
							, imgFileType, imgSize, searchLimit, relatedSite);
					taskFragment.startTask(criterion);
				}
				
			}
		});
		
		ListView view = (ListView) findViewById(R.id.list);
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
				intent.putExtra("url", list.get(position).getLink());
				MainActivity.this.startActivity(intent);
			}
		});
		adapter = new ImageAdapter(this, list);
		view.setAdapter(adapter);
		
		setupSpiner();
	}

	ArrayList<ImageData> getImageData() {
		return list;
	}
	
	ImageAdapter getImageAdapter() {
		return adapter;
	}
	
	private void setupSpiner() {
		Spinner spinner = (Spinner) findViewById(R.id.imgSize);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.img_size, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				imgSize = arg0.getItemAtPosition(arg2).toString();
				Log.i(TAG, "imgSize is " + imgSize);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
		});
		
		spinner = (Spinner) findViewById(R.id.fileType);
		adapter = ArrayAdapter.createFromResource(this,
		        R.array.file_type, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				imgFileType = arg0.getItemAtPosition(arg2).toString();
				Log.i(TAG, "imgFileType is " + imgFileType);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
		});
		
		spinner = (Spinner) findViewById(R.id.numLimit);
		adapter = ArrayAdapter.createFromResource(this,
		        R.array.num, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				searchLimit = arg0.getItemAtPosition(arg2).toString();
				Log.i(TAG, "searchLimit is " + searchLimit);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
