package com.example.saget;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.util.HashMap;

public class EditarUsuarioTIFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    String nombres,  apellidos, correo,  password;

    public EditarUsuarioTIFragment(String nombres, String apellidos, String correo, String password) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.password = password;
    }

    public EditarUsuarioTIFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_usuario_t_i, container, false);
        boolean nombresVoid = nombres==null;
        boolean apellidosVoid = apellidos==null;
        boolean correoVoid = correo==null;
        boolean passwordVoid = password==null;
        //valores variables
        TextView editarTitulo =(TextView) view.findViewById(R.id.textoModificableEditarTi);
        Button buttonEditarCampo = (Button) view.findViewById(R.id.aplicarCambios);
        TextView editarNombres = (TextView) view.findViewById(R.id.NombrePersonalTI);
        TextView editarApellidos = (TextView) view.findViewById(R.id.ApellidosPersonalTI);
        TextView editarCorreo = (TextView) view.findViewById(R.id.CorreoPersonalTI);
        TextView editarPwd = (TextView) view.findViewById(R.id.PasswordPersonalTI);
        TextView crearDni = (TextView) view.findViewById(R.id.DNIUsuarioTi);
        TextView textoDni = (TextView) view.findViewById(R.id.dniUsuarioTi);
        boolean existe;
        if(nombresVoid && apellidosVoid && correoVoid && passwordVoid){
            //crear
            editarTitulo.setText("Añadir Usuario de TI");
            buttonEditarCampo.setText("Registrar Usuario");
            crearDni.setVisibility(View.VISIBLE);
            textoDni.setVisibility(View.VISIBLE);
            existe = false;
        }else{
            //editar
            editarTitulo.setText("Editar Usuario de TI");
            buttonEditarCampo.setText("Guardar Cambios");
            //en el caso de los valores ya existentes se muestran
            editarNombres.setText(nombres);
            editarApellidos.setText(apellidos);
            editarCorreo.setText(correo);
            editarPwd.setText(password);
            existe = true;
        }
        buttonEditarCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombresText = editarNombres.getText().toString();
                String apellidosText = editarApellidos.getText().toString();
                String correoText = editarCorreo.getText().toString();
                String pwdeTxt = editarPwd.getText().toString();
                String dniTxt = crearDni.getText().toString();
                boolean nombresVacios = nombresText.equalsIgnoreCase("");
                boolean apellidosVacios = apellidosText.equalsIgnoreCase("");
                boolean correoVacio = correoText.equalsIgnoreCase("");
                boolean pwdVacio = pwdeTxt.equalsIgnoreCase("");
                boolean dniVacio = dniTxt.equalsIgnoreCase("");
                boolean dniAlfa=false;
                if(nombresVacios||apellidosVacios||correoVacio||pwdVacio){
                    if(nombresVacios){
                        editarNombres.setError("Debe ingresar un nombre");
                    }
                    if(apellidosVacios){
                        editarApellidos.setError("Debe ingresar los apellidos");
                    }
                    if(correoVacio){
                        editarCorreo.setError("Debe ingresar un correo");
                    }
                    if(pwdVacio){
                        editarPwd.setError("Debe ingresar la contraseña");
                    }
                    if(!existe&&dniVacio){
                        crearDni.setError("El DNI no puede estar vacio");
                    }
                }else{
                    //Se procede a realizar el guardado
                    if(existe){
                       firebaseDatabase.getReference().child("ti").orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               String dniEditar = snapshot.getKey();
                               HashMap<String,Object> map = new HashMap<>();
                               map.put("nombres",nombresText);
                               map.put("apellidos",apellidosText);
                               map.put("correo",correoText);
                               map.put("password",sha256(pwdeTxt));
                               firebaseDatabase.getReference().child("ti").child(dniEditar).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       Toast.makeText(getActivity().getApplicationContext(),"Se actualizo al usuario de manera exitosa",Toast.LENGTH_SHORT).show();
                                       onBackPressed();
                                   }
                               });
                           }
                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
                    }else{
                        try{
                            int dniHelper = Integer.parseInt(dniTxt);
                            int length = dniTxt.length();
                            if(length != 8){
                                dniAlfa = true;
                            }
                        }catch (NumberFormatException e){
                            dniAlfa = true;
                        }
                        if(dniAlfa){
                            crearDni.setText("Ha ingresado un DNI invalido");
                        }else{
                            Usuario usuario = new Usuario(nombresText,apellidosText,correoText,"No especifica","Operario",2,sha256(pwdeTxt));
                            firebaseDatabase.getReference("ti").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    firebaseDatabase.getReference().child("ti").child(dniTxt).setValue(usuario);
                                    Toast.makeText(getActivity().getApplicationContext(),"Se ha añadido al usuario de manera exitosa",Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
        });
        //
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton floatingBackPressed = (FloatingActionButton) view.findViewById(R.id.haciaAtras);
        floatingBackPressed.setOnClickListener(view1 -> {
            onBackPressed();
        });
        return view;
    }
    public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity)getContext();
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container_admin,new InicioAdminFragment()).addToBackStack(null).commit();
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