package com.example.festivapp.ui.registro;

import androidx.annotation.Nullable;

/**
 * Validación de datos del formulario de registro.
 */
public class RegistroFormState {

    @Nullable
    private Integer nombreCompletoError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer sexError;
    private boolean isDataValid;

   RegistroFormState(@Nullable Integer nombreCompletoError, @Nullable Integer usernameError, @Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer sexError) {
        this.nombreCompletoError = nombreCompletoError;
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.sexError = sexError;
        this.isDataValid = false;
    }

    RegistroFormState(boolean isDataValid) {
        this.nombreCompletoError = null;
        this.usernameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.sexError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNombreCompletoError() {
        return nombreCompletoError;
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

    @Nullable
    Integer getSexError() {
        return sexError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
