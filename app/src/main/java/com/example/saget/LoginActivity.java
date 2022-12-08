package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

     DatabaseReference referenceUser,referenceTi,referenceAdmin;
     FirebaseAuth auth;
     String userCorreo;
     EditText editTextCorreoLogueo,editTextPasswordLogueo;
     Button botonIngresarLogueo;
     TextView olvidoContra;
     String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        //para firebase
        auth=FirebaseAuth.getInstance();
        referenceUser= FirebaseDatabase.getInstance().getReference("usuario");
        referenceTi= FirebaseDatabase.getInstance().getReference("ti");
        referenceAdmin= FirebaseDatabase.getInstance().getReference("admin");
        editTextCorreoLogueo=findViewById(R.id.editTextCorreoLogueo);
        editTextPasswordLogueo=findViewById(R.id.editTextPasswordLogueo);
        botonIngresarLogueo=findViewById(R.id.botonIngresarLogueo);
        olvidoContra=findViewById(R.id.clickOlvidoContrasenia);




    }


    public void btnIngresar(View view){
        String email=editTextCorreoLogueo.getText().toString().trim();
        String password=editTextPasswordLogueo.getText().toString();
        if(email.isEmpty()){
            editTextCorreoLogueo.setError("Correo es obligatorio");
            editTextCorreoLogueo.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextCorreoLogueo.setError("Ingrese correo válido");
            editTextCorreoLogueo.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPasswordLogueo.setError("Contraseña es obligatorio");
            editTextPasswordLogueo.requestFocus();
            return;
        }
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    userCorreo=user.getEmail();
                    userID=user.getUid();
                    Log.d("msj-login",userCorreo);
                    Log.d("msj-login1",userID);
                    referenceUser.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int j=0;
                            for(DataSnapshot ds:snapshot.getChildren()){
                                j++;
                            }
                            if(j==0){
                                referenceTi.child(userID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("msj-login2",snapshot.getValue(String.class));
                                        int i=0;
                                        for(DataSnapshot ds:snapshot.getChildren()){
                                            i++;
                                        }
                                        if(i==0){
                                            referenceAdmin.child(userID).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int k=0;
                                                    for(DataSnapshot ds:snapshot.getChildren()){
                                                        k++;
                                                    }
                                                    if(k==0){
                                                        Toast.makeText(LoginActivity.this,"No existe una cuenta asociada al correo",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        startActivity(new Intent(LoginActivity.this, VistaPrincipalAdminActivity.class));
                                                        finish();
                                                        Toast.makeText(LoginActivity.this,"Bienvenido Admin",Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(LoginActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            startActivity(new Intent(LoginActivity.this, VistaPrincipalTIActivity.class));
                                            finish();
                                            Toast.makeText(LoginActivity.this,"Bienvenido Usuario TI",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LoginActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                startActivity(new Intent(LoginActivity.this, VistaPrincipalUsuarioActivity.class));
                                finish();
                                Toast.makeText(LoginActivity.this,"Bienvenido Usuario",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this,"Correo o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}