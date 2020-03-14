package com.example.festivapp.data.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;

@ParseClassName("Festival")
public class Festival extends ParseObject {

    /* Los atributos a persistir en el servidor NO son atributos de clase
    en este caso:
        - nombre
        - fechaInicio
        - fechaFin
        - localizacion
        - pais
        - artistasConfirmados
        - generos
    */

    public Festival() {}

    public String getNombre() { return getString("nombre"); }

    public String getFechaInicio() {
       Calendar cal = Calendar.getInstance();
       cal.setTime(getDate("fechaInicio"));
       return cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH)  + "/" + cal.get(Calendar.YEAR);

    }

    public String getFechaFin() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate("fechaFin"));
        return cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH)  + "/" + cal.get(Calendar.YEAR);
    }

    public String getLocalizacion() { return getString("localizacion"); }

    public String getPais() { return getString("pais"); }

    public JSONArray getArtistasConfirmados() { return getJSONArray("artistas"); }

    public JSONArray getGeneros() { return getJSONArray("generos"); }

    @Override
    public String toString() {
        return "Nombre del festival: " + this.getNombre() +" Localizacion: " + this.getLocalizacion();
    }

}
