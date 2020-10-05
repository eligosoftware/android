package com.eligosoftware.notifon;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mragl on 12.11.2016.
 */

public class DifficultySelectAdapter extends ArrayAdapter {

    private Context ctx;
    private String[] contentArray;
    private Integer[] imageArray;

    public DifficultySelectAdapter(Context context, int resource, String[] objects,Integer[] imageArray) {
        super(context, R.layout.spinner_value_layout,R.id.spinnerTextView, objects);
        ctx=context;
        contentArray=objects;
        this.imageArray=imageArray;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
   return getCustomView(position,convertView,parent);

    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.spinner_value_layout,null);
        }
        TextView textView=(TextView)convertView.findViewById(R.id.spinnerTextView);
        textView.setText(contentArray[position]);

        ImageView imageView=(ImageView)convertView.findViewById(R.id.spinnerImages);
        imageView.setImageResource(imageArray[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }
}
