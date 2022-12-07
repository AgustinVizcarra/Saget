package com.example.saget;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SolicitudesTiAdapter extends FirebaseRecyclerAdapter<SolicitudDePrestamo,SolicitudesTiAdapter.myViewHolder> {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String keySoliTi;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public SolicitudesTiAdapter(@NonNull FirebaseRecyclerOptions<SolicitudDePrestamo> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_solicitudes_ti,parent,false);
        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull SolicitudesTiAdapter.myViewHolder holder, int position, @NonNull SolicitudDePrestamo solicitudDePrestamo) {



        databaseReference.child("prestamos").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren()){
                    SolicitudDePrestamo solicitudDePrestamo1 = children.getValue(SolicitudDePrestamo.class);
                    boolean igualUsuario = solicitudDePrestamo1.getUsuario().equals(solicitudDePrestamo.getUsuario());
                    boolean igualFoto = solicitudDePrestamo1.getFoto().equals(solicitudDePrestamo.getFoto());
                    boolean igualObservacion = solicitudDePrestamo1.getObservacion().equals(solicitudDePrestamo.getObservacion());
                    boolean igualMotivo = solicitudDePrestamo1.getMotivo().equals(solicitudDePrestamo.getMotivo());
                    boolean igualCurso=solicitudDePrestamo1.getCurso().equals(solicitudDePrestamo.getCurso());
                    boolean igualDetalles=solicitudDePrestamo1.getDetalles().equals(solicitudDePrestamo.getDetalles());
                    boolean igualProgramas=solicitudDePrestamo1.getProgramas().equals(solicitudDePrestamo.getProgramas());
                    boolean igualTiempoPres=solicitudDePrestamo1.getTiempoPrestamo().equals(solicitudDePrestamo.getTiempoPrestamo());
                    if(igualUsuario && igualFoto && igualObservacion && igualMotivo && igualCurso && igualDetalles && igualProgramas && igualTiempoPres){
                        keySoliTi = children.getKey();
                        break;
                    }

                }
                holder.numeroSoli.setText(keySoliTi);
                //falta considerar fecha de solicitud en usuario
                holder.fechaSolic.setText("");

                holder.equipoSoli.setText(solicitudDePrestamo.getEquipo());
                holder.tiempoPrestamoSoli.setText(solicitudDePrestamo.getTiempoPrestamo());

                //btn ver mas
                holder.btn_vermassoli.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new DetallesSolicitudTiFragment(keySoliTi)).addToBackStack(null).commit();
                        Log.d("msglist",keySoliTi);
                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message
            }
        });




    }


    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView numeroSoli;
        TextView fechaSolic;
        TextView equipoSoli;
        TextView tiempoPrestamoSoli;
        Button btn_vermassoli;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroSoli = itemView.findViewById(R.id.numeroSolicitudTi);
            fechaSolic = itemView.findViewById(R.id.fechaSolicitud);
            equipoSoli = itemView.findViewById(R.id.equipoSolicitud);
            tiempoPrestamoSoli = itemView.findViewById(R.id.tiempoPrestamoSolicitud);
            btn_vermassoli = itemView.findViewById(R.id.btn_vermassolic);



        }
    }
}
