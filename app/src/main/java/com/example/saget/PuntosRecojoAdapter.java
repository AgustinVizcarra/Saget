package com.example.saget;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PuntosRecojoAdapter extends FirebaseRecyclerAdapter<PuntoRecojo,PuntosRecojoAdapter.PuntosRecojoViewHolder> {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public PuntosRecojoAdapter(@NonNull FirebaseRecyclerOptions<PuntoRecojo> options) {
        super(options);
    }

    @NonNull
    @Override
    public PuntosRecojoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_puntos_recojo,parent,false);
        return new PuntosRecojoViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PuntosRecojoViewHolder holder, int position, @NonNull PuntoRecojo model) {
        holder.descripcion.setText(String.valueOf(model.getDescripcion()));
        holder.coordenadas.setText(String.valueOf(model.getCoordenadas()));
        String urlImagen = (String) model.getImagenes();
        Glide.with(holder.imagenPuntoRecojo.getContext()).load(urlImagen).override(100,100).into(holder.imagenPuntoRecojo);
        //Editar
        holder.btnEditarPRecojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_admin,new EditPuntoRecojoFragment(model)).addToBackStack(null).commit();
            }
        });
        //Eliminar
        holder.btnEliminarPRecojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle("SAGET");
                alertDialog.setMessage("¿Estás seguro de eliminar el punto de Recojo?");
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseDatabase.getReference().child("puntos_recojo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("estado","0");
                                firebaseDatabase.getReference().child("puntos_recojo").child(model.getKey()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(view.getContext(), "Se elimino correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public class PuntosRecojoViewHolder extends RecyclerView.ViewHolder{
        TextView descripcion;
        TextView coordenadas;
        ImageView imagenPuntoRecojo;
        Button btnEditarPRecojo;
        Button btnEliminarPRecojo;
        public PuntosRecojoViewHolder(@NonNull View itemView){
            super(itemView);
            descripcion = itemView.findViewById(R.id.descripcionPRecojo);
            coordenadas = itemView.findViewById(R.id.coordenadasPRRellenar);
            imagenPuntoRecojo = itemView.findViewById(R.id.iconPuntoRecojo);
            btnEditarPRecojo = itemView.findViewById(R.id.editarPR);
            btnEliminarPRecojo = itemView.findViewById(R.id.eliminarPR);
        }
    }
}
