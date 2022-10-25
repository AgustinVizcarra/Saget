package com.example.saget;

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

import java.util.ArrayList;
import java.util.List;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

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


        EditText nombresText = findViewById(R.id.nombresRegistro);
        String nombres = nombresText.getText().toString();
        if(nombres.equalsIgnoreCase("") || nombres == null){
            nombresText.setError("Ingrese sus nombres");
            guardar = false;
        }


        EditText apellidosText = findViewById(R.id.apellidossRegistro);
        String apellidos = apellidosText.getText().toString();
        if(apellidos.equalsIgnoreCase("") || apellidos == null){
            apellidosText.setError("Ingrese sus apellidos");
            guardar = false;
        }


        EditText codigoText = findViewById(R.id.codigoPUCP);
        String codigoPUCP = codigoText.getText().toString();
        if(codigoPUCP.equalsIgnoreCase("") || codigoPUCP == null){
            codigoText.setError("Ingrese su código PUCP");
            guardar = false;
        }


        EditText correoText = findViewById(R.id.correoRegistro);
        String correoHelper = correoText.getText().toString();
        if(correoHelper.equalsIgnoreCase("") || correoHelper == null){
            correoText.setError("Ingrese su correo PUCP");
            guardar = false;
        }else{
            if(correoHelper.contains("@")){
                String[] partesCorreo = correoHelper.split("@");
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
                }
            }
        }


        EditText contraText = findViewById(R.id.contrasenaRegistro);
        String contrasena = contraText.getText().toString();
        if(contrasena.equalsIgnoreCase("") || contrasena == null){
            contraText.setError("Ingrese su contraseña");
            guardar = false;
        }


        if(guardar){
            //Guardar y ya
            Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
        }

    }


}