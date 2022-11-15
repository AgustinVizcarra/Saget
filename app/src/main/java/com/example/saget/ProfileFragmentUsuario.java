package com.example.saget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ProfileFragmentUsuario extends Fragment {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference imageRef = firebaseStorage.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_usuario, container, false);

        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //String correoUsuarioPrestamo = currentUser.getEmail();
        String correoUsuarioPrestamo = "a20191566@pucp.edu.pe";


        ImageButton botonCamara = view.findViewById(R.id.imageButton6);
        Button botonActualizar = view.findViewById(R.id.buttonactualizardata);
        EditText nombresTxT = view.findViewById(R.id.editTextTextPersonName2);
        EditText apellidosTxt = view.findViewById(R.id.editTextTextPersonName3);
        EditText correoTxt = view.findViewById(R.id.editTextTextPersonName4);
        EditText dniTxt = view.findViewById(R.id.editTextTextPersonName5);


        databaseReference.child("usuario").orderByChild("correo").equalTo("a20191566@pucp.edu.pe").addListenerForSingleValueEvent(new ValueEventListener() {
            String keyUsuario;
            Usuario user;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren()){
                    Usuario usuario = children.getValue(Usuario.class);
                    boolean igualCorreo = usuario.getCorreo().equals(correoUsuarioPrestamo);
                    if(igualCorreo){
                        keyUsuario = children.getKey();
                        user = usuario;
                        break;
                    }

                }

                botonCamara.setVisibility(View.INVISIBLE);

                nombresTxT.setText(user.getNombres());
                nombresTxT.setFocusable(false);

                apellidosTxt.setText(user.getApellidos());
                apellidosTxt.setFocusable(false);

                correoTxt.setText(user.getCorreo());
                correoTxt.setFocusable(false);

                dniTxt.setText(keyUsuario);
                dniTxt.setFocusable(false);


                botonActualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botonActualizar.setText("Guardar");
                        botonCamara.setVisibility(View.VISIBLE);

                        nombresTxT.setFocusableInTouchMode(true);

                        apellidosTxt.setFocusableInTouchMode(true);


                        botonActualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String nombresActualizados = nombresTxT.getText().toString();
                                String apellidosActualizados = apellidosTxt.getText().toString();

                                databaseReference.child("usuario").child(keyUsuario).child("nombres").setValue(nombresActualizados);
                                databaseReference.child("usuario").child(keyUsuario).child("apellidos").setValue(apellidosActualizados);


                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new ProfileFragmentUsuario()).addToBackStack(null).commit();

                            }
                        });


                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message
            }
        });

        return view;
    }
}