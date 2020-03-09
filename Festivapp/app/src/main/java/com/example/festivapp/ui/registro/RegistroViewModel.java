package com.example.festivapp.ui.registro;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.festivapp.R;
import com.example.festivapp.ui.login.LoggedInUserView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

public class RegistroViewModel extends ViewModel {

    private MutableLiveData<RegistroFormState> registroFormState = new MutableLiveData<>();
    private MutableLiveData<RegistroResult> registroResult = new MutableLiveData<>();

    RegistroViewModel() {}

    LiveData<RegistroFormState> getRegistroFormState() {
        return registroFormState;
    }

    LiveData<RegistroResult> getRegistroResult() {
        return registroResult;
    }

    public void registrar(String username, String email, String password, String nombreCompleto, String sexo) {

        ParseUser user = new ParseUser();

        // Campos básicos
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // Campos no básicos
        user.put("nombre_completo", nombreCompleto);
        user.put("sexo", sexo);
        JSONArray jsonArray = new JSONArray();
        user.put("artistas_seguidos", jsonArray);
        user.put("generos_seguidos", jsonArray);
        user.put("festivales_seguidos", jsonArray);
        user.put("festivales_recomendados", jsonArray);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.v("Registro correcto","logInInBackground()");
                    registroResult.setValue(new RegistroResult(new LoggedInUserView(ParseUser.getCurrentUser().getString("nombre_completo"))));
                } else {
                    Log.v("Registro incorrecto","logInInBackground()");
                    registroResult.setValue(new RegistroResult(R.string.register_failed));
                }
            }
        });
    }

    public void registroDataChanged(String nombreCompleto, String username, String email, String password1, String password2, boolean sex_hombre, boolean sex_mujer) {
        if (!isNombreCompletoValid(nombreCompleto)) {
            registroFormState.setValue(new RegistroFormState(R.string.invalid_nombre_completo, null, null, null, null));
        } else if (!isUserNameValid(username)) {
            registroFormState.setValue(new RegistroFormState(null, R.string.invalid_username, null, null, null));
        } else if (!isEmailValid(email)) {
            registroFormState.setValue(new RegistroFormState(null, null, R.string.invalid_email, null, null));
        } else if (!isPasswordValid(password1, password2)) {
            registroFormState.setValue(new RegistroFormState(null, null, null, R.string.invalid_password_registro, null));
        } else if (!isSexValid(sex_hombre, sex_mujer)) {
            registroFormState.setValue(new RegistroFormState(null, null, null, null, R.string.invalid_sex_registro));
        } else {
            registroFormState.setValue(new RegistroFormState(true));
        }
    }

    // Validación de nombre completo
    private boolean isNombreCompletoValid(String nombreCompleto) {
        if (nombreCompleto == null) {
            return false;
        }
        if (nombreCompleto.trim().isEmpty())
            return false;
        return true;
    }

    // Validación de username
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    // Validación de email
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    // Validación de contraseña
    private boolean isPasswordValid(String password1, String password2) {
        if ((password1 == null) || (password2 == null)) {
            return false;
        }
        if (password1.trim().isEmpty() || password2.trim().isEmpty()) {
            return false;
        }
        return (password1.trim().compareTo(password2.trim()) == 0);
    }

    // Validación de sexo
    private boolean isSexValid(boolean sex_hombre, boolean sex_mujer) {
        return (sex_hombre || sex_mujer);
    }

}
