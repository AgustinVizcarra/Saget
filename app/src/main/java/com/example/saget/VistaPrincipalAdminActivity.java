package com.example.saget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class VistaPrincipalAdminActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    Fragment fragmentInicioAdmin,fragmentEstadisticasAdmin,fragmentUsuarios,fragmentPuntosRecojo,fragmentPrueba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal_admin);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
        fragmentInicioAdmin = new InicioAdminFragment();
        fragmentEstadisticasAdmin = new EstadisticasAdminFragment();
        fragmentUsuarios = new UsuariosFragment();
        fragmentPuntosRecojo = new PuntosRecojoFragment();
        loadFragment(fragmentInicioAdmin);
        bottomNavigationView.setOnItemSelectedListener(item->{
            switch(item.getItemId()){
                case R.id.house_admin:
                    loadFragment(fragmentInicioAdmin);
                    return true;
                case R.id.estadisticas_admin:
                    loadFragment(fragmentEstadisticasAdmin);
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
        ImageView btnLogout=findViewById(R.id.isalir);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deslogueo
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(VistaPrincipalAdminActivity.this, VistaInicioActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_admin,fragment);
        transaction.commit();
    }
}