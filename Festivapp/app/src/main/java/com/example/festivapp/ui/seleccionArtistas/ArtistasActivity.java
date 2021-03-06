package com.example.festivapp.ui.seleccionArtistas;

import android.content.Intent;
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
import com.example.festivapp.ui.main.MainActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ArtistasActivity extends AppCompatActivity {

    final public static String TAG = "Selección de Artistas";

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

        // Añadimos un listener
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
        recyclerViewSeguidos = findViewById(R.id.recyclerArtistasSeguidos);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeguidos.setLayoutManager(layoutManager);
        adapterSeguidos = new ArtistasRecyclerAdapter(listaSeguidos);
        recyclerViewSeguidos.setAdapter(adapterSeguidos);

        // Añadimos un listener
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

        // Consultamos en la BD los artistas que sigue el usuario y actualizamos el adapter de artistas seguidos
        ParseQuery<ParseObject> queryUser = ParseQuery.getQuery("_User");
        queryUser.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    // El usuario sigue a artistas
                    Object object = user.get("artistas_seguidos");
                    ArrayList<ParseObject> artistas_seguidos = (ArrayList<ParseObject>) object;
                    if ((artistas_seguidos != null) && (!artistas_seguidos.isEmpty())) {
                        Log.d(TAG, "El usuario sigue a " + artistas_seguidos.size() + " artistas");
                        for (int i = 0; i < artistas_seguidos.size(); i++) {
                            obtenerArtistasSeguidosUsuario(artistas_seguidos.get(i).getObjectId());
                        }
                    } else {
                        // El usuario no sigue artistas
                        Log.d(TAG, "El usuario no sigue a ningún artista");
                    }

                } else {
                    // Fallo en la query
                    Log.d(TAG, "Error recuperando los datos del usuario: " + e.getMessage());
                }
            }
        });

        // Barra de búsqueda
        artistasSearchView = findViewById(R.id.barra_busqueda_artistas);
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
                    query.whereStartsWith("searchable_nombre", textoQuery);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> artistasList, ParseException e) {
                            if (e == null) {
                                listaResultados.clear();
                                Log.d(TAG, "Obtenidos " + artistasList.size() + " resultados de artistas");
                                for (ParseObject artista : artistasList) {
                                    listaResultados.add((Artista) artista);
                                }
                                adapterResultados.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error recuperando resultados de la búsqueda de Artistas: " + e.getMessage());
                            }
                        }
                    });
                }

                return false;
            }
        });
    }

    public void obtenerArtistasSeguidosUsuario(String idArtista) {

        // Es una consulta asíncrona
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Artista");
        query.getInBackground(idArtista, new GetCallback<ParseObject>() {
            public void done(ParseObject artista, ParseException e) {
                if (e == null) {
                    listaSeguidos.add((Artista)artista);
                    artistasSeguidos.put(((Artista)artista).getNombre(), artista.getObjectId());
                    adapterSeguidos.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error en obtenerArtistasSeguidosUsuario(): " + e.getMessage());
                }
            }
        });
    }

    public void continuar(View view) {

        /* Obtenemos una lista con los objectId de cada uno de los artistas que el usuario ha seleccionado */
        final ArrayList<String> objectIdList = new ArrayList<>();
        for (String key : artistasSeguidos.keySet()) {
            String objectId = artistasSeguidos.get(key);
            objectIdList.add(objectId);
        }
        artistasSeguidos.clear();

        /* Update de artistas en el array de géneros seguidos por el usuario */
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject current_user, ParseException e) {
                if (e == null) {
                    JSONArray arrayArtistasSeguidos = new JSONArray();
                    for (String objectId : objectIdList) {
                        arrayArtistasSeguidos.put(ParseObject.createWithoutData("Artista", objectId));
                    }
                    objectIdList.clear();
                    current_user.put("artistas_seguidos", arrayArtistasSeguidos);
                    current_user.saveInBackground();

                    /* Mandamos al usuario a MainActivity */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado, inténtalo otra vez", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    Log.d(TAG, "Error recuperando los datos del usuario: " + e.getMessage());
                }
            }
        });
    }

}
