package com.example.saget;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class UpdateSolicitudFragmentUsuario extends Fragment {
    SolicitudDePrestamo solicitudUsuario;
    String solicitudID;
    Equipo equipoUsuario;
    String keyEquipo;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ImageButton botonCamara;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference imageRef = firebaseStorage.getReference();
    ImageView fondoDNI;
    Bitmap imgBitMap;
    FirebaseAuth mAuth;

    public UpdateSolicitudFragmentUsuario(String solicitudID,SolicitudDePrestamo solicitudDePrestamo,String keyEquipo,Equipo equipo) {
        this.solicitudUsuario = solicitudDePrestamo;
        this.solicitudID = solicitudID;
        this.equipoUsuario = equipo;
        this.keyEquipo = keyEquipo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View botonRetrocederHistorialSolicitud = view.findViewById(R.id.botonretrocerderSolicitud);
        botonRetrocederHistorialSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonRetrocoderSolicitud(view);
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
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
        fondoDNI = view.findViewById(R.id.fondoDNI);

        TextView titulo = view.findViewById(R.id.textView5);
        titulo.setText("ACTUALIZAR PRÉSTAMO");

        Button botonSolicitarPrestamo = view.findViewById(R.id.solicitarprestamoboton);
        botonSolicitarPrestamo.setText("ACTUALIZAR");

        botonCamara = view.findViewById(R.id.imageButton6);
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });


        //Falta setear los demas tipos de equipos
        if(equipoUsuario.getTipo() == 1){
            nombreEquipoPrestamo.setText(String.valueOf("LAPTOP " + equipoUsuario.getMarca() + " " + equipoUsuario.getNombre()));
        }

        tiempoPrestamoText.setText(solicitudUsuario.getTiempoPrestamo());
        cursoText.setText(solicitudUsuario.getCurso());
        programasText.setText(solicitudUsuario.getProgramas());
        motivoText.setText(solicitudUsuario.getMotivo());
        detallesText.setText(solicitudUsuario.getDetalles());


        imageRef.child(solicitudUsuario.getFoto()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).override(260,140).into(fondoDNI);

                Glide.with(getContext())
                        .asBitmap()
                        .load(uri)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                imgBitMap = resource;
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

            }
        });

        botonSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tiempoPrestamo;
                String curso;
                String programas;
                String motivo;
                String detalles;
                boolean guardar = true;

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

                    if(imgBitMap.equals("") || imgBitMap == null){
                        guardar = false;
                    }


                    if(guardar){

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imgBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        //Se puede borrar los metodos -> ya seria verlo
                        UploadTask uploadTask = imageRef.child(solicitudUsuario.getFoto()+".jpg").putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //error message
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //se subio correctamente
                            }
                        });


                        //FirebaseUser currentUser = mAuth.getCurrentUser();
                        //String correoUsuarioPrestamo = currentUser.getEmail();
                        String correoUsuarioPrestamo = "a20191566@pucp.edu.pe";

                        databaseReference.child("usuario").addListenerForSingleValueEvent(new ValueEventListener() {
                            String keyUsuario;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot children : snapshot.getChildren()){
                                    Usuario usuario = children.getValue(Usuario.class);
                                    boolean igualCorreo = usuario.getCorreo().equals(correoUsuarioPrestamo);
                                    if(igualCorreo){
                                        keyUsuario = children.getKey();
                                        break;
                                    }

                                }

                                databaseReference.child("prestamos/"+solicitudID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        SolicitudDePrestamo solicitudDePrestamo = snapshot.getValue(SolicitudDePrestamo.class);
                                        if(solicitudDePrestamo.getEstado().equals("En trámite")){
                                            SolicitudDePrestamo solicitudDePrestamo2 = new SolicitudDePrestamo(keyUsuario,keyEquipo,tiempoPrestamo,curso,programas,motivo,detalles,solicitudUsuario.getFoto(),"En trámite",null);
                                            databaseReference.child("prestamos").child(solicitudID).setValue(solicitudDePrestamo2);
                                            System.out.println("ACTUALIZO TU WBD");
                                        }else{
                                            //imprimir mensaje diciendole que ya ha sido recepcionado su solicitud y no puede hacer cambios

                                        }

                                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();
                                    }
                                });


                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                //error message
                            }
                        });


                    }else{
                        //message error -> campos incorrectos
                        System.out.println("NO ENTRE AL IF ");
                    }

                }catch (Exception e){
                    System.out.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRR");
                    //error message
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new RequestFragmentUsuario()).addToBackStack(null).commit();
                }

            }
        });


        return  view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imgBitMap = (Bitmap) extras.get("data");

            //Efecto Blur
            imgBitMap = getBlurImage(imgBitMap);

            fondoDNI.setImageBitmap(imgBitMap);
        }
    }




    public Bitmap getBlurImage(Bitmap imagenBitMap){
        Bitmap salidaBitmap = Bitmap.createBitmap(imagenBitMap);
        final RenderScript renderScript = RenderScript.create(getContext());
        Allocation entradaTemp = Allocation.createFromBitmap(renderScript,imagenBitMap);
        Allocation salidaTemp = Allocation.createFromBitmap(renderScript,salidaBitmap);

        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        scriptIntrinsicBlur.setRadius(25f);
        scriptIntrinsicBlur.setInput(entradaTemp);
        scriptIntrinsicBlur.forEach(salidaTemp);
        salidaTemp.copyTo(salidaBitmap);

        return salidaBitmap;
    }

    public void botonRetrocoderSolicitud(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new HistorialPrestamoFragmentUsuario(solicitudID)).addToBackStack(null).commit();
    }


    public String getRandomString() {
        String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
        int sizeOfRandomString = 15;
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }








}