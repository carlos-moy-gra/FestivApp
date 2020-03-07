package com.example.festivapp.data.model;

import com.parse.ParseObject;

import org.json.JSONArray;

public class Usuario extends ParseObject {

        /* Los atributos a persistir en el servidor NO son atributos de clase
    en este caso:
        - nombre_completo
        - username
        - email
        - sexo
        - artistas_seguidos
        - generos_seguidos
        - festivales_seguidos
        - festivales_recomendados
    */

    public Usuario() {}

    public String getNombreCompleto() { return getString("nombre_completo"); }

    public void setNombreCompleto(String nombre_completo) { put("nombre_completo", nombre_completo); }

    public String getUsername() { return getString("username"); }

    public void setUsername(String username) { put("username", username); }

    public String getEmail() { return getString("email"); }

    public void setEmail(String email) { put("email", email); }

    public String getSexo() { return getString("sexo"); }

    public void setSexo(String sexo) { put("sexo", sexo); }

    public JSONArray getArtistasSeguidos() { return getJSONArray("artistas_seguidos"); }

    public JSONArray getGenerosSeguidos() { return getJSONArray("generos_seguidos"); }

    public JSONArray getFestivalesSeguidos() { return getJSONArray("festivales_seguidos"); }

    public JSONArray getFestivalesRecomendados() { return getJSONArray("festivales_recomendados"); }

    public void setPasswordNE(String passwrodNE) { put("passwordNE", passwrodNE); }

    @Override
    public String toString() {
        return "Username: " + this.getUsername();
    }

}
