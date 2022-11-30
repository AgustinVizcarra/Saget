package com.example.saget;

import android.content.DialogInterface;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DispositivosTIAdapter extends FirebaseRecyclerAdapter<Equipo,DispositivosTIAdapter.DispositivosViewHolder> {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String keyEquipo;
    int tipoEquipo;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DispositivosTIAdapter(@NonNull FirebaseRecyclerOptions<Equipo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DispositivosTIAdapter.DispositivosViewHolder holder, int position, @NonNull Equipo equipo) {
        holder.nombreTI.setText(String.valueOf(equipo.getNombre()));
        holder.stockTI.setText(String.valueOf(equipo.getStock()));
        holder.marcaTI.setText(String.valueOf(equipo.getMarca()));

        ArrayList<String> imagenes = (ArrayList<String>) equipo.getImagenes();
        int n = (int) (Math.random() * (imagenes.size() - 1)) + 1;

        Glide.with(holder.imagenEquipoTI.getContext()).load(imagenes.get(n)).override(100,100).into(holder.imagenEquipoTI);



        //boton editar
        holder.btnEditarDispoTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                databaseReference.child("equipo").addListenerForSingleValueEvent(new ValueEventListener() {

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
                                tipoEquipo=equipo1.getTipo();
                                break;
                            }

                        }
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_TI,new FormEditDispositivosFragment(keyEquipo,tipoEquipo)).addToBackStack(null).commit();
                        Log.d("msglist",keyEquipo);


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //error message
                    }
                });



            }
        });
        //btn borrar
        holder.btnBorrarDispoTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle("SAGET");
                alertDialog.setMessage("¿Estás seguro de eliminar el equipo?");
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("equipo").addListenerForSingleValueEvent(new ValueEventListener() {

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
                                        tipoEquipo=equipo1.getTipo();
                                        break;
                                    }

                                }

                                Log.d("msglist",keyEquipo);


                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                //error message
                            }
                        });
                        databaseReference.child("equipo").child(keyEquipo).removeValue();
                        Toast.makeText(view.getContext(), "Se elimino correctamente", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();

            }
        });

    }

    @NonNull
    @Override
    public DispositivosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_dispositivos_ti,parent,false);
        return new DispositivosViewHolder(view);
    }

    public class DispositivosViewHolder extends RecyclerView.ViewHolder{
        TextView stockTI;
        TextView marcaTI;
        TextView nombreTI;
        ImageButton btnEditarDispoTI;
        ImageButton btnBorrarDispoTI;
        ImageView imagenEquipoTI;

        public DispositivosViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEditarDispoTI = itemView.findViewById(R.id.btnEditListaTi);
            btnBorrarDispoTI = itemView.findViewById(R.id.btnBorrarListaTi);
            nombreTI = itemView.findViewById(R.id.nombreEquipoDispoTI);
            stockTI = itemView.findViewById(R.id.stockEquipoDispoTI);
            marcaTI = itemView.findViewById(R.id.marcaEquipoDispoTI);
            imagenEquipoTI = itemView.findViewById(R.id.iconImageViewDispositivoTI);
        }
    }

}
