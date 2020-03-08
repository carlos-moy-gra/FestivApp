package com.example.festivapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.festivapp.R;
import com.example.festivapp.data.model.Genero;
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


public class GenerosActivity extends AppCompatActivity {

    private GridView gridView;
    private GenerosGridAdapter adapter;
    private ArrayList<String> listaGeneros = new ArrayList<>();
    private  Map<String, String> generosConId = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generos);

        gridView = findViewById(R.id.gridGeneros);
        adapter = new GenerosGridAdapter(this, listaGeneros);
        gridView.setAdapter(adapter);

        /* Consulta a la BD para obtener todos los géneros almacenados */
        rellenaListaConGenerosBD();

        /* TODO: Consulta a la BD para que aparezcan marcados los géneros que el usuario sigue actualmente (en el caso de que venga de la activity de registro no hace falta) */

        /* Añadimos un listener */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /* Al seleccionar un género este se marca con un tick, si lo volvemos a marcar el tick se quita */

                String generoSeleccionado = listaGeneros.get(position);
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
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> generosList, ParseException e) {
                if (e == null) {
                    Log.d("Genero", "Obtenidos " + generosList.size() + " géneros");
                    for(ParseObject genero : generosList) {
                        listaGeneros.add(((Genero)genero).getNombre());
                        generosConId.put(((Genero)genero).getNombre(), genero.getObjectId());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("Genero", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void continuar(View view) {
        if (adapter.generosSeguidos.isEmpty()) {

            /* No hay géneros seleccionados */
            Toast toast = Toast.makeText(getApplicationContext(), "Debes seleccionar algún género para continuar", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

        } else {

            /* Obtenemos una lista con los objectId de cada uno de los géneros que el usuario ha seleccionado */
            final ArrayList<String> objectIdList = new ArrayList<>();
            for (String key : adapter.generosSeguidos.keySet()) {
                String objectId = adapter.generosSeguidos.get(key);
                objectIdList.add(objectId);
            }

            /* TODO: Consulta de vaciado del array de géneros seguidos por el usuario */

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

                        /* TODO: Intent a ArtistasActivity */

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado, inténtalo otra vez", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                        }
                    }
            });

        }
    }
}
