package com.example.saget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DispositivosAdapter extends FirebaseRecyclerAdapter<Equipo,DispositivosAdapter.myViewHolder> {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public DispositivosAdapter(@NonNull FirebaseRecyclerOptions<Equipo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Equipo equipo) {

        holder.nombre.setText(String.valueOf(equipo.getNombre()));
        holder.stock.setText(String.valueOf(equipo.getStock()));
        holder.marca.setText(String.valueOf(equipo.getMarca()));

        ArrayList<String> imagenes = (ArrayList<String>) equipo.getImagenes();
        int n = (int) (Math.random() * (imagenes.size() - 1)) + 1;

        Glide.with(holder.imagenEquipo.getContext()).load(imagenes.get(n)).override(100,100).into(holder.imagenEquipo);
        holder.botonVerDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child("equipo").orderByChild("estado").equalTo("1_1").addListenerForSingleValueEvent(new ValueEventListener() {
                    String keyEquipo;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot children : snapshot.getChildren()){
                            Equipo equipo1 = children.getValue(Equipo.class);
                            boolean igualNombre = equipo1.getNombre().equals(equipo.getNombre());
                            boolean igualMarca = equipo1.getMarca().equals(equipo.getMarca());
                            boolean igualCaracteristicas = equipo1.getCaracteristicas().equals(equipo.getCaracteristicas());
                            boolean igualEquiposAdicionales = equipo1.getEquiposAdicionales().equals(equipo.getEquiposAdicionales());
                            if(igualNombre && igualMarca && igualCaracteristicas && igualEquiposAdicionales){
                                keyEquipo = children.getKey();
                                break;
                            }

                        }

                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new DetalleEquipoFragmentUsuario(keyEquipo)).addToBackStack(null).commit();


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //error message
                    }
                });

            }
        });

    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_dispositivos_usuario,parent,false);
        return new myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView stock;
        TextView marca;
        TextView nombre;
        Button botonVerDetalle;
        ImageView imagenEquipo;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            botonVerDetalle = itemView.findViewById(R.id.botonVerDetalle);
            nombre = itemView.findViewById(R.id.nombreEquipo);
            stock = itemView.findViewById(R.id.stockEquipo);
            marca = itemView.findViewById(R.id.marcaEquipo);
            imagenEquipo = itemView.findViewById(R.id.iconImageViewequipo);
        }
    }

}
