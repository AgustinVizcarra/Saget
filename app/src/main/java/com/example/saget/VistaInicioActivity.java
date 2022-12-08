package com.example.saget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class VistaInicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_inicio);
    }

    public void redireccionRegistro(View view){
        Intent i = new Intent(this,RegistroActivity.class);
        startActivity(i);
    }
    public void redireccionLogueo(View view){

        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);

    }
}