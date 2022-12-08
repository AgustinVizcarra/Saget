package com.example.saget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UsuarioAdapter extends FirebaseRecyclerAdapter<Usuario,UsuarioAdapter.viewHolder> {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference usuariosProfileStorage = storageReference.child("Usuarios");

    public UsuarioAdapter(@NonNull FirebaseRecyclerOptions<Usuario> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UsuarioAdapter.viewHolder holder, int position, @NonNull Usuario model) {
        holder.nombre.setText(model.getNombres());
        holder.apellido.setText(model.getApellidos());
        holder.correo.setText(model.getCorreo());
        holder.sexo.setText(model.getSexo());
        holder.cargo.setText(model.getCargo());
        if(model.getFoto()!=null){
            String url = (String) model.getFoto().get(1);
            Glide.with(holder.imagenUsuario.getContext()).load(url).override(100,100).into(holder.imagenUsuario);
        }else{
            Glide.with(holder.imagenUsuario.getContext()).load(usuariosProfileStorage.child("defaultProfile.jpg")).override(100,100).into(holder.imagenUsuario);
        }
    }

    @NonNull
    @Override
    public UsuarioAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_usuarios,parent,false);
        return new UsuarioAdapter.viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        TextView apellido;
        TextView correo;
        TextView cargo;
        TextView sexo;
        ImageView imagenUsuario;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreU);
            apellido = itemView.findViewById(R.id.apellidoU);
            correo = itemView.findViewById(R.id.correoU);
            cargo = itemView.findViewById(R.id.cargoU);
            sexo = itemView.findViewById(R.id.sexoU);
            imagenUsuario = itemView.findViewById(R.id.iconImageViewUsuario);
        }
    }




}
