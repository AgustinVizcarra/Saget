package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_TI,fragment);
        transaction.commit();
    }

}