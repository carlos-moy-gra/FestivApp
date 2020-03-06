package com.example.festivapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.example.festivapp.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LoginViewModel(){}

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.v("Login correcto","logInInBackground()");
                    loginResult.setValue(new LoginResult(new LoggedInUserView(ParseUser.getCurrentUser().getString("nombre_completo"))));
                } else {
                    Log.v("Login incorrecto","logInInBackground()");
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        });
    }

    /* Realizamos comprobaciones de validez de los campos del formulario de login */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /* Comprobación de validez del username */
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    /* Comprobación de validez de la contraseña */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
