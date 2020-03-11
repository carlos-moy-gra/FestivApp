package com.example.festivapp.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.festivapp.R;
import com.example.festivapp.data.model.Festival;

import java.util.ArrayList;

public class FestivalesRecyclerAdapter extends RecyclerView.Adapter<FestivalesRecyclerAdapter.FestivalesViewHolder> {

    protected View.OnClickListener onClickListener;
    ArrayList<Festival> arrayList;

    public static class FestivalesViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNombre;

        /* TODO: Aquí añadir los campos comentados cuando en la BD exista esa información sobre los objetos Festival */
        // public TextView textViewFecha;
        // public TextView textViewLocalizacion;

        public FestivalesViewHolder(View v) {
            super(v);
            this.textViewNombre = v.findViewById(R.id.nombre_festival);
        }
    }

    public FestivalesRecyclerAdapter(ArrayList<Festival> arrayList) {
        this.arrayList = arrayList;
    }

    // Crea nuevas views (lo invoca el layout manager)
    @Override
    public FestivalesRecyclerAdapter.FestivalesViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // Crea una nueva view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_festival_recycler, parent, false);
        v.setOnClickListener(onClickListener);
        FestivalesRecyclerAdapter.FestivalesViewHolder vh = new FestivalesRecyclerAdapter.FestivalesViewHolder(v);
        return vh;
    }

    // Reemplaza el contenido de una view (lo invoca el layout manager)
    @Override
    public void onBindViewHolder(FestivalesRecyclerAdapter.FestivalesViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView textView = holder.textViewNombre;
        textView.setText(arrayList.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /* TODO: Aquí habrá que definir que pasa cuando se hace click sobre un festival */
    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
