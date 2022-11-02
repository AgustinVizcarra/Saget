package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class VistaPrincipalUsuarioActivity extends AppCompatActivity {

    InicioFragmentUsuario inicioFragmentUsuario = new InicioFragmentUsuario();
    RequestFragmentUsuario requestFragmentUsuario = new RequestFragmentUsuario();
    ProfileFragmentUsuario profileFragmentUsuario = new ProfileFragmentUsuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal_usuario);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation_user);

        loadFragment(inicioFragmentUsuario);

        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.house_user:
                    loadFragment(inicioFragmentUsuario);
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