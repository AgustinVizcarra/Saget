package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class VistaPrincipalUsuarioActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    CatalogoUsuarioFragment catalogoFragmentUsuario = new CatalogoUsuarioFragment();
    RequestFragmentUsuario requestFragmentUsuario = new RequestFragmentUsuario();
    ProfileFragmentUsuario profileFragmentUsuario = new ProfileFragmentUsuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal_usuario);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_user);

        loadFragment(catalogoFragmentUsuario);

        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.house_user:
                    loadFragment(catalogoFragmentUsuario);
                    return true;
                case R.id.request_user:
                    loadFragment(requestFragmentUsuario);
                    return true;
                case R.id.profile_user:
                    loadFragment(profileFragmentUsuario);
                    return true;
            }
            return false;
        });

        ImageButton botonSalirSesion = findViewById(R.id.isalir);
        botonSalirSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                Intent i = new Intent(VistaPrincipalUsuarioActivity.this,VistaInicioActivity.class);//Vista Logueo
                startActivity(i);
            }
        });


    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_user,fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
    }
}