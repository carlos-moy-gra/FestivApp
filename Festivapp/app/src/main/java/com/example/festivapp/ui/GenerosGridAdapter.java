package com.example.festivapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.festivapp.R;

import java.util.ArrayList;

public class GenerosGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayList;

    public GenerosGridAdapter (Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_genero_grid,null);
        }
        TextView titulo = (TextView)convertView.findViewById(R.id.item_titulo);
        titulo.setText(arrayList.get(position));
        return convertView;
    }
}
