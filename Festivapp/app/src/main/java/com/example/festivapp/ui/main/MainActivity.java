package com.example.festivapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.example.festivapp.R;
import com.example.festivapp.ui.login.LoginActivity;
import com.example.festivapp.ui.registro.RegistroActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Botón flotante
        /* TODO: definir comportamiento del botón flotante */
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void logout(View view) {

        // Realizamos el logout (volvemos a la actividad de login, quizás en el futuro haga falta un intent aqui, cuando login deje de ser la actividad main en el manifest.xml)
        ParseUser.logOut();

        /* Mandamos al usuario al login */
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}