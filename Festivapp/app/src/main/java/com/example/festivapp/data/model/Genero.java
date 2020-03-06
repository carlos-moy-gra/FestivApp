package com.example.festivapp.data.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Genero")
public class Genero extends ParseObject {

    /* Los atributos a persistir en el servidor NO son atributos de clase
        en este caso:
            - nombre
    */

    public Genero() {}

    public String getNombre() { return getString("nombre");}

    @Override
    public String toString() {
        return this.getNombre();
    }

}
