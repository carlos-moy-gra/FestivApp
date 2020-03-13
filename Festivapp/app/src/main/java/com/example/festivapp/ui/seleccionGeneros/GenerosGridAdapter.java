package com.example.festivapp.ui.seleccionGeneros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.festivapp.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class GenerosGridAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> arrayList;
    Map<String, String> generosSeguidos = new Hashtable<>(); // mapa de géneros seguidos

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

        TextView textView = convertView.findViewById(R.id.item_seleccion_genero);

        if (generosSeguidos.containsKey(arrayList.get(position))) {
            textView.setText("");
            textView.setBackgroundResource(R.drawable.baseline_check_black_and_red);
        } else {
            textView.setText(arrayList.get(position));
            textView.setBackgroundColor(0xFFFF4444);
        }
        return convertView;
    }
}
