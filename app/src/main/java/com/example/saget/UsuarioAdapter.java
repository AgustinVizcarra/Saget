package com.example.saget;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAdapter extends FirebaseRecyclerAdapter<Usuario,UsuarioAdapter.viewHolder> {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://saget-d5557-default-rtdb.firebaseio.com/");
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://saget-d5557.appspot.com");
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference usuariosProfileStorage = storageReference.child("Usuarios");
    List<String> filenames;

    public UsuarioAdapter(@NonNull FirebaseRecyclerOptions<Usuario> options,List<String> filenames) {
        super(options);
        this.filenames = filenames;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Usuario model) {
        holder.nombre.setText(model.getNombres());
        holder.apellido.setText(model.getApellidos());
        holder.correo.setText(model.getCorreo());
        //obtengo el DNI de los usuarios para hacer el match consigo
        databaseReference.child("usuario").addListenerForSingleValueEvent(new ValueEventListener() {
        String uri = "";
        boolean found = false;
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot userObject: snapshot.getChildren()){
                boolean jpg = filenames.contains(userObject.getKey()+"."+"jpg");
                boolean png  = filenames.contains(userObject.getKey()+"."+"png");
                if(jpg || png){
                    //Si se encuentra dentro de la lista
                    uri = jpg ? userObject.getKey()+"."+"jpg":userObject.getKey()+"."+"png";
                    Glide.with(holder.imagenUsuario.getContext()).load(usuariosProfileStorage.child(uri)).override(100,100).into(holder.imagenUsuario);
                    found = true;
                    break;
                }
            }
            //si en caso no lo haya encontrado
            if(!found) {
                uri = "defaultProfile.jpg";
                Glide.with(holder.imagenUsuario.getContext()).load(usuariosProfileStorage.child(uri)).override(100,100).into(holder.imagenUsuario);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    holder.editarUsuarioTI.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container_admin,new EditarUsuarioTIFragment(model.getNombres(),model.getApellidos(),model.getCorreo(),model.getPassword())).addToBackStack(null).commit();
        }
    });
    holder.eliminarUsuarioTI.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.eliminarUsuarioTI.getContext());
            builder.setTitle("Eliminar usuario");
            builder.setMessage("¿Está seguro que desea eliminar al usuario"+model.getNombres()+" "+model.getApellidos()+"? ");
            builder.setPositiveButton("Sí",(dialogInterface, i) -> {
                databaseReference.child("usuario").child(getRef(holder.getAbsoluteAdapterPosition()).getKey()).removeValue();
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }
    });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_personal_ti,parent,false);
        return new viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView apellido;
        TextView correo;
        Button editarUsuarioTI;
        Button eliminarUsuarioTI;
        ImageView imagenUsuario;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            editarUsuarioTI = itemView.findViewById(R.id.editarTi);
            eliminarUsuarioTI = itemView.findViewById(R.id.eliminarTi);
            nombre = itemView.findViewById(R.id.nombreTI);
            apellido = itemView.findViewById(R.id.apellidoTI);
            correo = itemView.findViewById(R.id.correoTI);
            imagenUsuario = itemView.findViewById(R.id.iconImageViewPersonalTI);

        }
    }
}
