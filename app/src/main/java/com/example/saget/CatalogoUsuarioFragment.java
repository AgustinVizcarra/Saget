package com.example.saget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class CatalogoUsuarioFragment extends Fragment {


    public CatalogoUsuarioFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_catalogo_usuario, container, false);
        ImageView imageTabletUser = view.findViewById(R.id.imageTabletUser);
        imageTabletUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user, new InicioFragmentUsuario("1")).addToBackStack(null).commit();
            }
        });

        ImageView imageLaptopUser = view.findViewById(R.id.imageLaptopUser);
        imageLaptopUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario("2")).addToBackStack(null).commit();
            }
        });

        ImageView imageCelularUSer = view.findViewById(R.id.imageCelularUser);
        imageCelularUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario("3")).addToBackStack(null).commit();
            }
        });
        ImageView iamgeMonitorUser = view.findViewById(R.id.imageMonitorUser);
        iamgeMonitorUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario("4")).addToBackStack(null).commit();
            }
        });

        ImageView imageOtrosUser = view.findViewById(R.id.imageOtrosUser);
        imageOtrosUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario("5")).addToBackStack(null).commit();
            }
        });
        return view;
    }
}