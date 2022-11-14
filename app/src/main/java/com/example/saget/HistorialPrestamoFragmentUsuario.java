package com.example.saget;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class HistorialPrestamoFragmentUsuario extends Fragment {
    String solicitudID;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference imageRef = firebaseStorage.getReference();
    ImageView fondoDNIHistorial;

    public HistorialPrestamoFragmentUsuario(String solicitudID){
        this.solicitudID = solicitudID;
    }

    public HistorialPrestamoFragmentUsuario(){

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View botonRetrocederSolicitudes = view.findViewById(R.id.botonretrocerderSolicitudHistorial);
        botonRetrocederSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonRetrocoderSolicitudHistorial(view);
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
        View view = inflater.inflate(R.layout.fragment_historial_solicitud, container, false);


        TextView nombreEquipoPrestamoHistorial = view.findViewById(R.id.nombreEquipoSolicitudHistorial);
        TextView tiempoPrestamoTextHistorial = view.findViewById(R.id.tiempoPrestamoHistorial);
        TextView cursoTextHistorial = view.findViewById(R.id.cursosolicitudHistorial);
        TextView programasTextHistorial = view.findViewById(R.id.programassolicitudHistorial);
        TextView motivoTextHistorial = view.findViewById(R.id.motivosolicitudHistorial);
        TextView detallesTextHistorial = view.findViewById(R.id.detallessolicitudHistorial);
        fondoDNIHistorial = view.findViewById(R.id.fondoDNIHistorial);

        databaseReference.child("prestamos/"+solicitudID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() != null){
                    SolicitudDePrestamo solicitudDePrestamo = snapshot.getValue(SolicitudDePrestamo.class);

                    tiempoPrestamoTextHistorial.setText(String.valueOf(solicitudDePrestamo.getTiempoPrestamo()));
                    cursoTextHistorial.setText(String.valueOf(solicitudDePrestamo.getCurso()));
                    programasTextHistorial.setText(String.valueOf(solicitudDePrestamo.getProgramas()));
                    motivoTextHistorial.setText(String.valueOf(solicitudDePrestamo.getMotivo()));
                    detallesTextHistorial.setText(String.valueOf(solicitudDePrestamo.getDetalles()));


                    databaseReference.child("equipo/"+solicitudDePrestamo.getEquipo()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue() != null){
                                Equipo equipo = snapshot.getValue(Equipo.class);

                                //seteo el nombre de acuerdo al tipo de equipo
                                //laptop
                                if(equipo.getTipo() == 1){
                                    nombreEquipoPrestamoHistorial.setText(String.valueOf("LAPTOP " + equipo.getMarca() + " " + equipo.getNombre()));
                                }
                                //Falta setear los demas tipos

                                imageRef.child(solicitudDePrestamo.getFoto()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(getContext()).load(uri).override(260,140).into(fondoDNIHistorial);
                                    }
                                });

                            }else{
                                //error message
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //error message
                            AppCompatActivity activity = (AppCompatActivity) getContext();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();
                        }
                    });


                }else{
                    //error message
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();
            }

        });

        return  view;
    }



    public void botonRetrocoderSolicitudHistorial(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();
    }


}
