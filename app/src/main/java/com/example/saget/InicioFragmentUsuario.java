package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class InicioFragmentUsuario extends Fragment {
    RecyclerView recycleview;
    DispositivosAdapter adapter;
    String tipoEquipo;



    public InicioFragmentUsuario(){

    }

    //PASARME EL TIPO DE EQUIPO PARA SETEARLO EN LA VISTA
    public InicioFragmentUsuario(String tipoEquipo){
        this.tipoEquipo = tipoEquipo;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View botonRetrocederCatalogo = view.findViewById(R.id.imageButton3);
        botonRetrocederCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonRetrocederCatalogo(view);
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_usuario,container,false);

        TextView titulo = view.findViewById(R.id.textView3);
        switch (tipoEquipo){
            case "1":
                titulo.setText("TABLET");
                break;
            case "2":
                titulo.setText("LAPTOP");
                break;
            case "3":
                titulo.setText("CELULAR");
                break;
            case "4":
                titulo.setText("MONITOR");
                break;
            default:
                titulo.setText("OTROS");
                break;
        }

        recycleview = (RecyclerView) view.findViewById(R.id.recycleriniciousuario);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Equipo> options = new FirebaseRecyclerOptions.Builder<Equipo>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("equipo")
                                .orderByChild("estado").equalTo("1_"+tipoEquipo),Equipo.class)
                .build();

        adapter = new DispositivosAdapter(options);
        recycleview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void botonRetrocederCatalogo(View view){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new CatalogoUsuarioFragment()).addToBackStack(null).commit();
    }
}