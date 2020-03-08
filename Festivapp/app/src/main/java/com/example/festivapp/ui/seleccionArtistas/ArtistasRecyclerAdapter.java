package com.example.festivapp.ui.seleccionArtistas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.festivapp.R;
import com.example.festivapp.data.model.Artista;

import java.util.ArrayList;

public class ArtistasRecyclerAdapter extends RecyclerView.Adapter<ArtistasRecyclerAdapter.ArtistasViewHolder> {

    protected View.OnClickListener onClickListener;
    ArrayList<Artista> arrayList;

    public static class ArtistasViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public ArtistasViewHolder(View v) {
            super(v);
            this.textViewNombre = v.findViewById(R.id.item_seleccion_artista_recycler);
        }
    }

    public ArtistasRecyclerAdapter(ArrayList<Artista> arrayList) {
        this.arrayList = arrayList;
    }

    // Crea nuevas views (lo invoca el layout manager)
    @Override
    public ArtistasRecyclerAdapter.ArtistasViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // Crea una nueva view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artista_recycler, parent, false);
        v.setOnClickListener(onClickListener);
        ArtistasViewHolder vh = new ArtistasViewHolder(v);
        return vh;
    }

    // Reemplaza el contenido de una view (lo invoca el layout manager)
    @Override
    public void onBindViewHolder(ArtistasViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView textView = holder.textViewNombre;
        textView.setText(arrayList.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
