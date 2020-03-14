package com.example.festivapp.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.festivapp.data.model.Festival;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PageViewModel extends ViewModel {

    /* Clase que prepara los datos para los fragments */

    // TAG para los log
    public static String TAG = "PageViewModel";

    // Almacenamiento de resultados (es asíncrono)
    private List<Festival> resultadosMisFestivales = new ArrayList<>();
    private List<Festival> resultadosDescubrir = new ArrayList<>();

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private MutableLiveData<String> infoNoFestivalesSeguidos = new MutableLiveData<>();
    private MutableLiveData<List<Festival>> festivalesMisFestivales;
    private MutableLiveData<List<Festival>> festivalesDescubrir;

    public void setIndex(int index) {
        mIndex.setValue(index); // Importante, provoca un cambio en el index que desencadena una serie de acciones, se llama desde PlaceHolderItem, que a su vez se llama desde SectionPageAdapter
    }

    public MutableLiveData<String> getInfoNoFestivales() { return infoNoFestivalesSeguidos; }

    public LiveData<List<Festival>> getMisFestivales() {
        if (festivalesMisFestivales == null) {
            festivalesMisFestivales = new MutableLiveData<>();
            consultarContenidoMisFestivales();
        }
        return festivalesMisFestivales;
    }

    public LiveData<List<Festival>> getDescubrir() {
        if (festivalesDescubrir == null) {
            festivalesDescubrir = new MutableLiveData<>();
            consultarContenidoDescubrir();
        }
        return festivalesDescubrir;
    }


    private void consultarContenidoMisFestivales(){

        // Consulta asíncrona

        Log.d(TAG, "Realizando consulta a la BD:  consultarContenidoMisFestivales()");

        ParseQuery<ParseObject> queryUser = ParseQuery.getQuery("_User");
        queryUser.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    Object object = user.get("festivales_seguidos");
                    ArrayList<ParseObject> festivales_seguidos = (ArrayList<ParseObject>) object;
                    if ((festivales_seguidos != null) && (!festivales_seguidos.isEmpty())) {
                        int numMax = festivales_seguidos.size();
                        for (int i = 0; i < numMax; i++) {
                            obtenerFestivalesSeguidosUsuario(festivales_seguidos.get(i).getObjectId(), i, numMax - 1);
                        }
                    } else {
                        Log.d(TAG, "El usuario no sigue a ningún festival");
                        festivalesMisFestivales.setValue(new ArrayList<Festival>());
                    }

                } else {
                    // Fallo en la query
                    Log.d(TAG, "Error en la consulta: consultarContenidoMisFestivales(): " + e.getMessage());
                }
            }
        });
    }

    private void obtenerFestivalesSeguidosUsuario(String idFestival, final int  numCall, final int numMax) {

        // Consulta asíncrona (podría hacerse con una lista también, probablemente en la próxima versión se modifique)

        if (numCall == 0) {
            resultadosMisFestivales.clear(); // clear() preventivo
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Festival");
        query.getInBackground(idFestival, new GetCallback<ParseObject>() {
            public void done(ParseObject festival, ParseException e) {
                if (e == null) {
                    resultadosMisFestivales.add((Festival)festival);
                   if (numCall == numMax) {
                       // Significa que acaba de finalizar la última llamada asíncrona que hay que hacer
                       festivalesMisFestivales.setValue(resultadosMisFestivales);  // realizamos setValue -> se activan los observers
                   }
                } else {
                    Log.d(TAG, "Error en la consulta: obtenerFestivalesSeguidosUsuario(): " + e.getMessage());
                }
            }
        });
    }

    private void consultarContenidoDescubrir(){

        // Consulta asíncrona

        Log.d(TAG, "Realizando consulta a la BD:  consultarContenidoDescubrir()");

        ParseQuery<ParseObject> queryUser = ParseQuery.getQuery("_User");
        queryUser.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    Object object1 = user.get("generos_seguidos");
                    Object object2 = user.get("artistas_seguidos");
                    ArrayList<ParseObject> generos_seguidos = (ArrayList<ParseObject>) object1;
                    ArrayList<ParseObject> artistas_seguidos = (ArrayList<ParseObject>) object2;
                    if ((generos_seguidos != null) && (!generos_seguidos.isEmpty())) {
                            // Queremos por cada género que sigue el usuario, todos los festivales que pertenecen a dicho género
                            obtenerFestivalesPorGeneros(generos_seguidos, artistas_seguidos);
                    } else {
                        Log.d(TAG, "Error de consistencia en la BD, el usuario no sigue ningún género musical");
                    }
                } else {
                    // Fallo en la query
                    Log.d(TAG, "Error en la consulta: consultarContenidoDescubrir(): " + e.getMessage());
                }
            }
        });
    }

    private void  obtenerFestivalesPorGeneros(final ArrayList<ParseObject> generosUsuario, final ArrayList<ParseObject> artistasUsuario) {

        resultadosDescubrir.clear(); // clear() preventivo

        ArrayList<ParseObject> listaGeneros = new ArrayList<>();
        for (int i = 0; i < generosUsuario.size(); i++) {
            listaGeneros.add(ParseObject.createWithoutData("Genero", generosUsuario.get(i).getObjectId()));
        }
        // Query 1
        ParseQuery<ParseObject> queryGeneros = ParseQuery.getQuery("Festival");
        queryGeneros.whereContainedIn("generos", listaGeneros);

        if ((artistasUsuario != null) && (!artistasUsuario.isEmpty())) {

            /* Si el usuario sigue a artistas */
            ArrayList<ParseObject> listaArtistas = new ArrayList<>();
            for (int i = 0; i < artistasUsuario.size(); i++) {
                listaArtistas.add(ParseObject.createWithoutData("Artista", artistasUsuario.get(i).getObjectId()));
            }
            // Query 2
            ParseQuery<ParseObject> queryArtistas = ParseQuery.getQuery("Festival");
            queryArtistas.whereContainedIn("artistas", listaArtistas);
            List<ParseQuery<ParseObject>> listaQueries = new ArrayList<>();
            listaQueries.add(queryGeneros);
            listaQueries.add(queryArtistas);
            // Query main
            ParseQuery<ParseObject> mainQuery = ParseQuery.or(listaQueries);
            mainQuery.orderByAscending("fechaInicio");
            mainQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> festivales, ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Festivales obtenidos en la consulta generos OR artistas: " + festivales.size());
                        for (ParseObject festival : festivales) {
                            resultadosDescubrir.add((Festival) festival);
                        }
                        festivalesDescubrir.setValue(resultadosDescubrir);
                    } else {
                        Log.d(TAG, "Error en la consulta generos OR artistas: " + e.getMessage());
                    }
                }
            });
        } else {

            /* Si el usuario no sigue a artistas */
            queryGeneros.orderByAscending("fechaInicio");
            queryGeneros.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> festivales, ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Festivales obtenidos en la consulta generos: " + festivales.size());
                        for (ParseObject festival : festivales) {
                            resultadosDescubrir.add((Festival) festival);
                        }
                        festivalesDescubrir.setValue(resultadosDescubrir);
                    } else {
                        Log.d(TAG, "Error en la consulta generos OR artistas: " + e.getMessage());
                    }
                }
            });
        }
    }

}