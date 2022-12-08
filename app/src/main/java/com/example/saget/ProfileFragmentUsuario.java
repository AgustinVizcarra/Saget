package com.example.saget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;


public class ProfileFragmentUsuario extends Fragment {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference imageRef = firebaseStorage.getReference();
    ImageView fondoPerfil;
    FirebaseAuth mAuth;
    Bitmap imgBitMap;
    Usuario usuario;
    String keyUsuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_usuario, container, false);

        mAuth= FirebaseAuth.getInstance();
        //PARA SACAR EL CORREO DEL USUARIO LOGUEADO
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String correoUsuarioPrestamo = currentUser.getEmail();
        //String correoUsuarioPrestamo = "a20191566@pucp.edu.pe";

        ImageButton botonCamara = view.findViewById(R.id.imageButton6);
        Button botonActualizar = view.findViewById(R.id.buttonactualizardata);
        EditText nombresTxT = view.findViewById(R.id.editTextTextPersonName2);
        EditText apellidosTxt = view.findViewById(R.id.editTextTextPersonName3);
        EditText correoTxt = view.findViewById(R.id.editTextTextPersonName4);
        EditText dniTxt = view.findViewById(R.id.editTextTextPersonName5);
        fondoPerfil = view.findViewById(R.id.imagenperfilusuario);

        databaseReference.child("usuario").orderByChild("correo").equalTo(correoUsuarioPrestamo).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren()){
                    usuario = children.getValue(Usuario.class);
                    keyUsuario = children.getKey();
                   break;
                }

                botonCamara.setVisibility(View.INVISIBLE);

                nombresTxT.setText(usuario.getNombres());
                nombresTxT.setFocusable(false);

                apellidosTxt.setText(usuario.getApellidos());
                apellidosTxt.setFocusable(false);

                correoTxt.setText(usuario.getCorreo());
                correoTxt.setFocusable(false);

                dniTxt.setText(usuario.getDNI());
                dniTxt.setFocusable(false);

                if(usuario.getFoto() != null){
                    String ruta = (String) usuario.getFoto().get(1);
                    fondoPerfil.setBackground(null);
                    Glide.with(getContext()).load(ruta).override(100,125).into(fondoPerfil);
                }


                botonActualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botonActualizar.setText("Guardar");
                        botonCamara.setVisibility(View.VISIBLE);

                        nombresTxT.setFocusableInTouchMode(true);

                        apellidosTxt.setFocusableInTouchMode(true);


                        botonCamara.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,1);
                            }
                        });



                        botonActualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean guardar = true;

                                try{
                                    String nombresActualizados = nombresTxT.getText().toString();
                                    String apellidosActualizados = apellidosTxt.getText().toString();

                                    if(nombresActualizados.equalsIgnoreCase("") || nombresActualizados == null || nombresActualizados.isEmpty()){
                                        nombresTxT.setError("Ingrese su nombre");
                                        guardar = false;
                                    }

                                    if(apellidosActualizados.equalsIgnoreCase("") || apellidosActualizados == null || apellidosActualizados.isEmpty()){
                                        apellidosTxt.setError("Ingrese su apellido");
                                        guardar = false;
                                    }

                                    if(guardar){
                                        databaseReference.child("usuario").child(keyUsuario).child("nombres").setValue(nombresActualizados);
                                        databaseReference.child("usuario").child(keyUsuario).child("apellidos").setValue(apellidosActualizados);

                                        if(imgBitMap != null){
                                            String idFoto = getRandomString();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            imgBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                            byte[] data = baos.toByteArray();

                                            imageRef.child(idFoto+".jpg").putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    imageRef.child(idFoto+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String url = String.valueOf(uri);
                                                            HashMap<String, Object> valorcito = new HashMap<>();

                                                            valorcito.put("1",url);

                                                            databaseReference.child("usuario").child(keyUsuario).child("foto").setValue(valorcito);

                                                        }
                                                    });

                                                }
                                            });

                                        }

                                        Toast.makeText(getActivity(),"Datos actualizados",Toast.LENGTH_SHORT).show();
                                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new ProfileFragmentUsuario()).addToBackStack(null).commit();

                                    }else{

                                        Toast.makeText(getActivity(),"Campos Incorrectos!",Toast.LENGTH_SHORT).show();
                                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new ProfileFragmentUsuario()).addToBackStack(null).commit();

                                    }

                                }catch (Exception e){
                                    Toast.makeText(getActivity(),"Todos los campos son obligatorios!",Toast.LENGTH_SHORT).show();
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new ProfileFragmentUsuario()).addToBackStack(null).commit();
                                }

                            }
                        });


                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(),"An error has ocurred!",Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            Bundle extras = data.getExtras();
            imgBitMap = (Bitmap) extras.get("data");
            fondoPerfil.setBackground(null);
            fondoPerfil.setImageBitmap(imgBitMap);

        }
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