package com.ulbra.bancodesenhas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;

public class MenuActivity extends AppCompatActivity {
    Firebase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_menu);


    }
}
