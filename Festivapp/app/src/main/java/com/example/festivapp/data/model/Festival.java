package com.example.festivapp.data.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.Date;

@ParseClassName("Festival")
public class Festival extends ParseObject {

    /* Los atributos a persistir en el servidor NO son atributos de clase
    en este caso:
        - nombre
        - fechaInicio
        - fechaFin
        - localizacion
        - artistasConfirmados
        - generos
    */

    public Festival() {}

    public String getNombre() { return getString("nombre"); }

    public Date getFechaInicio() { return getDate("fechaInicio"); }

    public Date getFechaFin() { return getDate("fechaFin"); }

    public String getLocalizacion() { return getString("localizacion"); }

    public JSONArray getArtistasConfirmados() { return getJSONArray("artistas"); }

    public JSONArray getGeneros() { return getJSONArray("generos"); }

    @Override
    public String toString() {
        return "Nombre del festival: " + this.getNombre() +" Localizacion: " + this.getLocalizacion();
    }

}
