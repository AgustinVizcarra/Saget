package com.example.saget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DispositivosAdapter extends RecyclerView.Adapter<DispositivosAdapter.DispositivosViewHolder> {

    private ArrayList<Equipo> equipoArrayList;
    private Context context;

    public ArrayList<Equipo> getEquipoArrayList() {
        return equipoArrayList;
    }

    public void setEquipoArrayList(ArrayList<Equipo> equipoArrayList) {
        this.equipoArrayList = equipoArrayList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public DispositivosAdapter(Context context,ArrayList<Equipo> arrayList){
        this.equipoArrayList = arrayList;
        this.context = context;
    }

    class DispositivosViewHolder extends RecyclerView.ViewHolder {
        Equipo equipo;

        public DispositivosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public DispositivosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_inicio_usuario, parent, false);
        return new DispositivosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DispositivosViewHolder holder, int position) {
        Equipo eq = equipoArrayList.get(position);
        holder.equipo = eq;

        TextView textView = holder.itemView.findViewById(R.id.stock);
        textView.setText(String.valueOf(eq.getStock()));

        TextView textView2 = holder.itemView.findViewById(R.id.marca);
        textView2.setText(eq.getMarca());
    }


    @Override
    public int getItemCount() {
        return equipoArrayList.size();
    }



}
