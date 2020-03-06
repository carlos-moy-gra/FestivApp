package com.example.festivapp.ui.registro;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.festivapp.R;

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

    public void registrar(String username, String email, String password) {
        // can be launched in a separate asynchronous job
        // TODO: comprobar resultado de proceso de registro de usuario
    }

    public void registroDataChanged(String username, String email, String password1, String password2) {
        if (!isUserNameValid(username)) {
            registroFormState.setValue(new RegistroFormState(R.string.invalid_username, null, null));
        } else if (!isEmailValid(email)) {
            registroFormState.setValue(new RegistroFormState(null, R.string.invalid_email, null));
        } else if (!isPasswordValid(password1, password2)) {
            registroFormState.setValue(new RegistroFormState(null, null, R.string.invalid_password_registro));
        } else {
            registroFormState.setValue(new RegistroFormState(true));
        }
    }

    // Validación de username
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.trim().isEmpty()) {
            return false;
        }
        // Comprobación de si existe el nombre de usuario en la BD (es lo que nos certifica la verdadera validez)
        return true;
    }

    // Validación de email
    private boolean isEmailValid(String email) {
        if (email.contains("@")) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return false;
            }
        } else {
            return false;
        }
        // Comprobación del nombre de usuario en la BD (es lo que nos certifica la verdadera validez)
        return true;
    }

    // Validación de contraseña
    private boolean isPasswordValid(String password1, String password2) {
        if (password1.trim().isEmpty() || password2.trim().isEmpty()) {
            return false;
        }
        if (password1.trim().compareTo(password2.trim()) != 0) {
            return false;
        }
        return true;
    }

}
