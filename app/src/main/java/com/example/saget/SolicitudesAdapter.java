package com.example.saget;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SolicitudesAdapter extends FirebaseRecyclerAdapter<SolicitudDePrestamo,SolicitudesAdapter.myViewHolder> {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    MessageFragment messageFragment;

    public SolicitudesAdapter(@NonNull FirebaseRecyclerOptions<SolicitudDePrestamo> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull SolicitudDePrestamo solicitudDePrestamo) {
        int orden = position + 1;

        System.out.println("------------");
        System.out.println(orden);
        if(solicitudDePrestamo.getEstado().equals("En trámite")){
            holder.estado.setTextColor(Color.parseColor("#FAFF00"));
        }else{
            if(solicitudDePrestamo.getEstado().equals("Aprobado")){
                holder.estado.setTextColor(Color.parseColor("#24FF00"));
            }else{
                holder.estado.setTextColor(Color.parseColor("#FF0505"));
            }
        }

        holder.estado.setText(String.valueOf(solicitudDePrestamo.getEstado()));

        holder.numeroSolicitud.setText("Fecha: \n"+solicitudDePrestamo.getFechasoli());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String solicitudID = getRef(orden-1).getKey();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                messageFragment = new MessageFragment("Solicitud #"+ orden,solicitudDePrestamo.getObservacion(),solicitudID);
                messageFragment.show(activity.getSupportFragmentManager(),"My fragment");

            }
        });

    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_solicitudes_usuario,parent,false);
        return new myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView estado;
        TextView numeroSolicitud;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            estado = itemView.findViewById(R.id.namesolicitud3);
            numeroSolicitud = itemView.findViewById(R.id.namesolicitud);

        }
    }
}
