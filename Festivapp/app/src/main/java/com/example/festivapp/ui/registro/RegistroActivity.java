package com.example.festivapp.ui.registro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.festivapp.R;
import com.example.festivapp.ui.seleccionArtistas.ArtistasActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class RegistroActivity extends AppCompatActivity {

    private RegistroViewModel registroViewModel;
    private boolean existeEmailEnBD;
    private boolean existeUsernameEnBD;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser.getCurrentUser().logOut();
        setContentView(R.layout.activity_registro);

        this.existeEmailEnBD = true;
        this.existeUsernameEnBD = true;

        registroViewModel = ViewModelProviders.of(this, new RegistroViewModelFactory())
                .get(RegistroViewModel.class);

        final EditText nombreCompletoEditText = findViewById(R.id.registro_nombre_completo);
        final EditText usernameEditText = findViewById(R.id.registro_username);
        final EditText emailEditText = findViewById(R.id.registro_email);
        final EditText password_1_EditText = findViewById(R.id.registro_password1);
        final EditText password_2_EditText = findViewById(R.id.registro_password2);
        final Button registerButton = findViewById(R.id.registro_register);
        final RadioButton radio_hombre = findViewById(R.id.radio_hombre);
        final RadioButton radio_mujer = findViewById(R.id.radio_mujer);

        registroViewModel.getRegistroFormState().observe(this, new Observer<RegistroFormState>() {
            @Override
            public void onChanged(@Nullable RegistroFormState registroFormState) {
                if (registroFormState == null) {
                    return;
                }
                // Invalidamos el botón de registrar si el estado del formulario es incorrecto
                registerButton.setEnabled(registroFormState.isDataValid());

                // Mostramos los errores
                if (registroFormState.getNombreCompletoError() != null) {
                   nombreCompletoEditText.setError(getString(registroFormState.getNombreCompletoError()));
                }
                if (registroFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registroFormState.getUsernameError()));
                }
                if (registroFormState.getEmailError() != null) {
                    emailEditText.setError(getString(registroFormState.getEmailError()));
                }
                if (registroFormState.getPasswordError() != null) {
                    password_1_EditText.setError(getString(registroFormState.getPasswordError()));
                    password_2_EditText.setError(getString(registroFormState.getPasswordError()));
                }
                if (registroFormState.getSexError() != null) {
                    String errorString = getString(registroFormState.getSexError());
                    Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });

        registroViewModel.getRegistroResult().observe(this, new Observer<RegistroResult>() {
            @Override
            public void onChanged(@Nullable RegistroResult registroResult) {
                if (registroResult == null) {
                    return;
                }
                if (registroResult.getError() != null) {
                    // El registro ha fallado
                    showRegistroFailed(registroResult.getError());
                } else {
                    // El registro ha sido exitoso
                    Toast toast = Toast.makeText(getApplicationContext(), "Registro correcto: " + ParseUser.getCurrentUser().get("nombre_completo"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    /* Mandamos al usuario a la activity de selección de géneros */
                    // Intent intent = new Intent(getApplicationContext(), GenerosActivity.class);
                    // startActivity(intent);
                    Intent intent = new Intent(getApplicationContext(), ArtistasActivity.class);
                    startActivity(intent);
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registroViewModel.registroDataChanged(
                        nombreCompletoEditText.getText().toString(),
                        usernameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        password_1_EditText.getText().toString(),
                        password_2_EditText.getText().toString(),
                        radio_hombre.isChecked(),
                        radio_mujer.isChecked());
            }
        };
        nombreCompletoEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        password_1_EditText.addTextChangedListener(afterTextChangedListener);
        password_2_EditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUsernameInBD(usernameEditText.getText().toString());
                isEmailInBD(emailEditText.getText().toString());
                if ((!existeEmailEnBD) && (!existeUsernameEnBD)) {
                    String sexo;
                    final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
                    if (radio_mujer.isChecked())
                        sexo = "Mujer";
                    else
                        sexo = "Hombre";
                    registroViewModel.registrar(usernameEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            password_1_EditText.getText().toString(),
                            nombreCompletoEditText.getText().toString(),
                            sexo);

                } else {
                    if (existeUsernameEnBD && existeEmailEnBD) {
                        usernameEditText.setError("Username no disponible");
                        emailEditText.setError("Email no disponible");
                    } else {
                        if (existeUsernameEnBD) {
                            usernameEditText.setError("Username no disponible");
                            emailEditText.setError(null);
                        } else {
                            emailEditText.setError("Email no disponible");
                            usernameEditText.setError(null);
                        }
                    }
                }
            }
        });
    }

    private void showRegistroFailed(@StringRes Integer errorString) {
        Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public void irAArtistas(View view) {
        /* Mandamos al usuario a la activity de selección de artistas */
        Intent intent = new Intent(getApplicationContext(), ArtistasActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {

        final EditText nombreCompletoEditText = findViewById(R.id.registro_nombre_completo);
        final EditText usernameEditText = findViewById(R.id.registro_nombre_completo);
        final EditText emailEditText = findViewById(R.id.registro_email);
        final EditText password_1_EditText = findViewById(R.id.registro_password1);
        final EditText password_2_EditText = findViewById(R.id.registro_password2);

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_hombre:
                if (checked) {
                    final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
                    final RadioButton radio_hombre = findViewById(R.id.radio_hombre);
                    radio_mujer.setChecked(false);
                    registroViewModel.registroDataChanged(
                            nombreCompletoEditText.getText().toString(),
                            usernameEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            password_1_EditText.getText().toString(),
                            password_2_EditText.getText().toString(),
                            radio_hombre.isChecked(),
                            radio_mujer.isChecked());
                }
                break;
            case R.id.radio_mujer:
                if (checked) {
                    final RadioButton radio_hombre = findViewById(R.id.radio_hombre);
                    final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
                    radio_hombre.setChecked(false);
                    registroViewModel.registroDataChanged(
                            nombreCompletoEditText.getText().toString(),
                            usernameEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            password_1_EditText.getText().toString(),
                            password_2_EditText.getText().toString(),
                            radio_hombre.isChecked(),
                            radio_mujer.isChecked());
                }
                break;
        }
    }

    public void isUsernameInBD(String username) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username", username);
        try {
            List<ParseObject> resultList = query.find();
            if (resultList.size() > 0) {
                Log.d("User", "Username existe en la BD");
                this.existeUsernameEnBD = true;
            } else {
                Log.d("User", "Username no existe en la BD");
                this.existeUsernameEnBD = false;
            }
        } catch (final ParseException e) {
            Log.d(null , "Error: " + e.getMessage());
        }

    }

    public void isEmailInBD(String email) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("email", email);
        try {
            List<ParseObject> resultList = query.find();
            if (resultList.size() > 0) {
                Log.d("Email", "email existe en la BD");
                this.existeEmailEnBD = true;
            } else {
                Log.d("Email", "email no existe en la BD");
                this.existeEmailEnBD = false;
            }
        } catch (final ParseException e) {
            Log.d(null , "Error: " + e.getMessage());
        }

    }

}
