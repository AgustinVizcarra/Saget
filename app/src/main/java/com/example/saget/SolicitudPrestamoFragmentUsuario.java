package com.example.saget;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsic;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class SolicitudPrestamoFragmentUsuario extends Fragment {
    private String key;
    private Equipo equipo;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ImageButton botonCamara;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference imageRef = firebaseStorage.getReference();
    ImageView fondoDNI;
    Bitmap imgBitMap;
    FirebaseAuth mAuth;

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

        Button botonSolicitarPrestamo = view.findViewById(R.id.solicitarprestamoboton);

        botonCamara = view.findViewById(R.id.imageButton6);
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });


        databaseReference.child("equipo/"+key).addValueEventListener(new ValueEventListener() {
            String tiempoPrestamo;
            String curso;
            String programas;
            String motivo;
            String detalles;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Equipo equipoBanderita = snapshot.getValue(Equipo.class);
                    equipo = equipoBanderita;

                    switch (equipo.getTipo()){
                        case 1:
                            nombreEquipoPrestamo.setText(String.valueOf("TABLET " + equipo.getMarca() + " " + equipo.getNombre()));
                            break;
                        case 2:
                            nombreEquipoPrestamo.setText(String.valueOf("LAPTOP " + equipo.getMarca() + " " + equipo.getNombre()));
                            break;
                        case 3:
                            nombreEquipoPrestamo.setText(String.valueOf("CELULAR " + equipo.getMarca() + " " + equipo.getNombre()));
                            break;
                        case 4:
                            nombreEquipoPrestamo.setText(String.valueOf("MONITOR " + equipo.getMarca() + " " + equipo.getNombre()));
                            break;
                        default:
                            nombreEquipoPrestamo.setText(String.valueOf("OTROS " + equipo.getMarca() + " " + equipo.getNombre()));
                            break;
                    }

                    botonSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(equipo.getEstado().equals("0_"+equipo.getTipo()) || equipo.getStock() == 0){

                                Toast.makeText(getActivity(),"El equipo ya no se encuentra disponible",Toast.LENGTH_SHORT).show();
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario(String.valueOf(equipo.getTipo()))).addToBackStack(null).commit();

                            }else{
                                boolean guardar = true;
                                try {
                                    tiempoPrestamo = tiempoPrestamoText.getText().toString();
                                    curso = cursoText.getText().toString();
                                    programas = programasText.getText().toString();
                                    motivo = motivoText.getText().toString();
                                    detalles = detallesText.getText().toString();

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
                                        int stockRestante = equipo.getStock() - 1;
                                        if(stockRestante > 0){
                                            databaseReference.child("equipo/"+key).child("stock").setValue(stockRestante);

                                        }else{
                                            databaseReference.child("equipo/"+key).child("stock").setValue(0);
                                            databaseReference.child("equipo/"+key).child("disponibilidad").setValue(0);
                                            databaseReference.child("equipo/"+key).child("estado").setValue("0_"+equipo.getTipo());
                                        }


                                        String idFoto = getRandomString();

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        imgBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] data = baos.toByteArray();
                                        imageRef.child(idFoto+".jpg").putBytes(data);


                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        String correoUsuarioPrestamo = currentUser.getEmail();

                                        databaseReference.child("usuario").child("correo").equalTo(correoUsuarioPrestamo).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot != null){
                                                    String keyUsuario = snapshot.getKey();
                                                    DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy, HH:mm:ss ");

                                                    String date = dateFormat.format(new Date());
                                                    SolicitudDePrestamo solicitudDePrestamo = new SolicitudDePrestamo(keyUsuario,key,tiempoPrestamo,curso,programas,motivo,detalles,idFoto,"En trámite",null,date);
                                                    databaseReference.child("prestamos").push().setValue(solicitudDePrestamo);

                                                    Toast.makeText(getActivity(),"Reserva exitosa",Toast.LENGTH_SHORT).show();
                                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario(String.valueOf(equipo.getTipo()))).commit();

                                                }else{

                                                    Toast.makeText(getActivity(),"An error has ocurred!",Toast.LENGTH_SHORT).show();
                                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new DetalleEquipoFragmentUsuario(key)).addToBackStack(null).commit();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(getActivity(),"An error has ocurred!",Toast.LENGTH_SHORT).show();
                                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new DetalleEquipoFragmentUsuario(key)).addToBackStack(null).commit();
                                            }
                                        });

                                    }else{

                                        Toast.makeText(getActivity(),"Campos incorrectos!",Toast.LENGTH_SHORT).show();

                                    }

                                }catch (Exception e){

                                    Toast.makeText(getActivity(),"Todos los campos son obligatorios!",Toast.LENGTH_SHORT).show();
                                    //AppCompatActivity activity = (AppCompatActivity) getContext();
                                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new SolicitudPrestamoFragmentUsuario(key)).addToBackStack(null).commit();

                                }

                            }

                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"An error has ocurred!",Toast.LENGTH_SHORT).show();
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new CatalogoUsuarioFragment()).addToBackStack(null).commit();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(),"An error has ocurred!",Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new CatalogoUsuarioFragment()).addToBackStack(null).commit();

            }
        });


        return  view;
    }

    //VER EL TEMA DEL BLUR Y DE AGRANDAR LA IMAGEN AL DARLE CLICK
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imgBitMap = (Bitmap) extras.get("data");

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
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new DetalleEquipoFragmentUsuario(key)).addToBackStack(null).commit();
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