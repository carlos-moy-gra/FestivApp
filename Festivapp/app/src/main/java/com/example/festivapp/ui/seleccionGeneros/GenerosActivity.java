package com.example.festivapp.ui.seleccionGeneros;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.festivapp.R;
import com.example.festivapp.data.model.Genero;
import com.example.festivapp.ui.main.MainActivity;
import com.example.festivapp.ui.seleccionArtistas.ArtistasActivity;
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

public class GenerosActivity extends AppCompatActivity {

    final public static String TAG = "Selección de Géneros";

    private GridView gridView;
    private GenerosGridAdapter adapter;
    private ArrayList<String> listaGeneros = new ArrayList<>(); // Lista de géneros seleccionados por el usuario (solamente contiene sus nombres)
    private  Map<String, String> generosConId = new Hashtable<>(); // Almacena todos los géneros musicales de la BD
    private boolean usuarioEstaEnProcesoDeRegistro = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generos);

        gridView = findViewById(R.id.gridGeneros);
        adapter = new GenerosGridAdapter(this, listaGeneros);

        /* Si los géneros seguidos por el usuario es un campo sin contenido en la BD significa que la actividad anterior era la actividad de registro, y por lo tanto
           al hacer click en la flecha de continuar, el usuario pasará a la activity de selección de artistas. En caso contrario, significa que el usuario ha llegado a
           esta actividad a través del floating button del menú principal, y, por lo tanto, al hacer click sobre la fecha de continuar deberá ser llevado otra vez al menú
           principal sin pasar por la actividad de selección de artistas */
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject current_user, ParseException e) {
                if (e == null) {
                    ArrayList<ParseObject> generos_seguidos = (ArrayList<ParseObject>) current_user.get("generos_seguidos");
                    if ((generos_seguidos != null) && (!generos_seguidos.isEmpty())) {

                        /* El usuario ya tiene géneros guardados, por lo tanto viene del menú principal */
                        usuarioEstaEnProcesoDeRegistro = false;

                        Genero objectGenero;
                        for(ParseObject genero : generos_seguidos) {
                            ParseQuery<ParseObject> queryGeneros = ParseQuery.getQuery("Genero");
                            try {
                                objectGenero = (Genero) queryGeneros.get(genero.getObjectId());
                                Log.d(TAG, "Recuperado Género: " + (objectGenero).getNombre());
                                adapter.generosSeguidos.put(objectGenero.getNombre(), objectGenero.getObjectId()); // Añadimos el género recuperado a géneros seguidos por el usuario
                            } catch (ParseException ex) {
                                Log.d(TAG, "Error recuperando datos de Género: " + ex.getMessage());
                            }
                        }
                    } else {
                        Log.d(TAG, "Se ha accedido a la actividad de selección de géneros a través del proceso de registro");
                    }

                    // Consulta a la BD para obtener todos los géneros almacenados
                    rellenaListaConGenerosBD();

                    // Asociamos el adaptador al gridView
                    gridView.setAdapter(adapter);

                } else {
                    // Fallo query
                    Log.d(TAG, "Error recuperando los datos del usuario: " + e.getMessage());
                }
            }
        });

        // Añadimos un listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /* Al seleccionar un género este se marca con un tick, si lo volvemos a marcar el tick se quita */

                String generoSeleccionado = listaGeneros.get(position); // nombre del género seleccionado
                TextView textView = view.findViewById(R.id.item_seleccion_genero);

                if (adapter.generosSeguidos.containsKey(generoSeleccionado)) {

                    /* El género ya estaba seleccionado, lo marcamos como no seleccionado */
                    adapter.generosSeguidos.remove(generoSeleccionado);
                    System.out.println("Género a marcar como no seleccionado: " + generoSeleccionado);
                    textView.setText(adapter.arrayList.get(position));
                    textView.setBackgroundColor(0xFFFF4444);

                } else {

                    /* El género no estaba seleccionado, lo marcamos como seleccionado */
                    String idGenero = generosConId.get(generoSeleccionado);
                    adapter.generosSeguidos.put(generoSeleccionado, idGenero);
                    System.out.println("Género a marcar como seleccionado: " + generoSeleccionado);
                    textView.setText("");
                    textView.setBackgroundResource(R.drawable.baseline_check_black_and_red);
                }
            }
        });
    }

    public void rellenaListaConGenerosBD() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Genero");
        try {
            List<ParseObject> generosList = query.find();
            if (generosList != null) {
                Log.d("Genero", "Obtenidos " + generosList.size() + " géneros");
                for (ParseObject genero : generosList) {
                    listaGeneros.add(((Genero) genero).getNombre());
                    generosConId.put(((Genero) genero).getNombre(), genero.getObjectId());
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.d("Genero", "Error, la lista de géneros obtenida es nula: rellenaListaConGenerosBD()");
            }
        } catch (ParseException e) {
            Log.d("Genero", "Error: " + e.getMessage());
        }
    }

    public void continuar(View view) {
        if (adapter.generosSeguidos.isEmpty()) { // No hay géneros seleccionados
            Toast toast = Toast.makeText(getApplicationContext(), "Debes seleccionar algún género para continuar", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

        } else { // Hay géneros seleccionados

            /* Obtenemos una lista con los objectId de cada uno de los géneros que el usuario ha seleccionado */
            final ArrayList<String> objectIdList = new ArrayList<>();
            for (String key : adapter.generosSeguidos.keySet()) {
                String objectId = adapter.generosSeguidos.get(key);
                objectIdList.add(objectId);
            }

            /* Update de géneros en el array de géneros seguidos por el usuario */
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject current_user, ParseException e) {
                    if (e == null) {
                        JSONArray arrayGenerosSeguidos = new JSONArray();
                        for (String objectId : objectIdList) {
                            arrayGenerosSeguidos.put(ParseObject.createWithoutData("Genero", objectId));
                        }
                        current_user.put("generos_seguidos", arrayGenerosSeguidos);
                        current_user.saveInBackground();

                        // Comprobamos cómo hemos llegado a la actividad

                        if (usuarioEstaEnProcesoDeRegistro) {

                            /* Mandamos al usuario a ArtistasActivity */
                            Intent intent = new Intent(getApplicationContext(), ArtistasActivity.class);
                            startActivity(intent);
                        } else {

                            /* Mandamos al usuario a MainActivity (menú principal de la app) */
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        // Fallo query
                        Log.d(TAG, "Error recuperando los datos del usuario: " + e.getMessage());
                        Toast toast = Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado, inténtalo otra vez", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                        }
                    }
            });
        }
    }
}
