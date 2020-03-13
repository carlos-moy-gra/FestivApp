package com.example.festivapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.example.festivapp.R;
import com.example.festivapp.ui.login.LoginActivity;
import com.example.festivapp.ui.seleccionArtistas.ArtistasActivity;
import com.example.festivapp.ui.seleccionGeneros.GenerosActivity;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {

            Log.d(TAG, "No hay datos del usuario almacenados, redirigiendo a login");

            /* Mandamos al usuario al login */
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            // El usuario no necesita pasar por LoginActivity

            Log.d(TAG, "Hay datos del usuario almacenados");

            setContentView(R.layout.activity_main);
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
        }
    }

    public void logout(View view) {

        // Realizamos el logout (volvemos a la actividad de login, quizás en el futuro haga falta un intent aqui, cuando login deje de ser la actividad main en el manifest.xml)
        ParseUser.logOut();

        /* Mandamos al usuario al login */
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void irASeleccionarArtistas(View view) {

        /* Mandamos a ArtistasActivity */

        Intent intent = new Intent(this, ArtistasActivity.class);
        startActivity(intent);
    }

    public void irASeleccionarGeneros(View view) {

        /* Mandamos a GenerosActivity */

        Intent intent = new Intent(this, GenerosActivity.class);
        startActivity(intent);
    }
}