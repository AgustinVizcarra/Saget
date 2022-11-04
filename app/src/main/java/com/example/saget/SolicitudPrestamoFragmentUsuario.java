package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://saget-d5557-default-rtdb.firebaseio.com/");
    DatabaseReference databaseReference = firebaseDatabase.getReference();

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitud_prestamo_usuario, container, false);

        TextView nombreEquipoPrestamo = view.findViewById(R.id.nombreEquipoSolicitud);

        TextView tiempoPrestamoText = view.findViewById(R.id.tiempoPrestamo);
        TextView cursoText = view.findViewById(R.id.cursosolicitud);
        TextView programasText = view.findViewById(R.id.programassolicitud);
        TextView motivoText = view.findViewById(R.id.motivosolicitud);
        TextView detallesText = view.findViewById(R.id.detallessolicitud);

        Button botonSolicitarPrestamo = view.findViewById(R.id.solicitarprestamoboton);


        databaseReference.child("equipo/"+key).addValueEventListener(new ValueEventListener() {
            String tiempoPrestamo;
            String curso;
            String programas;
            String motivo;
            String detalles;
            boolean guardar = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Equipo equipoBanderita = snapshot.getValue(Equipo.class);
                    equipo = equipoBanderita;

                    //seteo el nombre de acuerdo al tipo de equipo
                    //laptop
                    if(equipo.getTipo() == 1){
                        nombreEquipoPrestamo.setText(String.valueOf("LAPTOP " + equipo.getMarca() + " " + equipo.getNombre()));
                    }


                    botonSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(equipo.getEstado().equals("0_"+equipo.getTipo()) || equipo.getStock() == 0){
                                //error message -> no hay stock
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
                            }else{

                                try {

                                    tiempoPrestamo = tiempoPrestamoText.getText().toString();
                                    curso = cursoText.getText().toString();
                                    programas = programasText.getText().toString();
                                    motivo = motivoText.getText().toString();
                                    detalles = detallesText.getText().toString();


                                    //Valido esos campos
                                    if(tiempoPrestamo.equalsIgnoreCase("") || tiempoPrestamo == null || tiempoPrestamo.isEmpty()){
                                        tiempoPrestamoText.setError("Ingrese su tiempo de prestamo");
                                        guardar = false;
                                    }

                                    if(curso.equalsIgnoreCase("") || curso == null || curso.isEmpty()){
                                        cursoText.setError("Ingrese el curso para el que lo necesita");
                                        guardar = false;
                                    }

                                    if(programas.equalsIgnoreCase("") || programas == null || programas.isEmpty()){
                                        programasText.setError("Ingrese al menos un programa que necesita");
                                        guardar = false;
                                    }

                                    if(motivo.equalsIgnoreCase("") || motivo == null || motivo.isEmpty()){
                                        motivoText.setError("Ingrese el motivo del prestamo");
                                        guardar = false;
                                    }

                                    if(detalles.equalsIgnoreCase("") || detalles == null || detalles.isEmpty()){
                                        detallesText.setError("Ingrese detalles adicionales");
                                        guardar = false;
                                    }


                                    if(guardar){
                                        //Quito en 1 el stock o inhabilito el stock dependiendo de como este el stock -> Hago update en db del equip
                                        int stockRestante = equipo.getStock() - 1;
                                        if(stockRestante > 0){
                                            databaseReference.child("equipo/"+key).child("stock").setValue(stockRestante);

                                        }else{
                                            //STOCK 0
                                            databaseReference.child("equipo/"+key).child("stock").setValue(0);
                                            //NO DISPONIBLE
                                            databaseReference.child("equipo/"+key).child("disponibilidad").setValue(0);
                                            //ESTADO
                                            databaseReference.child("equipo/"+key).child("estado").setValue("0_"+equipo.getTipo());
                                        }


                                        //en prestamos del usuario -> necesito el id del usuario y la key del equipo





                                        //Redirigo a fragmento request y muestro su solicitud
                                        //No cambia el color verde del botombar de la casa pintada al archivo pintado
                                        AppCompatActivity activity = (AppCompatActivity) getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();

                                    }else{
                                        //message error -> campos incorrectos

                                    }

                                }catch (Exception e){
                                    //error message
                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
                                }

                            }

                        }
                    });


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