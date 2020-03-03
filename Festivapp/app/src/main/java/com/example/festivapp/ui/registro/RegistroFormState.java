package com.example.festivapp.ui.registro;

import androidx.annotation.Nullable;

/**
 * Validación de datos del formulario de registro.
 */
public class RegistroFormState {

    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

   RegistroFormState(@Nullable Integer usernameError, @Nullable Integer emailError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    RegistroFormState(boolean isDataValid) {
        this.usernameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
