package com.example.festivapp.ui.seleccionArtistas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.festivapp.R;
import com.example.festivapp.data.model.Artista;

import java.util.ArrayList;

public class ArtistasGridAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Artista> arrayList;

    public ArtistasGridAdapter (Context context, ArrayList<Artista> arrayList) {
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
            convertView = layoutInflater.inflate(R.layout.item_artista_grid,null);
        }

        TextView textView = convertView.findViewById(R.id.item_seleccion_artista);

        textView.setText(arrayList.get(position).getNombre());

        return convertView;
    }
}
