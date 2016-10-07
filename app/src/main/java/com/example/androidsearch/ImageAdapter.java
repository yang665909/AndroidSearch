package com.example.androidsearch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<ImageData> {
	public ImageAdapter(Context context, ArrayList<ImageData> images) {
       super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
    	ImageData c = getItem(position);    
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
       }
       // Lookup view for data population
       ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
       // Populate the data into the template view using the data object
       imageView.setImageBitmap(c.getBitmap());
       TextView resolution = (TextView)convertView.findViewById(R.id.size);
       TextView location = (TextView)convertView.findViewById(R.id.location);
       location.setText(c.getDisplayLink());
       StringBuffer buffer = new StringBuffer(32);
       resolution.setText(buffer.append(c.getWidth()).append(" x ").append(c.getHeight()).toString());
       return convertView;

   }
}
