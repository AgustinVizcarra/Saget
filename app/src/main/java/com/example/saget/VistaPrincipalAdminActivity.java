package com.example.saget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VistaPrincipalAdminActivity extends AppCompatActivity {

    Fragment fragmentInicioAdmin,fragmentEstadisticas,fragmentUsuarios,fragmentPuntosRecojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal_admin);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
        fragmentInicioAdmin = new InicioAdminFragment();
        fragmentEstadisticas = new EstadisticasFragment();
        fragmentUsuarios = new UsuariosFragment();
        fragmentPuntosRecojo = new PuntosRecojoFragment();
        loadFragment(fragmentInicioAdmin);
        bottomNavigationView.setOnItemSelectedListener(item->{
            switch(item.getItemId()){
                case R.id.house_admin:
                    loadFragment(fragmentInicioAdmin);
                    return true;
                case R.id.estadisticas_admin:
                    loadFragment(fragmentEstadisticas);
                    return true;
                case R.id.users_admin:
                    loadFragment(fragmentUsuarios);
                    return true;
                case R.id.pickup_admin:
                    loadFragment(fragmentPuntosRecojo);
                    return true;
                default:
                    return false;
            }
        });
    }
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_admin,fragment);
        transaction.commit();
    }
}