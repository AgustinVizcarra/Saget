package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class VistaPrincipalTIActivity extends AppCompatActivity {

    Fragment fragmentInicioTI = new InicioFragmentTi();
    Fragment fragmentProfileTI=new ProfileFragmentTi();
    Fragment fragmentSolicTI=new SolicitudesFragmentTi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal_tiactivity);
        BottomNavigationView bottomNavigationViewTI = findViewById(R.id.bottom_navigation_ti);

        loadFragment(fragmentInicioTI);
        bottomNavigationViewTI.setOnItemSelectedListener(item ->{
            switch(item.getItemId()){
             case R.id.house_ti:
                 loadFragment(fragmentInicioTI);
                 return true;
             case R.id.profile_ti:
                 loadFragment(fragmentProfileTI);
                 return true;
             case R.id.request_ti:
                 loadFragment(fragmentSolicTI);
                 return true;
             default:
                return false;
            }
        });
        ImageView btnLogout=findViewById(R.id.ic_logout_ti);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deslogueo
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(VistaPrincipalTIActivity.this, VistaInicioActivity.class));
                finish();
            }
        });


    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_TI,fragment);
        transaction.commit();
    }



}