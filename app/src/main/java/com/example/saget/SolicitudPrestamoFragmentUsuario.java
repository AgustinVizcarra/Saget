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
    private String caracteristicas;
    private int disponibilidad;
    private String equiposAdicionales;
    private String marca;
    private String nombre;
    private int stock;
    private int tipo;
    private String estado;
    FirebaseDatabase firebaseDatabase;

    public SolicitudPrestamoFragmentUsuario(String marca,int stock,String nombre,int disponibilidad,String caracteristicas,String equiposAdicionales,int tipo,String estado){
        this.caracteristicas = caracteristicas;
        this.disponibilidad = disponibilidad;
        this.estado = estado;
        this.equiposAdicionales = equiposAdicionales;
        this.marca = marca;
        this.nombre = nombre;
        this.stock = stock;
        this.tipo = tipo;
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

        final String[] nombreTipo = {""};

        TextView nombreEquipoPrestamo = view.findViewById(R.id.nombreEquipoSolicitud);

        databaseReference.child("tipo_equipo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren()){
                    if(children.getKey().equalsIgnoreCase(String.valueOf(tipo))){
                        nombreTipo[0] = children.child("nombre").getValue(String.class);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
            }
        });

        nombreEquipoPrestamo.setText(String.valueOf(nombreTipo[0] + " " + marca + " " + nombre));

        return  view;
    }

    public void botonRetrocoderSolicitud(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new DetalleEquipoFragmentUsuario(marca,stock,nombre,disponibilidad,caracteristicas,equiposAdicionales,tipo,estado)).addToBackStack(null).commit();
    }

}