package com.example.festivapp.ui.main;

import android.view.Gravity;
import android.widget.Toast;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    // Interesante el LiveData, aquí es donde haremos las consultas adecuadas a la BD

    // En este caso cada vez que cambia el index cambia el texto, esto se provoca desde el PlaceholderFragment

    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {

            // Aqui igual se podria poner un switch en funcion del input
            String seccion = "";

            switch(input) {
                case 1:
                    seccion = "Mis Festivales";
                    break;
                case 2:
                    seccion = "Descubrir";
                    break;
            }

            return "Hello world from section: " + seccion;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}