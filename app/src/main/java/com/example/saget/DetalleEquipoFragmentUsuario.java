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

public class DetalleEquipoFragmentUsuario extends Fragment {
    private String caracteristicas;
    private int disponibilidad;
    private String equiposAdicionales;
    private String marca;
    private String nombre;
    private int stock;
    private int tipo;
    private String estado;

    public DetalleEquipoFragmentUsuario(String marca,int stock,String nombre,int disponibilidad,String caracteristicas,String equiposAdicionales,int tipo,String estado){
        this.caracteristicas = caracteristicas;
        this.estado = estado;
        this.disponibilidad = disponibilidad;
        this.equiposAdicionales = equiposAdicionales;
        this.marca = marca;
        this.nombre = nombre;
        this.stock = stock;
        this.tipo = tipo;
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

        nameDetalleEquipo.setText(String.valueOf(nombre));
        stockDetalleEquipo.setText(String.valueOf(stock));
        marcaDetalleEquipo.setText(String.valueOf(marca));
        caracteristicasDetalleEquipo.setText(String.valueOf(caracteristicas));
        incluyeDetalleEquipo.setText(String.valueOf(equiposAdicionales));

        return  view;
    }

    public void botonRetrocederListaEquipos(View view){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
    }

    public void botonReservarDetalleEquipo(View view){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new SolicitudPrestamoFragmentUsuario(marca,stock,nombre,disponibilidad,caracteristicas,equiposAdicionales,tipo,estado)).addToBackStack(null).commit();
    }
}
