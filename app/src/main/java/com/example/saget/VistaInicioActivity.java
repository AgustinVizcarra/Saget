package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class VistaInicioActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference referenceUser,referenceTi,referenceAdmin;
    String userCorreo;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_inicio);
        referenceUser= FirebaseDatabase.getInstance().getReference("usuario");
        referenceTi= FirebaseDatabase.getInstance().getReference("ti");
        referenceAdmin= FirebaseDatabase.getInstance().getReference("admin");
    }

    public void redireccionRegistro(View view){
        Intent i = new Intent(this,RegistroActivity.class);
        startActivity(i);
    }
    public void redireccionLogueo(View view){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
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
                                    referenceAdmin.child(userID).child("correo").equalTo(userCorreo).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int k=0;
                                            for(DataSnapshot ds:snapshot.getChildren()){
                                                k++;
                                            }
                                            if(k==0){
                                                Toast.makeText(VistaInicioActivity.this,"No existe una cuenta asociada al correo",Toast.LENGTH_SHORT).show();
                                            }else{
                                                startActivity(new Intent(VistaInicioActivity.this, VistaPrincipalAdminActivity.class));
                                                finish();
                                                Toast.makeText(VistaInicioActivity.this,"Bienvenido Admin",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(VistaInicioActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    startActivity(new Intent(VistaInicioActivity.this, VistaPrincipalTIActivity.class));
                                    finish();
                                    Toast.makeText(VistaInicioActivity.this,"Bienvenido Usuario TI",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(VistaInicioActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        startActivity(new Intent(VistaInicioActivity.this, VistaPrincipalUsuarioActivity.class));
                        finish();
                        Toast.makeText(VistaInicioActivity.this,"Bienvenido Usuario",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(VistaInicioActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }


    }
}