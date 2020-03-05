package com.example.festivapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.festivapp.R;

import java.util.ArrayList;


public class GenerosActivity extends AppCompatActivity {

    /* https://www.youtube.com/watch?v=DOXBg1HwXcI */

    private GridView gridView;
    private GenerosGridAdapter adapter;

    ArrayList<String> listaGeneros = new ArrayList<>();
    // poblar, no se xq no deja
    // arrayList.add("Genero");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generos);

        GridView gridGeneros = (GridView)findViewById(R.id.gridGeneros);
        adapter = new GenerosGridAdapter(this,listaGeneros);
        gridView.setAdapter(adapter);

        /* Accion al pinchar en un elemento
        en el video abre una ventana para el elemento
        nosotros aqui solo queremos que lo guarde y cambie la imagen como marcada*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        /*
        http://www.androidcurso.com/index.php/tutoriales-android-avanzado/51-unidad-1-diseno-avanzado-de-interfaces-de-usuario/358-gridview

        gridGeneros.setAdapter(new GenerosGridAdapter(this));
        gridGeneros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Mensaje --> Cambiar por chekear el boton
                Toast.makeText(GenerosActivity.this, "Seleccionado el elemento: " + position, Toast.LENGTH_SHORT).show();
            }
        });

         */
    }
}
