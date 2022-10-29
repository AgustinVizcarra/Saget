package com.example.saget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saget.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        firebaseDatabase = FirebaseDatabase.getInstance("https://saget-d5557-default-rtdb.firebaseio.com/");

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
        String rol = null;

        DatabaseReference databaseReference = firebaseDatabase.getReference();
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


        EditText codigoText = findViewById(R.id.codigoPUCP);
        String codigoPUCP = codigoText.getText().toString();
        if(codigoPUCP.equalsIgnoreCase("") || codigoPUCP == null || codigoPUCP.isEmpty()){
            codigoText.setError("Ingrese su código PUCP");
            guardar = false;
        }else{
            try{
                int code = Integer.parseInt(codigoPUCP);
                Calendar cal= Calendar.getInstance();
                int year= cal.get(Calendar.YEAR);

                if(code<1917 || code>year){
                    codigoText.setError("Ingrese un código PUCP valido");
                    guardar = false;
                }

            }catch (NumberFormatException e){
                codigoText.setError("Ingrese un código PUCP valido");
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

                if(partesCorreo[0].equals("") || partesCorreo[0].isEmpty()){
                    correoText.setError("Ingrese un correo PUCP valido");
                    guardar = false;
                }else{
                    if (partesCorreo[1].equals("pucp.edu.pe")){
                        correo = correoHelper;
                    }else{
                        correoText.setError("Ingrese un correo PUCP valido");
                        guardar = false;
                    }
                }

            }else{
                correoText.setError("Ingrese un correo PUCP valido");
                guardar = false;
            }
        }


        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        String sexo = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();


        Spinner spinner = findViewById(R.id.spinnerRolRegistro);
        String rolHelper =spinner.getSelectedItem().toString();
        if(rolHelper.equals("Estudiante")){
            rol = rolHelper;
        }else{
            if(rolHelper.equals("Profesor")){
                rol = rolHelper;
            }else{
                if(rolHelper.equals("Personal Administrativo")){
                    rol = rolHelper;
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


        //GUARDAMOS
        if(guardar){
            Usuario usuario = new Usuario(nombres,apellidos,correo,sexo,rol,sha256(contrasena));
            databaseReference.child("usuario").child(codigoPUCP).setValue(usuario);
            Toast.makeText(this,"Cuenta creada exitosamente",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Campo(s) incorrecto(s)",Toast.LENGTH_SHORT).show();
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

}