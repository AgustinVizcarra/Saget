package com.example.saget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DispositivosAdapter extends FirebaseRecyclerAdapter<Equipo,DispositivosAdapter.myViewHolder> {


    public DispositivosAdapter(@NonNull FirebaseRecyclerOptions<Equipo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Equipo equipo) {
        holder.stock.setText(String.valueOf(equipo.getStock()));
        holder.marca.setText(String.valueOf(equipo.getMarca()));

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

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            stock = itemView.findViewById(R.id.stockEquipo);
            marca = itemView.findViewById(R.id.marcaEquipo);
        }
    }

}
