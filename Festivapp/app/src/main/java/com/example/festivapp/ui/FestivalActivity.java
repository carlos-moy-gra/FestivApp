package com.example.festivapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.festivapp.R;

public class FestivalActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView cartel = (ImageView)findViewById(R.id.imgcartel);
    RecyclerView listArtistas = (RecyclerView)findViewById(R.id.listArtistas);

    ImageButton botonCartel = (ImageButton)findViewById(R.id.botonCartel);
    ImageButton botonArtistas = (ImageButton)findViewById(R.id.botonArtistas);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);
    }

    @Override
    public void onClick(View v) {

    }
}
