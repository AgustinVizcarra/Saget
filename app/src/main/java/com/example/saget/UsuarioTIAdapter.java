package com.example.saget;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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

public class UsuarioTIAdapter extends FirebaseRecyclerAdapter<Usuario, UsuarioTIAdapter.viewHolder> {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference usuariosProfileStorage = storageReference.child("Usuarios");


    public UsuarioTIAdapter(@NonNull FirebaseRecyclerOptions<Usuario> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Usuario model) {
        holder.nombre.setText(model.getNombres());
        holder.apellido.setText(model.getApellidos());
        holder.correo.setText(model.getCorreo());
        if(model.getFoto()!=null){
            String url = (String) model.getFoto().get(1);
            Glide.with(holder.imagenUsuario.getContext()).load(url).override(100,100).into(holder.imagenUsuario);
        }else{
            Glide.with(holder.imagenUsuario.getContext()).load(usuariosProfileStorage.child("defaultProfile.jpg")).override(100,100).into(holder.imagenUsuario);
        }
        holder.editarUsuarioTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container_admin, new EditarUsuarioTIFragment(model.getNombres(), model.getApellidos(), model.getCorreo(), model.getPassword())).addToBackStack(null).commit();
            }
        });
        holder.eliminarUsuarioTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.eliminarUsuarioTI.getContext());
                builder.setTitle("Eliminar usuario");
                builder.setMessage("¿Está seguro que desea eliminar al usuario" + model.getNombres() + " " + model.getApellidos() + "? ");
                builder.setPositiveButton("Sí", (dialogInterface, i) -> {
                    databaseReference.child("ti").child(getRef(holder.getAbsoluteAdapterPosition()).getKey()).removeValue();
                    Toast.makeText(view.getContext(), "Se elimino el usuario exitosamente!", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_personal_ti, parent, false);
        return new viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView apellido;
        TextView correo;
        ImageButton editarUsuarioTI;
        ImageButton eliminarUsuarioTI;
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
