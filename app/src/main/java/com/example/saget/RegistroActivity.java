package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saget.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistroActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        firebaseDatabase = FirebaseDatabase.getInstance();

        List<String> valuesSpinner = new ArrayList<>();
        valuesSpinner.add(0,"Seleccionar");
        valuesSpinner.add("Estudiante");
        valuesSpinner.add("Profesor");
        valuesSpinner.add("Personal Administrativo");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,valuesSpinner);
        Spinner spinner = findViewById(R.id.spinnerRolRegistro);
        spinner.setAdapter(arrayAdapter);

    }


    public void botonRegistrarse(View view){
        boolean guardar = true;
        String correo = null;
        String cargo = null;

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        try {
            EditText nombresText = findViewById(R.id.nombresRegistro);
            String nombres = nombresText.getText().toString();
            if(nombres.equalsIgnoreCase("") || nombres == null || nombres.isEmpty()){
                nombresText.setError("Ingrese sus nombres");
                guardar = false;
            }

            EditText apellidosText = findViewById(R.id.apellidossRegistro);
            String apellidos = apellidosText.getText().toString();
            if(apellidos.equalsIgnoreCase("") || apellidos == null || apellidos.isEmpty()){
                apellidosText.setError("Ingrese sus apellidos");
                guardar = false;
            }


            EditText dniText = findViewById(R.id.DNI);
            String DNI = dniText.getText().toString();
            if(DNI.equalsIgnoreCase("") || DNI == null || DNI.isEmpty()){
                dniText.setError("Ingrese su DNI");
                guardar = false;
            }else{
                try{
                    int dniHelper = Integer.parseInt(DNI);
                    int length = DNI.length();
                    if(length != 8){
                        dniText.setError("Ingrese un DNI valido");
                        guardar = false;
                    }

                }catch (NumberFormatException e){
                    dniText.setError("Ingrese un DNI valido");
                    guardar = false;
                }
            }


            EditText correoText = findViewById(R.id.correoRegistro);
            String correoHelper = correoText.getText().toString();
            if(correoHelper.equalsIgnoreCase("") || correoHelper == null || correoHelper.isEmpty()){
                correoText.setError("Ingrese su correo PUCP");
                guardar = false;
            }else{
                if(correoHelper.contains("@")){
                    String[] partesCorreo = correoHelper.split("@");

                    if(partesCorreo[0].length() == 0 || partesCorreo[0].length() == 1){
                        correoText.setError("Ingrese un correo PUCP valido");
                        guardar = false;

                    }

                    if (partesCorreo[1].equals("pucp.edu.pe")){
                        correo = correoHelper;
                    }else{
                        correoText.setError("Ingrese un correo PUCP valido");
                        guardar = false;
                    }

                }else{
                    correoText.setError("Ingrese un correo PUCP valido");
                    guardar = false;
                }
            }


            RadioGroup rg = findViewById(R.id.radioGroup);
            RadioButton M = findViewById(R.id.sexoMasculino);
            RadioButton F = findViewById(R.id.sexoFemenino);
            String sexo = null;

            if(!M.isChecked() && !F.isChecked()){
                guardar = false;
                F.setError("Seleccione uno");
                M.setError("Seleccione uno");

            }else{
                F.setError(null);
                M.setError(null);
                sexo = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
            }

            Spinner spinner = findViewById(R.id.spinnerRolRegistro);
            String cargoHelper =spinner.getSelectedItem().toString();
            if(cargoHelper.equals("Estudiante")){
                cargo = cargoHelper;
            }else{
                if(cargoHelper.equals("Profesor")){
                    cargo = cargoHelper;
                }else{
                    if(cargoHelper.equals("Personal Administrativo")){
                        cargo = cargoHelper;
                    }else{
                        ((TextView)spinner.getSelectedView()).setError("Seleciona un rol válido");
                        guardar = false;
                    }
                }
            }


            EditText contraText = findViewById(R.id.contrasenaRegistro);
            String contrasena = contraText.getText().toString();
            if(contrasena.equalsIgnoreCase("") || contrasena == null || contrasena.isEmpty()){
                contraText.setError("Ingrese su contraseña");
                guardar = false;
            }


            if(guardar){
                String finalCorreo = correo;
                String finalCargo = cargo;
                String finalSexo = sexo;
                String finalContrasena = sha256(contrasena);

                firebaseAuth.createUserWithEmailAndPassword(finalCorreo,finalContrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid = firebaseAuth.getUid();

                            databaseReference.child("usuario").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot == null){
                                        System.out.println("ES NULO");
                                        Usuario usuario = new Usuario(nombres,apellidos, finalCorreo, finalSexo, finalCargo,1,finalContrasena,DNI,null);
                                        databaseReference.child("usuario").child(uid).setValue(usuario);
                                        Toast.makeText(RegistroActivity.this,"Cuenta creada exitosamente!",Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(RegistroActivity.this,VistaInicioActivity.class);
                                        startActivity(i);
                                    }else{
                                        boolean existe = false;
                                        for(DataSnapshot children2 : snapshot.getChildren()){
                                            Usuario user2 = children2.getValue(Usuario.class);
                                            if(user2.getDNI().equalsIgnoreCase(DNI)){
                                                existe = true;
                                                Toast.makeText(RegistroActivity.this,"DNI ya existe!",Toast.LENGTH_SHORT).show();
                                                break;
                                            }else{
                                                if(user2.getCorreo().equals(finalCorreo)){
                                                    existe = true;
                                                    Toast.makeText(RegistroActivity.this,"Correo ya existe!",Toast.LENGTH_SHORT).show();
                                                    break;
                                                }
                                            }
                                        }

                                        if(!existe){
                                            Usuario usuario = new Usuario(nombres,apellidos, finalCorreo, finalSexo, finalCargo,1,finalContrasena,DNI,null);
                                            databaseReference.child("usuario").child(uid).setValue(usuario);
                                            Toast.makeText(RegistroActivity.this,"Cuenta creada exitosamente!",Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(RegistroActivity.this,VistaInicioActivity.class);
                                            startActivity(i);
                                        }


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RegistroActivity.this,"An error has ocurred!",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            Toast.makeText(RegistroActivity.this,"An error has ocurred!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                Toast.makeText(this,"Campo(s) incorrecto(s)!",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(this,"Campo(s) incorrecto(s)!",Toast.LENGTH_SHORT).show();
        }

    }

    public String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){

            return base;
        }
    }

    public void botonRetrocederRegistro(View view){
        Intent i = new Intent(this,VistaInicioActivity.class);
        startActivity(i);
    }

}
