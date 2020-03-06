package com.example.festivapp;

import android.app.Application;

import com.example.festivapp.data.model.Artista;
import com.example.festivapp.data.model.Festival;
import com.example.festivapp.data.model.Genero;
import com.parse.Parse;
import com.parse.ParseObject;

public class FestivappApplication extends Application {

    // Creamos objetos que serán accesibles desde cualquier actividad

    @Override
    public void onCreate() {
        super.onCreate();

        /* Registramos las clases */
        ParseObject.registerSubclass(Genero.class);
        ParseObject.registerSubclass(Artista.class);
        ParseObject.registerSubclass(Festival.class);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myAppId")
                .clientKey("empty")
                .server("https://festivapp2020.herokuapp.com/parse/")
                .build());
    }
}
