package com.example.festivapp.ui.registro;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.festivapp.R;

public class RegistroActivity extends AppCompatActivity {

    private RegistroViewModel registroViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        registroViewModel = ViewModelProviders.of(this, new RegistroViewModelFactory())
                .get(RegistroViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText password_1_EditText = findViewById(R.id.password1);
        final EditText password_2_EditText = findViewById(R.id.password2);
        final Button registerButton = findViewById(R.id.register);

        registroViewModel.getRegistroFormState().observe(this, new Observer<RegistroFormState>() {
            @Override
            public void onChanged(@Nullable RegistroFormState registroFormState) {
                if (registroFormState == null) {
                    return;
                }
                registerButton.setEnabled(registroFormState.isDataValid());
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
            }
        });

        registroViewModel.getRegistroResult().observe(this, new Observer<RegistroResult>() {
            @Override
            public void onChanged(@Nullable RegistroResult registroResult) {
                if (registroResult == null) {
                    return;
                }
                if (registroResult.getError() != null) {
                    showRegistroFailed(registroResult.getError());
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
                registroViewModel.registroDataChanged(usernameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        password_1_EditText.getText().toString(),
                        password_2_EditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        password_1_EditText.addTextChangedListener(afterTextChangedListener);
        password_2_EditText.addTextChangedListener(afterTextChangedListener);
        password_1_EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //?????????????????????????
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registroViewModel.registrar(usernameEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            password_1_EditText.getText().toString());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroViewModel.registrar(usernameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        password_1_EditText.getText().toString());
            }
        });
    }

    private void showRegistroFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}
