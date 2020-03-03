package com.example.festivapp.ui.registro;

import androidx.annotation.Nullable;

public class RegistroResult {

    @Nullable
    private Integer error;

    RegistroResult(@Nullable Integer error) {
        this.error = error;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
