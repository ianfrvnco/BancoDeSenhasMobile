package com.ulbra.bancodesenhas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000; // 5 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_splash);

        // Handler para esperar alguns segundos antes de abrir a tela principal
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish(); // fecha a Splash para não voltar
            }
        }, SPLASH_TIME_OUT);
    }
}
