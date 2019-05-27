package com.ebmacs.helpapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class SearchAdapter  extends ArrayAdapter<Integer> {

    public SearchAdapter(Context context, ArrayList<Integer> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        ImageView imgView = convertView.findViewById(R.id.imgView);
        imgView.setImageResource(getItem(position));
        // Return the completed view to render on screen
        return convertView;
    }
}