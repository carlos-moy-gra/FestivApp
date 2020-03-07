package com.example.festivapp.data.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONObject;

@ParseClassName("Artista")
public class Artista  extends ParseObject {

    /* Los atributos a persistir en el servidor NO son atributos de clase
        en este caso:
            - nombre
            - genero
    */

    public Artista() {}

    public String getNombre() { return getString("nombre"); }

    public JSONObject getGenero() { return getJSONObject("genero"); }

    @Override
    public String toString() {
        return this.getNombre();
    }

}
