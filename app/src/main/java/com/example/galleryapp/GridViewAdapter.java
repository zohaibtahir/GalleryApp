package com.example.galleryapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<File> list;
    RequestOptions options = new RequestOptions().centerCrop().placeholder(R.color.colorAccent).error(R.color.colorPrimary);

    public GridViewAdapter(Context context,ArrayList<File> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_layout,null);
        ImageView imageView = v.findViewById(R.id.image_in_grid);
        TextView textView = v.findViewById(R.id.image_name_in_grid);

        Glide.with(context).load(list.get(i)).apply(options).into(imageView);
        textView.setText(list.get(i).getName());
        return v;
    }
}
