package com.example.festivapp.ui.seleccionArtistas;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.festivapp.R;
import com.example.festivapp.data.model.Artista;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ArtistasActivity extends AppCompatActivity {

    private GridView gridViewResultados;
    private RecyclerView recyclerViewSeguidos;
    private ArtistasGridAdapter adapterResultados;
    private ArtistasRecyclerAdapter adapterSeguidos;
    private RecyclerView.LayoutManager layoutManager;

    // Conjuntos de datos
    private ArrayList<Artista> listaResultados = new ArrayList<>();  // Esta lista variará cada vez que se realice una consulta a la BD
    private ArrayList<Artista> listaSeguidos = new ArrayList<>();    /* Esta lista variará cuando el usuario seleccione artistas o marque artistas que estaban
                                                                       seleccionados como no seleccionados */
    private Map<String, String> artistasSeguidos = new Hashtable<>();  // Almacena los objectId de los artistas que se encuentran en seguidos

    private SearchView artistasSearchView;  // Barra de búsqueda de artistas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistas);

        // Grid resultados búsqueda
        gridViewResultados = findViewById(R.id.gridResultadosArtistas);
        adapterResultados = new ArtistasGridAdapter(this, listaResultados);
        gridViewResultados.setAdapter(adapterResultados);

        /* Añadimos un listener */
        gridViewResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /* Al seleccionar un artista de los resultados de la búsqueda, este se mueve a artistas seguidos (si no se encuentra ya allí)  */

                Artista artistaSeleccionado = listaResultados.get(position);
                if (!artistasSeguidos.containsKey(artistaSeleccionado.getNombre())) {
                    /* El artista no estaba seleccionado, lo movemos a seguidos */
                    artistasSeguidos.put(artistaSeleccionado.getNombre(), artistaSeleccionado.getObjectId());
                    listaSeguidos.add(artistaSeleccionado);
                    adapterSeguidos.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Ahora sigues a " + artistaSeleccionado.getNombre(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });

        // RecyclerView seguidos
        recyclerViewSeguidos = findViewById(R.id.gridArtistasSeguidos);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeguidos.setLayoutManager(layoutManager);
        adapterSeguidos = new ArtistasRecyclerAdapter(listaSeguidos);
        recyclerViewSeguidos.setAdapter(adapterSeguidos);

        /* Añadimos un listener */
        adapterSeguidos.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recyclerViewSeguidos.getChildAdapterPosition(v);
                Artista artistaSeleccionado = listaSeguidos.get(pos);
                artistasSeguidos.remove(artistaSeleccionado.getNombre());
                listaSeguidos.remove(pos);
                adapterSeguidos.notifyDataSetChanged();
                Toast toast = Toast.makeText(getApplicationContext(), "Ya no sigues a " + artistaSeleccionado.getNombre(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
        });

        /* Aquí consultamos en la BD los artistas que sigue el usuario y actualizamos el adapter de artistas seguidos

        // Obtenemos el objeto usuario al completo (campos actualizados) TODO: testear su funcionamiento correcto
        ParseQuery<ParseObject> queryUser = ParseQuery.getQuery("_User");
        queryUser.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {

                    Object object = user.get("artistas_seguidos");
                    ArrayList<ParseObject> artistas_seguidos = (ArrayList<ParseObject>) object;
                    if (!artistas_seguidos.isEmpty()) {
                        for (int i = 0; i < artistas_seguidos.size(); i++) {
                            obtenerArtistasSeguidosUsuario(artistas_seguidos.get(i).getObjectId());
                        }
                    } else {
                        System.out.println("El Usuario no sigue a ningún artista");
                    }

                } else {
                    // Fallo en la query
                }
            }
        });
        */

        // Barra de búsqueda
        artistasSearchView = (SearchView) findViewById(R.id.barra_busqueda_artistas);
        artistasSearchView.setIconifiedByDefault(false);

        // Añadimos un listener
        artistasSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String textoQuery) {

                /* No hacemos nada */

                return false;
            }

            @Override
            public boolean onQueryTextChange(String textoQuery) {

                /* Buscamos en la BD */
                if (textoQuery.trim().isEmpty()) {
                    listaResultados.clear();
                    adapterResultados.notifyDataSetChanged();
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Artista");
                    query.whereStartsWith("nombre", textoQuery);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> artistasList, ParseException e) {
                            if (e == null) {
                                Log.d("Artista", "Obtenidos " + artistasList.size() + " resultados");
                                for (ParseObject artista : artistasList) {
                                    listaResultados.add((Artista) artista);
                                }
                                adapterResultados.notifyDataSetChanged();
                            } else {
                                Log.d("Artista", "Error: " + e.getMessage());
                            }
                        }
                    });
                }

                return false;
            }
        });
    }

    public void obtenerArtistasSeguidosUsuario(String idArtista) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Artista");
        query.getInBackground(idArtista, new GetCallback<ParseObject>() {
            public void done(ParseObject artista, ParseException e) {
                if (e == null) {
                    listaSeguidos.add((Artista)artista);
                    artistasSeguidos.put(((Artista)artista).getNombre(), ((Artista)artista).getObjectId());
                    adapterSeguidos.notifyDataSetChanged();
                } else {
                    // Fallo en la query
                }
            }
        });
    }

}
