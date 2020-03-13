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

        // Consulta asíncrona

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
                    Object object = user.get("generos_seguidos");
                    ArrayList<ParseObject> generos_seguidos = (ArrayList<ParseObject>) object;
                    if ((generos_seguidos != null) && (!generos_seguidos.isEmpty())) {
                            // Queremos por cada género que sigue el usuario, todos los festivales que pertenecen a dicho género
                            obtenerFestivalesPorGeneros(generos_seguidos);
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

    private void  obtenerFestivalesPorGeneros(ArrayList<ParseObject> generosUsuario) {

        // Consulta asíncrona

        resultadosDescubrir.clear(); // clear() preventivo

        /*
        // Va a ser una consulta síncrona
        Map<String, Festival> resultadoFestivales = new HashMap<>();
        // Por cada género
        for(int i = 0; i < generosUsuario.size(); i++) {
            // Buscamos todos los festivales que tengan relación con él
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Festival");
            query.whereEqualTo("generos", ParseObject.createWithoutData("Genero", generosUsuario.get(i).getObjectId()));
            try {
                List<ParseObject> resul = query.find();
                Log.d(TAG, "Obtenidos " + resul.size() + " festivales como contenido para el apartado Descubrir [ObjectId Genero: " + generosUsuario.get(i).getObjectId() + "]");
                for (ParseObject festival : resul) {
                    resultadoFestivales.put(festival.getObjectId(), (Festival)festival);
                }
            } catch (ParseException e) {
                Log.d(TAG, "Error obteniendo festival en obtenerFestivalesPorGeneros(): " + e.getMessage());
            }
        }
        for (String festivalId : resultadoFestivales.keySet()) {
            resultadosDescubrir.add(resultadoFestivales.get(festivalId));
        }
        festivalesDescubrir.setValue(resultadosDescubrir); // realizamos setValue -> se activan los observers
        */

        ArrayList<ParseObject> listaGeneros = new ArrayList<>();
        for (int i = 0; i < generosUsuario.size(); i++) {
            listaGeneros.add(ParseObject.createWithoutData("Genero", generosUsuario.get(i).getObjectId()));
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Festival");
        query.whereContainedIn("generos", listaGeneros);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> festivales, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Obtenidos " + festivales.size() + " festivales como contenido para el apartado Descubrir");
                    for (ParseObject festival : festivales) {
                        resultadosDescubrir.add((Festival) festival);
                    }
                    festivalesDescubrir.setValue(resultadosDescubrir); // realizamos setValue -> se activan los observers
                } else {
                    Log.d(TAG, "Error obteniendo festivales en obtenerFestivalesPorGeneros(): " + e.getMessage());
                }
            }
        });
    }
}