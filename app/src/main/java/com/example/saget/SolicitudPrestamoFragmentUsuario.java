package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SolicitudPrestamoFragmentUsuario extends Fragment {
    private String key;
    private Equipo equipo;
    FirebaseDatabase firebaseDatabase;

    public SolicitudPrestamoFragmentUsuario(String keyEquipo){
        this.key = keyEquipo;
    }

    public SolicitudPrestamoFragmentUsuario(){

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View botonRetrocederListaEquipos = view.findViewById(R.id.botonretrocerderSolicitud);
        botonRetrocederListaEquipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonRetrocoderSolicitud(view);
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance("https://saget-d5557-default-rtdb.firebaseio.com/");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitud_prestamo_usuario, container, false);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        TextView nombreEquipoPrestamo = view.findViewById(R.id.nombreEquipoSolicitud);

        databaseReference.child("equipo/"+key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Equipo equipoBanderita = snapshot.getValue(Equipo.class);
                    if(equipoBanderita.getEstado().equals("0_"+equipoBanderita.getTipo()) || equipoBanderita.getStock() == 0){
                        //error message
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();

                    }else{
                        equipo = equipoBanderita;

                        //laptop
                        if(equipo.getTipo() == 1){
                            nombreEquipoPrestamo.setText(String.valueOf("LAPTOP " + equipo.getMarca() + " " + equipo.getNombre()));
                        }




                    }

                }else{
                    //error message
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
            }
        });


        return  view;
    }

    public void botonRetrocoderSolicitud(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new DetalleEquipoFragmentUsuario(key)).addToBackStack(null).commit();
    }

}