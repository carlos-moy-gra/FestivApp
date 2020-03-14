package com.example.festivapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.festivapp.R;
import com.example.festivapp.data.model.Festival;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    // Se crean en total 2 instancias del fragment

    public static String TAG = "PlaceholderFragment";

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private ArrayList<Festival> listaFestivales;
    private FestivalesRecyclerAdapter adapter;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);  // pasamos el indice de la sección al fragment a través del bundle
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index); // Aquí se produce el cambio en los datos (al cambiar de Tab)

        switch(index) {
            case 1:
                Log.d(TAG, "Inicializando datos del apartado Mis Festivales de MainActivity");
                pageViewModel.getMisFestivales().observe(this, new Observer<List<Festival>>() {
                    @Override
                    public void onChanged(@Nullable List<Festival> lista) {
                        actualizarAdapter(lista);
                        System.out.println("Cambiando info");
                        if ((lista != null) && (lista.size() > 0))
                            pageViewModel.getInfoNoFestivales().setValue("");
                        else
                            pageViewModel.getInfoNoFestivales().setValue("Todavía no sigues a ningún festival");
                    }
                });
                break;
            case 2:
                Log.d(TAG, "Inicializando datos del apartado Descubrir de MainActivity");
                pageViewModel.getDescubrir().observe(this, new Observer<List<Festival>>() {
                    @Override
                    public void onChanged(@Nullable List<Festival> lista) {
                        actualizarAdapter(lista);
                    }
                });
                break;
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Inicializamos la lista de festivales del fragment
        listaFestivales = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        final TextView infoNoFestivales = root.findViewById(R.id.info_no_festivales);

        // RecyclerView que mostrará los festivales en un lista
        RecyclerView recyclerView = root.findViewById(R.id.recyclerMain);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FestivalesRecyclerAdapter(listaFestivales);
        recyclerView.setAdapter(adapter);

        pageViewModel.getInfoNoFestivales().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String texto) {
                infoNoFestivales.setText(texto);
            }
        });

        return root;
    }

    public void actualizarAdapter(List<Festival> lista) {
        listaFestivales.clear();
        listaFestivales.addAll(lista);
        adapter.notifyDataSetChanged(); // No se realiza bien, solamente se muestra 1 resultado en el apartado Descubrir
    }
}