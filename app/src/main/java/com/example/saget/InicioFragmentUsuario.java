package com.example.saget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class InicioFragmentUsuario extends Fragment {
    RecyclerView recycleview;
    DispositivosAdapter adapter;

    public InicioFragmentUsuario(){

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

        recycleview = (RecyclerView) view.findViewById(R.id.recycleriniciousuario);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Equipo> options = new FirebaseRecyclerOptions.Builder<Equipo>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("equipo")
                                .orderByChild("estado").equalTo("1_1"),Equipo.class)
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
        //Falta implementar vista o fragmento
        //AppCompatActivity activity = (AppCompatActivity) getContext();
        //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user,new InicioFragmentUsuario()).addToBackStack(null).commit();
    }
}