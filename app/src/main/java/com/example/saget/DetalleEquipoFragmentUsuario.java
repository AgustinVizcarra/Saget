package com.example.saget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetalleEquipoFragmentUsuario extends Fragment {
    private Equipo equipo;
    private String key;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://saget-d5557-default-rtdb.firebaseio.com/");
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public DetalleEquipoFragmentUsuario(String keyEquipo){
        this.key = keyEquipo;
    }

    public DetalleEquipoFragmentUsuario(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View botonRetrocederListaEquipos = view.findViewById(R.id.imageButton5);
        botonRetrocederListaEquipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonRetrocederListaEquipos(view);
            }
        });

        View botonReservar = view.findViewById(R.id.button);
        botonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonReservarDetalleEquipo(view);
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_equipo_usuario, container, false);
        TextView nameDetalleEquipo = view.findViewById(R.id.textView5);
        TextView stockDetalleEquipo = view.findViewById(R.id.textView7);
        TextView marcaDetalleEquipo = view.findViewById(R.id.editTextTextPersonName2);
        TextView caracteristicasDetalleEquipo = view.findViewById(R.id.editTextTextPersonName3);
        TextView incluyeDetalleEquipo = view.findViewById(R.id.editTextTextPersonName4);


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
                        nameDetalleEquipo.setText(String.valueOf(equipo.getNombre()));
                        stockDetalleEquipo.setText(String.valueOf(equipo.getStock()));
                        marcaDetalleEquipo.setText(String.valueOf(equipo.getMarca()));
                        caracteristicasDetalleEquipo.setText(String.valueOf(equipo.getCaracteristicas()));
                        incluyeDetalleEquipo.setText(String.valueOf(equipo.getEquiposAdicionales()));
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

    public void botonRetrocederListaEquipos(View view){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
    }

    public void botonReservarDetalleEquipo(View view){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new SolicitudPrestamoFragmentUsuario(key)).addToBackStack(null).commit();
    }
}
